'''
Author: Randolph Sapp
Info: UnitTest for tablereader class
'''

import os
import glob
import unittest
import cv2 as cv
from image_extraction.tablereader import TableReader

ROW_DESC = ['SectionHeader', 'CharNo', 'Script', 'ChnChar', 'Phonetic',
            'SpecimenNo', 'LCNo', 'Notes', 'SeqNo']

class TestTableReader(unittest.TestCase):
    '''
    automated unit testing for Parser
    '''

    def setUp(self):
        '''
        redundant setup stuff
        '''
        page = cv.imread(os.path.join('table_p1-pre', '0001-1.png'))
        self.tabr = TableReader(page)

    def test_get_cell(self):
        '''
        compare cells against known values
        '''
        for row in next(os.walk('table_p1-proc-no-carry'))[1]:
            rpath = os.path.join('table_p1-proc-no-carry', row)
            for ipath in glob.glob(rpath+'*.png'):
                name = os.path.splitext(ipath)[0]
                if name not in ROW_DESC:
                    continue
                col = ROW_DESC.index(name)
                image = cv.imread(path, cv.IMREAD_GRAYSCALE)
                self.assertEqual(image, self.tabr.get_cell(row, col))

    def test_is_cell_empty(self):
        '''
        make sure empty cells are properly identified
        '''
        self.assertEqual(True, self.tabr.is_cell_empty(10, 1))
        self.assertEqual(False, self.tabr.is_cell_empty(38, 2))

    def test_get_cell_bounds(self):
        '''
        check that cell bounds are properly reported
        '''
        bound_5_1 = [0.15887096774193550, 0.16443431176973497,
                     0.21733870967741936, 0.18552294100883443]
        bound_1_5 = [0.42661290322580650, 0.07210031347962383,
                     0.52822580645161290, 0.09318894271872329]
        self.assertEqual(bound_5_1, self.tabr.get_cell_bounds(5, 1))
        self.assertEqual(bound_1_5, self.tabr.get_cell_bounds(1, 5))

    def test_get_row(self):
        '''
        verify that a row image list is returned properly
        '''
        for row in next(os.walk('table_p1-proc-no-carry'))[1]:
            rpath = os.path.join('table_p1-proc-no-carry', row)
            for i, image in enumerate(self.tabr.get_row(int(row))):
                path = os.path.join(rpath, ROW_DESC[i]+'.png')
                ref = cv.imread(path, cv.IMREAD_GRAYSCALE)
                self.assertEqual(image.all(), ref.all())


if __name__ == '__main__':
    unittest.main()
