#!/usr/bin/env python3

'''
Author: Randolph Sapp
Info: Tool to navigate output tree and apply post-processing routines
'''

import os
import glob
import cv2 as cv

FILES = ['Script.png']

# fetch directories to process
for d in glob.glob('*-proc'):
    # fetch row directories to read through
    for row in glob.glob(os.path.join(d, '*')):
        # process each file picked in FILES
        for f in FILES:
            path = os.path.join(row, f)

            # skip files that don't exist
            if not os.path.isfile(path):
                continue

            # save output to new directory tree
            outdir = os.path.join(d[:-5]+'-post', row)
            os.makedirs(outdir, exist_ok=True)
            outpath = os.path.join(outdir, f[:-4]+'-thinned.png')
            image = cv.imread(path, cv.IMREAD_GRAYSCALE)
            thinned = cv.ximgproc.thinning(image)
            cv.imwrite(outpath, thinned)
