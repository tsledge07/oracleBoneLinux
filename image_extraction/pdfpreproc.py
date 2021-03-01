#!/usr/bin/env python3

'''
Author: Randolph Sapp
Info: Tool to parse tables in PDFs
'''

import glob
import os
from pdf2image import convert_from_path

# fetch list of PDFs to process
for path in glob.glob("*.pdf"):
    # make image directory tree
    name = os.path.splitext(path)[0] + '-pre'
    os.makedirs(name, exist_ok=True)

    # use poppler to generate images
    convert_from_path(path, dpi=300, output_folder=name, use_pdftocairo=True,
                      output_file='', fmt='png', thread_count=4)
