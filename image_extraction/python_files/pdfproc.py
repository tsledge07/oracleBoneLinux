#!/usr/bin/env python3

'''
Author: Randolph Sapp
Info: Tool to rip images and transcript from bone script PDF
'''

import glob
import json
import os
import cv2 as cv
from image_extraction.parser import Parser
from image_extraction.tablereader import TableReader

ROW_DESC = ['SectionHeader', 'CharNo', 'Script', 'ChnChar', 'Phonetic',
            'SpecimenNo', 'LCNo', 'Notes', 'SeqNo']
CARRY = [True, True, False, False, True, False, True, False, False]

# fetch all PDFs
for path in glob.glob('*.pdf'):
    # make sure an image directory exists
    name = os.path.splitext(path)[0] + '-pre'
    if not os.path.isdir(name):
        continue

    # prepare to parse pdf
    pdf = open(path, 'rb')
    pr = Parser(pdf)

    # generate output directory
    outputdir = name[:-4] + '-proc'
    os.makedirs(outputdir, exist_ok=True)

    # prepare refrence values
    oldelements, oldtranscript = [], {}
    ARI = 0

    # process only the pages we have images for in order
    for pi, impath in enumerate(sorted(glob.glob(os.path.join(name, '*.png')))):
        page = cv.imread(impath)
        tr = TableReader(page)
        pr.read_page(pi)

        # generate row directories and populate with images
        for ri in range(tr.numrows-1):
            row = tr.get_row(ri)
            rowdir = os.path.join(outputdir, str(ARI))
            os.makedirs(rowdir, exist_ok=True)
            transcript = {}
            # try to fetch transcript and image for each row
            for ci, element in enumerate(row):
                title = ROW_DESC[ci]
                x1, y1, x2, y2 = tr.get_cell_bounds(ri, ci)

                # if transcipt has value: use it
                text = pr.get_text(x1, y1, x2, y2)
                if len(text) > 0:
                    transcript[title] = text
                # otherwise check to see if we should carry the previous value
                elif CARRY[ci]:
                    transcript[title] = oldtranscript[title]

                # if cell is empty: see if we should carry it
                if tr.is_cell_empty(ri, ci) and CARRY[ci]:
                    element = oldelements[ci]
                    row[ci] = element

                # dump image to directory tree
                cv.imwrite(os.path.join(rowdir, title+'.png'), element)

            # dump transcript to directory tree
            json.dump(transcript, open(os.path.join(rowdir, 'transcript.json'), 'w'))

            # update refrence values
            oldelements, oldtranscript = row, transcript
            ARI += 1
