package oracle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class ThinningTest {

	private BufferedImage ziImage;
	private ZiWithCenterLinePanel ziPanel;
	private int[][] bw;
	
	private void test() {
	    try
	    {
	        //colored image path
	        //ziImage = ImageIO.read(new File("ZImage/YouKaishu.png"));
	        ziImage = ImageIO.read(new File("ZImage/quanJGW.png"));
	        //ziImage = ImageIO.read(new File("ZImage/BaoJGW.png"));
	        //ziImage = ImageIO.read(new File("ZImage/HuoJGWen.png"));
	        //ziImage = ImageIO.read(new File("ZImage/TuJGW.png"));
	        //getting width and height of image
	        int image_width = ziImage.getWidth();
	        int image_height = ziImage.getHeight();
	        BufferedImage img = ziImage;

	        //drawing a new image
	        BufferedImage bimg = new BufferedImage((int)image_width, (int)image_height, BufferedImage.TYPE_BYTE_GRAY);
	        Graphics2D gg = bimg.createGraphics();
	        gg.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);

	        //saving black and white image onto drive
	        String temp = "quanJGWthinBW.png";
	        File fi = new File("ZImage/" + temp);
	        ImageIO.write(bimg, "png", fi);

        	System.out.println(image_width + "x" + image_height);

	        //color keys: black = 1, white = 0
	        bw = new int[image_height][image_width];
	        int rgb;
	        int bOrW;
	        for (int x = 0; x < bw.length; x++) {
	        	System.out.print(((x<10)?" ":"") + x + ":");
		        for (int y = 0; y < bw[0].length; y++) {
		        	rgb = bimg.getRGB(y, x);
		        	bw[x][y] = (rgb==-1)?0:1;
		        	System.out.print(bw[x][y]);
		        }
	        	System.out.println();
	        }

	        int[][] bwThin = new int[image_width][image_height];
	        bwThin = new ThinningService().doZhangSuenThinning(bw, false);
	        //printImage(bwThin);
	        /*
	        //thinning by resizing gray scale image to desired eight and width
	        BufferedImage bimg2 = new BufferedImage((int)image_width, (int)image_height, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2 = bimg2.createGraphics();

	        // Perform your drawing here
	        g2.setColor(Color.BLACK);
	        g2.drawLine(0, 0, 200, 200);

	       //saving thinned image onto drive
	       String temp2 = "quanJGWthin.png";
	       File fi2 = new File("ZImage/" + temp2);
	       ImageIO.write(bimg2, "png", fi2);
	       */
	       //g2.dispose();
	    }
	    catch (Exception e)
	    {
	        System.out.println(e);
	    }       
	 }
	
	private void showZiWithCenterLine() {
		ZiWithCenterLinePanel ziPanel = new ZiWithCenterLinePanel(ziImage);
		
		ziPanel.setCenterLine(bw);
		
		ziPanel.drawCenterLine();
		
		JFrame f = new JFrame("Thinning Result");
		f.add(ziPanel);
		f.pack();
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) 
	{
		ThinningTest test = new ThinningTest();
		test.test();
		test.showZiWithCenterLine();
	}
}
