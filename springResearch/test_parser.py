'''
Author: Randolph Sapp
Info: UnitTest for parser class
'''

import unittest
import json
from image_extraction.parser import Parser

class TestParser(unittest.TestCase):
    '''
    automated unit testing for Parser
    '''

    def setUp(self):
        '''
        redundant setup stuff
        '''
        with open("table_p1.pdf", 'rb') as doc:
            self.parser = Parser(doc)
            self.parser.read_page(0)

    def test_page_dict(self):
        '''
        be sure page dictionary is reasonable
        '''
        with open('pdict_p1.json', 'r') as jfile:
            pdict = json.load(jfile)
            for rowi, row in self.parser.pdict.items():
                for coli, val in row.items():
                    self.assertEqual(val, pdict[str(rowi)][str(coli)])

    def test_get_text(self):
        '''
        be sure get_text works
        '''
        text = self.parser.get_text(0.42661290322580650,
                                    0.07210031347962383,
                                    0.52822580645161290,
                                    0.09318894271872329)
        self.assertEqual(text, 'H05611')


if __name__ == '__main__':
    unittest.main()
