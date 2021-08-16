package animazi;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class StrokeImageParser {
	int strokeNo = 0;
	static int rows = 1, cols = 6;
	static int x = 0, y = 0, gap = 3, size = 150;
	
	private static void testModifyScript(Image ziImg) {
		rows = ziImg.getHeight(null) / size;
		//System.out.println(rows);
		List<Image> ziByStrokes = new LinkedList<Image>();
		BufferedImage bimPage = new BufferedImage(ziImg.getWidth(null), 
				ziImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
		bimPage.getGraphics().drawImage(ziImg, 0, 0, null);
		
		BufferedImage defaultImg = new BufferedImage(size, 
				size, BufferedImage.TYPE_BYTE_GRAY);
		
		defaultImg.getGraphics().drawImage(
				bimPage.getSubimage(4*(150+3), (rows-1) * 153, size, size), 0, 0, null);
		JOptionPane.showConfirmDialog(null, "Color image...", 
				"Harvested Script Images", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, 
				new ImageIcon(bimPage.getSubimage(3*(150+3), (rows-1) * 153, size, size)));
		JOptionPane.showConfirmDialog(null, "Gray image...", 
				"Harvested Script Images", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(defaultImg));
		/*
		System.out.println(defaultImg.getRGB(48,  22));
		System.out.println(defaultImg.getRGB(129,  107));
		System.out.println(defaultImg.getRGB(0,  0));
		System.out.println(defaultImg.getRGB(142,  30));
		*/
		
		int rgb;
		//int count = 0;
		for (int i = 0; i < size; i++)
			for (int j=0; j<size; j++) {
				rgb = defaultImg.getRGB(i, j);
				if (rgb > -6000000 && rgb < -3000000)
					defaultImg.setRGB(i, j, -8618884);
				else if (rgb > -3000000 && rgb < -2500000) {
					defaultImg.setRGB(i, j, -1);
					//System.out.println((++count) + "@" + i + "-" + j);
				}
			}
		JOptionPane.showConfirmDialog(null, "Gray image... Modified!", 
				"Harvested Script Images", JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(defaultImg));
	}

	public static List<Image> parseImage(Image ziImg) {
		rows = ziImg.getHeight(null) / size;
		//System.out.println(rows);
		List<Image> ziByStrokes = new LinkedList<Image>();
		BufferedImage bimPage = new BufferedImage(ziImg.getWidth(null), 
				ziImg.getHeight(null), BufferedImage.TYPE_INT_RGB);
		bimPage.getGraphics().drawImage(ziImg, 0, 0, null);
				
		BufferedImage bim;
		for (int i=0; i<rows; i++) {
			for (int j=0; j<cols; j++) {
					//g.drawRect(100 + j*(150 + 3), 100 + i*(150+3), 150, 150);
				// first row on the first page lists column labels
				//if (page == 1 && i==0)
				//	continue;
				
				bim = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
				bim.getGraphics().drawImage(
						bimPage.getSubimage(x, y, size, size), 0, 0, null);
				/*
				JOptionPane.showConfirmDialog(null, "Stroke #" + (i*cols + j +1), 
						"Harvested Script Images", JOptionPane.OK_CANCEL_OPTION, 
						JOptionPane.INFORMATION_MESSAGE, new ImageIcon(bim));
				*/
				
				ziByStrokes.add(bim);
				
				x += size + gap;
			}
			x = 0;
			y += size + gap;
		}
		
		return ziByStrokes;
	}

	public static void main(String[] args) {
		String charImage = "images/LiveStrokes.png";
		Image ziImg = new ImageIcon(charImage).getImage();

		testModifyScript(ziImg);
	}

}
