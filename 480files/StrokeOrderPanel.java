package animazi;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

public class StrokeOrderPanel extends JComponent 
		implements MouseListener {
	String background = "images/bg_town.jpg";
	String teachImage = "images/xu_wei.png";
	String canvasImage = "images/canvas.png";
	//String charImage = "images/LiveStrokes.png";
	String charImage = "images/文Strokes.png";
	Image bkImg, thImg, cvImg, ziImg; 
	List<Image> ziByStrokes = new LinkedList<Image>();
	
	Image strokeImg;
	int strokeNo = 0;
	int strokeCount = 4;
	
	int canvasX = 150, canvasY = 200;
	int ziOffset = 25, ziSize = 150;
	private boolean initChar = true;
	private boolean playing = false;
	Timer t;
	
	StrokeOrderPanel() {
		bkImg = new ImageIcon(background).getImage();
		thImg = new ImageIcon(teachImage).getImage();
		cvImg = new ImageIcon(canvasImage).getImage();
		ziImg = new ImageIcon(charImage).getImage();
		this.setMaximumSize(new Dimension(1100, 650));
		this.setMinimumSize(new Dimension(1100, 650));
		this.setPreferredSize(new Dimension(1100, 650));
		//this.addMouseListener(this);
		
		ziByStrokes = StrokeImageParser.parseImage(ziImg);
		
        class TimerListener implements ActionListener {
            public void actionPerformed(ActionEvent evt) {
            	updateImage();
                //test.repaint();
            }
        }
        TimerListener tl = new TimerListener();
        int delay = 1000;
        t = new Timer(delay, tl);
        //t.start();
	}
	
	public void paintComponent(Graphics g) {
		g.drawImage(bkImg, 0, 0, null);
		g.drawImage(thImg, 450, 100, null);
		g.drawImage(cvImg, canvasX, canvasY, ziSize + 2*ziOffset, 
				ziSize + 2*ziOffset, null);
		
		if (initChar)
			strokeImg = ziByStrokes.get(0);
		g.drawImage(strokeImg, canvasX + ziOffset, canvasY + ziOffset, null);
		
		/*
		g.setColor(Color.PINK);
		for (int i=0; i<2; i++)
			for (int j=0; j<6; j++)
				g.drawRect(100 + j*(150 + 3), 100 + i*(150+3), 150, 150);
		//g.drawRect(100, 100, 150, 150);
		//g.drawRect(253, 100, 151, 151);
		 */
	}

	public void updateImage() {
		if (strokeNo < strokeCount) {
			strokeNo++;
		    System.out.println(strokeNo);
			//charImage = "images\\char1stroke" + strokeNo + ".png";
		    strokeImg = ziByStrokes.get(strokeNo);
		}
		else {
			strokeNo = 0;
			strokeImg = ziByStrokes.get(strokeNo);
		}
		//ziImg = new ImageIcon(charImage).getImage();
		//if (strokeNo == 6) resetImage();
		repaint();
	}
	
	/*public void resetImage() {
		strokeNo = 0;
		charImage = "images\\char1complete.png";
	}
	*/
	
	public static void main(String[] args) {
		JFrame test = new JFrame("Stroke Order Test");
		StrokeOrderPanel p = new StrokeOrderPanel();
		test.addMouseListener(p);
		test.add(p);
		test.pack();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		test.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (initChar) 
			initChar = false;

		if (playing) {
			playing = false;
			t.stop();
		}
		else {
			playing = true;
			t.start();
		}
			
		updateImage();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
