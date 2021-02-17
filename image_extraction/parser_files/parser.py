'''
Author: Randolph Sapp
Info: Tool to generate transcripts from PDFs
'''

from pdfminer.pdfparser import PDFParser
from pdfminer.pdfdocument import PDFDocument
from pdfminer.pdfpage import PDFPage
from pdfminer.pdfpage import PDFTextExtractionNotAllowed
from pdfminer.pdfinterp import PDFResourceManager
from pdfminer.pdfinterp import PDFPageInterpreter
from pdfminer.layout import LAParams
from pdfminer.converter import PDFPageAggregator
import pdfminer

X_RES = 10
Y_RES = 1

class Parser:
    '''
    class to convert PDF into objects and read through them as needed
    '''
    def __init__(self, doc):
        parser = PDFParser(doc)
        document = PDFDocument(parser)
        if not document.is_extractable:
            raise PDFTextExtractionNotAllowed
        rsrcmgr = PDFResourceManager()
        laparams = LAParams()
        self.device = PDFPageAggregator(rsrcmgr, laparams=laparams)
        self.interpreter = PDFPageInterpreter(rsrcmgr, self.device)
        self.pages = list(PDFPage.create_pages(document))
        self.characters = []
        self.width, self.height = 0, 0
        self.pdict = {}

    def read_page(self, ipage):
        '''
        prepare page for extraction
        '''
        self.interpreter.process_page(self.pages[ipage])
        layout = self.device.get_result()
        _, _, self.width, self.height = self.pages[ipage].mediabox
        self.parse_obj(layout._objs)
        self.gen_dict()

    def parse_obj(self, lt_objs):
        '''
        recursivly search for text and whitespace objects
        '''
        for obj in lt_objs:
            if isinstance(obj, pdfminer.layout.LTChar):
                self.characters.append(obj)
            if isinstance(obj, pdfminer.layout.LTAnno):
                self.characters.append(obj)
            # if it's a container, recurse
            elif hasattr(obj, '_objs'):
                self.parse_obj(obj._objs)

    def gen_dict(self):
        '''
        create dictionary of characters and locations
        '''
        for i, obj in enumerate(self.characters):
            if isinstance(obj, pdfminer.layout.LTChar):
                x_1, y_1, x_2, y_2 = obj.bbox
                rowpos = int((y_1+y_2)/2 * Y_RES)
                colpos = int((x_1+x_2)/2 * X_RES)
                rdict = self.pdict.get(rowpos, {})
                text = obj.get_text()
                if i < len(self.characters)-1:
                    nextobj = self.characters[i+1]
                    if isinstance(nextobj, pdfminer.layout.LTAnno):
                        text += ' '
                rdict[colpos] = text
                self.pdict[rowpos] = rdict

    def get_text(self, x_1, y_1, x_2, y_2):
        '''
        fetch text in given area from dictionary
        '''
        output = ""
        for key in sorted(self.pdict.keys()):
            vpercent = 1-(key/self.height)
            if y_1 < vpercent < y_2:
                rowdict = self.pdict[key]
                for pos in sorted(rowdict.keys()):
                    xpercent = (pos/X_RES)/self.width
                    if x_1 < xpercent < x_2:
                        output += rowdict[pos]
        return output.strip()
