'''
Author: Randolph Sapp
Info: Tool to parse tables in PDFs
'''

import numpy as np
import cv2 as cv

class TableReader:
    '''
    class to split tables into cells and fetch these cells at will
    '''
    def __init__(self, page):
        self.dst = cv.cvtColor(page, cv.COLOR_BGR2GRAY)
        _, self.thresh = cv.threshold(self.dst, 200, 255, cv.THRESH_BINARY_INV)
        self.numrows, self.numcols = 0, 0
        self.h_lines, self.v_lines = [], []
        self.update_lines()

    def update_lines(self):
        '''
        search for lines and update entries
        '''
        self.numrows, self.numcols = 0, 0
        self.h_lines, self.v_lines = [], []
        lines = cv.HoughLinesP(self.thresh, 1, np.pi*0.5, 500, None, 500, 5)
        if lines is not None:
            for line in sorted(lines, key=lambda x:x[0][0]):
                x_1, y_1, x_2, y_2 = line[0]
                if self.numcols < 1 or abs(x_1-self.v_lines[-1][0]) > 20:
                    self.v_lines.append([x_1, y_1, x_2, y_2])
                    self.numcols += 1
            for line in sorted(lines, key=lambda x:x[0][1]):
                x_1, y_1, x_2, y_2 = line[0]
                if self.numrows < 1 or abs(y_1-self.h_lines[-1][1]) > 20:
                    self.h_lines.append([x_1, y_1, x_2, y_2])
                    self.numrows += 1

    def get_cell(self, j, i):
        '''
        return image array of specified cell
        '''
        return self.thresh[self.h_lines[j][1]+5:self.h_lines[j+1][1]-2,
               self.v_lines[i][0]+5:self.v_lines[i+1][0]-2]

    def is_cell_empty(self, j, i):
        '''
        check if cell is empty enough to be called such
        '''
        cell = self.get_cell(j, i)
        return cell.size*0.01 > cv.countNonZero(cell)

    def get_cell_bounds(self, j, i):
        '''
        fetch the cell bounds from a given index
        '''
        return [(self.v_lines[i][0]+5)/self.dst.shape[1],
                (self.h_lines[j][1]+5)/self.dst.shape[0],
                (self.v_lines[i+1][0]-2)/self.dst.shape[1],
                (self.h_lines[j+1][1]-2)/self.dst.shape[0]]

    def get_row(self, j):
        '''
        return list of cells from given row index
        '''
        output = []
        for i in range(len(self.v_lines)-1):
            output.append(self.get_cell(j, i))
        return output
