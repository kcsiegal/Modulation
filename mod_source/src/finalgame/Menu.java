package finalgame;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Menu implements MouseListener
{
	public int offset = 24; 
	//offset is to account for funky image measurement criteria (seems to differ between photshop and eclipse/swing)
	JFrame f;
	
	public Menu()
	{
		f = new JFrame("Modulation");
		BGPanel bg = new BGPanel();
		f.setSize(480,500);
		f.setResizable(false);
		f.add(bg);
		f.setVisible(true);
		f.addMouseListener(this);
	}

	public class BGPanel extends JPanel
	{
		private BufferedImage image;
		public BGPanel()
		{
			try 
			{
				image = ImageIO.read(new File("menuScreen.png"));
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public void paintComponent(Graphics g)
		{
			g.drawImage(image, 0, 0, null);
		}
	}
	
	public static void main(String[] args) 
	{
		new Menu();
		//System.out.println("HERRO");
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		// TODO Auto-generated method stub

		
		if (isPlayButton(e))
		{
			PlayLevels pl = new PlayLevels(1);
			f.setVisible(false);
		}
		if (isLevelButton(e))
		{
			System.out.println("Level");
		}
		if (isInstrButton(e))
		{
			System.out.println("Instructions");
		}
		if (isQuitButton(e))
		{
			System.exit(0);
		}
		
	}
	
	public Boolean isPlayButton(MouseEvent e)
	{
		if (e.getX() >= 162 && e.getX() <= 310 && e.getY() >= 328 + offset && e.getY() <= 353 + offset)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Boolean isLevelButton(MouseEvent e)
	{
		if (e.getX() >= 155 && e.getX() <= 312 && e.getY() >= 358 + offset && e.getY() <= 383 + offset)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Boolean isInstrButton(MouseEvent e)
	{
		if (e.getX() >= 153 && e.getX() <= 319 && e.getY() >= 388 + offset && e.getY() <= 412 + offset)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Boolean isQuitButton(MouseEvent e)
	{
		if (e.getX() >= 197 && e.getX() <= 275 && e.getY() >= 418 + offset && e.getY() <= 443 + offset)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}

