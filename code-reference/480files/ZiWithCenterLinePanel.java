package oracle;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

public 
class ZiWithCenterLinePanel extends JPanel {
	private BufferedImage ziImage, bimg;
	private int[][] centerLine;

	public ZiWithCenterLinePanel(BufferedImage ziImage) {
		this.ziImage = ziImage;
		Dimension d = new Dimension(2 * ziImage.getWidth() + 30, 
				ziImage.getHeight());
		this.setPreferredSize(d);
		
		// create an image with center line
        bimg = new BufferedImage(ziImage.getWidth(), 
        		ziImage.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D gg = bimg.createGraphics();
	}
	
	public void setCenterLine(int[][] clPattern) {
		centerLine = clPattern;
	}
	
	public void drawCenterLine() {
        int seqNo = 1, countNeighbor = 0, lineNo = 0;
        NumberedVertex v;
        
        for (int col = 0; col < centerLine[0].length; col++) {
        	//System.out.print(((x<10)?" ":"") + x + ":");
	        for (int row = 0; row < centerLine.length; row++) {
	        	//lineNo++;
	        	
	        	if (centerLine[row][col] == 1) {
	    			//System.out.print(lineNo + " dot @(" + col + ", " + row + ")");
	        		bimg.setRGB(col, row, 255);
	        	}
	        }
        }
		
		printImage();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(ziImage, 0, 0, null);
		
        int offset = ziImage.getWidth() + 30;
		g.drawImage(bimg, offset, 0, null);
		
	}
	
	private void printImage() {

		int[][] bw = centerLine.clone();

        for (int x = 0; x < bw.length; x++) {
        	System.out.print(((x<10)?" ":"") + x + ":");
	        for (int y = 0; y < bw[0].length; y++) {
	        	System.out.print((centerLine[x][y]==0)?".":bw[x][y]);
	        }
        	System.out.println();
        }
	}
}