package parser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import dao.SqlServerDbAccessor;

public class ParserTest extends JPanel {
	private Image page;
	int width, height;
	
	private SqlServerDbAccessor dao = new SqlServerDbAccessor();
	
	public ParserTest(String file) {
		dao.connectToDb();

		page = new ImageIcon(file).getImage();
		width = page.getWidth(null);
		height = page.getHeight(null);
		System.out.println(width);
		System.out.println(height);
		Dimension dim = new Dimension(1240, 2000);
		this.setMaximumSize(dim);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(page, 0, 0, 1240, 2000, 230, 100, 1470, 2100, null);
		g.setColor(Color.RED);
		//int x = 126, size = 44;
		int x = 206, size = 44;
		double y = 10;
		double hh = 609/12.;
		for (int i=0; i<39; i++) {
			//g.drawRect(120, 10, 45, 45);
			g.drawRect(x, (int)y, size, size);
			y += hh;
		}
	}

	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setTitle("Parser Test");
		ParserTest test = new ParserTest(
				"image/OracleBS-CNF first 11 pages_Page_01.jpg");	
        JScrollPane scrollPane = new JScrollPane(test);

        // now add the scrollpane to the jframe's content pane, specifically
        // placing it in the center of the jframe's borderlayout
        //JFrame frame = new JFrame("JScrollPane Test");
        window.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		//window.add(test);
		window.pack();
		window.setVisible(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//test.browseSripts(test);
		
		test.enterScripts(test);
	}

	private void enterScripts(ParserTest test) {
		// TODO Auto-generated method stub
		String[] chnChars = {"人", "匕", "妣", "尸", "夷", "", "勹", "氏", "", "介"};
		String[] pinyin = {"rén", "bǐ", "bǐ", "	shī", "yí", "", "fú", "shì", 
				"", "jiè"};
		String[] translation = {"man; people; mankind; someone else",
				"spoon, ladle; knife, dirk", "one's descensed mother",
				"corpse; to impersonate the dead; to preside; KangXi radical 44",
				"ancient barbarian tribes", "", "	wrap; KangXi radical 20",
				"clan, family; mister", "", 
				"forerunner, herald, harbinger; to lie between; sea shell; to wear armor"};
		int[] charIds = {1, 8, 19, 20, 22, 23, 24, 29, 30, 32};
		String buShou = chnChars[0];
		ArrayList<Image> scripts = browseSripts(test);
		
		
		for (int charSeqNo = 0; charSeqNo<charIds.length; charSeqNo++) {
			int i = charIds[charSeqNo];
			if (i == 1)
				insertSectHdr(i, scripts.get(i), chnChars[0]);
			
			insertChar(charSeqNo+1, scripts.get(i), chnChars[charSeqNo], 
					pinyin[charSeqNo], translation[charSeqNo], 1);
		}
	}

	private void insertChar(int charId, Image image, String chnChar, 
			String pinyin, String translation, int bushouId) {
		// TODO Auto-generated method stub
		String charStr = nullableInput(chnChar), 
				pinyinStr = nullableInput(pinyin), 
				tranStr = nullableInput(translation); 

		String query = "INSERT INTO Character(CharNo, DefaultScript, ChnChar, "
				+ "PhoneticNotation, EnglishTranslation, RadicalID) VALUES (" 
				+ charId + "," + " image-" + charId + ", " + charStr + ", "
				+ pinyinStr + ", " + tranStr + ", " + bushouId + ")";
		JOptionPane.showConfirmDialog(null, query, 
				"Insert ChnChar " + chnChar, JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(image));	
	}

	private String nullableInput(String chnChar) {
		String retStr = "null";
		if (chnChar.length() > 0)
			retStr = "'" + chnChar + "'";
		return retStr;
	}

	private void insertSectHdr(int i, Image image, String string) {
		String query = "INSERT INTO SectionHeader VALUES (" + i + ","
				+ " image-1, " + "'" + string + "')";
		dao.prepareInsertSectHdrStmt();
		if (dao.insertIntoSectionHeader(i, image, string))
			JOptionPane.showConfirmDialog(null, query, 
				"Insert BuShou"+i, JOptionPane.OK_CANCEL_OPTION, 
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(image));
	}

	private ArrayList<Image> browseSripts(ParserTest test) {
		// create a modifiable copy of the page image
		BufferedImage bimPage = new BufferedImage(1240, 2000, 
				BufferedImage.TYPE_BYTE_GRAY);
		bimPage.getGraphics().drawImage(test.page, 0, 0, null);
		
		// store harvested images with an ArrayList
		ArrayList<Image> scripts = new ArrayList<Image>();
		int x = 436, size = 44;
		double y = 110;
		double hh = 609/12.;
		BufferedImage bim;
		int page = 1;
		for (int i=0; i<35; i++) {
			System.out.println(i);
			// first row on the first page lists column labels
			//if (page == 1 && i==0)
			//	continue;
			
			bim = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY);
			bim.getGraphics().drawImage(
					bimPage.getSubimage(x, (int) y, size, size), 0, 0, null);
			//JOptionPane.showConfirmDialog(null, "Script #" + (i+1), 
			//		"Harvested Script Images", JOptionPane.OK_CANCEL_OPTION, 
			//		JOptionPane.INFORMATION_MESSAGE, new ImageIcon(bim));
			
			scripts.add(bim);
			y += hh;
		}
		
		return(scripts);
	}
}
