'''
Author: Randolph Sapp
Info: Pip compatible setup script
'''

from setuptools import setup

with open('README.md', 'r') as fh:
    long_description = fh.read()

with open('requirements.txt') as f:
    install_requires = f.read().splitlines()

setup(
        name = 'image_extraction',
        version = '0.0.1',
        author = 'Randolph Sapp',
        description = 'Rip info and images from PDF tables.',
        long_description=long_description,
        long_description_content_type='text/markdown',
        url = 'https://github.com/tsledge07/csc480-oracle-bone',
        packages = ['image_extraction'],
        scripts = ['pdfproc.py',
                   'pdfpreproc.py',
                   'pdfpostproc.py'],
        python_requires = '>=3.6',
        install_requires = install_requires,
)
