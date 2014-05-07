//TESTING GAME STATES
/**
 * 
 */
package finalgame;

import jgame.JGColor;
import jgame.JGObject;
import jgame.JGPoint;
import jgame.platform.JGEngine;

public class Game extends JGEngine
{
	public Player p;
	public Enemy e;
	
	public Game(JGPoint size) 
	{ 
		initEngine(size.x,size.y); 
	}
	
	public void initCanvas() 
	{
		 setCanvasSettings(10,10,48,48,null,null,null);
	}

	public void initGame() 
	{
		setGameState("Menu");
	}

    public void startMenu() {
        removeObjects(null,0);
        setCanvasSettings(10,10,48,48,JGColor.green,JGColor.black,null);
    }
    
    public void paintFrameMenu() {
        drawString("Welcome to Modulation!",pfWidth()/2,120,0);
        drawString("Press Enter to begin the game",pfWidth()/2,180,0);
    }
    
    public void doFrameMenu() {
        if (getKey(KeyEnter)) {
            System.out.println("Yay");
            clearKey(KeyEnter);
            addGameState("InGame");
            removeGameState("Menu");
        }
    }
    
    
    public void startInGame() {
        setCanvasSettings(10,10,48,48,null,JGColor.white,null);
        setFrameRate(20,1);
		defineMedia("game.tbl");
		p = new Player(3,3);
		e = new Enemy(8,7);
		
		
		
    }
    
    public void paintFrameInGame() {
        
        setTiles(0,0,new String[] 
		       { 
				"||||||||||",
				"|........|",
				"|....|...|",
				"|........|",
				"|...|....|",
				"|........|",
				"|........|",
				"|........|",
				"|.|...|..|",
				"||||||||||",
		       });
		
		setTileSettings("|",2,0);
		setTileSettings("#",2,0);
		drawString("Press Enter to return to the Menu",pfWidth()/2,450,0);
    }

	public void doFrameInGame()
	{
		//This is a bizarre solution to segmented moving. It might be the case that later on, we might need to change this implementation around.**
		while (getKey(KeyUp) || getKey(KeyDown) || getKey(KeyRight) || getKey(KeyLeft))
		{			
			delay(.05);
			moveObjects(null,0);
		}
		
		checkBGCollision(2,1);
		if (! p.hasMoved())
		{
			e.moveBackFromWhenstThouCamest();
		}
		if (getKey(KeyEnter)) {
		    System.out.println("Woot");
		    clearKey(KeyEnter);
		    addGameState("Menu");
		    removeGameState("InGame");
		}
	}
	
	public abstract class Token extends JGObject
	{
		public double oldX;
		public double oldY;
		
		public Token(int x_tile,int y_tile,String type)
		{
			super("player",true,((double)x_tile)*48 - 48,((double)y_tile)*48 - 48,1,type,0,0);
		}
		
		protected void moveBackFromWhenstThouCamest()
		{
			this.setPos(oldX, oldY);
		}
		
		public abstract void move();
		public abstract void moveHelper(int lastKey);
		
		
	}
	
	public class Player extends Token
	{
		public Boolean playerHasMoved = true;
		
		public Player(int x_tile,int y_tile)
		{
			super(x_tile,y_tile,"player");
			
		}
		
		public void move()
		{
			//KEY HAS BEEN PRESSED
			if (!getKey(KeyUp) && !getKey(KeyDown) && !getKey(KeyRight) && !getKey(KeyLeft))
			{
				moveHelper(getLastKey());
				playerHasMoved = true;
			}
			
		}
		
		public void moveHelper(int lastKey)
		{
			setDir(0,0);
			oldX = this.x;
			oldY = this.y;
			if (lastKey == KeyLeft) 
			{
				this.setPos(this.x  - 48, this.y);
			}
			else 
			{
				if (lastKey == KeyRight)  
				{
					this.setPos(this.x + 48, this.y);
				}
				else 
				{
					if (lastKey == KeyDown) 	
					{
						this.setPos(this.x, this.y + 48);
					}
					else 
					{
						if (lastKey == KeyUp) 
						{
							this.setPos(this.x, this.y - 48);
						}
					}
				}
			}
		}
		
		public void hit_bg(int tilecid)
		{
		    System.out.println("We hit something");
			moveBackFromWhenstThouCamest();
			playerHasMoved = false;
		}
		
		public Boolean hasMoved()
		{
			return playerHasMoved;
		}

	}
	
	public class Enemy extends Token
	{
		
		public Enemy(int x_tile,int y_tile)
		{
			super(x_tile,y_tile,"enemy");
		}
		
		public void move()
		{
			//KEY HAS BEEN PRESSED BY THE TIME WE'RE IN THIS METHOD
			if (!getKey(KeyUp) && !getKey(KeyDown) && !getKey(KeyRight) && !getKey(KeyLeft))
			{
				moveHelper(getLastKey());
			}
			
		}
		
		public void moveHelper(int lastKey)
		{
			setDir(0,0);
			oldX = this.x;
			oldY = this.y;
			if (lastKey == KeyLeft) 
			{
				this.setPos(this.x  - 48, this.y);
			}
			else 
			{
				if (lastKey == KeyRight)  
				{
					this.setPos(this.x + 48, this.y);
				}
				else 
				{
					if (lastKey == KeyDown) 	
					{
						this.setPos(this.x, this.y + 48);
					}
					else 
					{
						if (lastKey == KeyUp) 
						{
							this.setPos(this.x, this.y - 48);
						}
					}
				}
			}
		}
		
		public void hit_bg(int tilecid)
		{
			moveBackFromWhenstThouCamest();
		}
	
	}
	
	public static void delay(double seconds)
	{
		try
		{
			Thread.sleep((int)(seconds*1000));
		}
		catch (InterruptedException e)
		{
			System.out.println("Error");
		}
	}
	
	public static void main(String[] args) 
	{
		new Game(new JGPoint(480,480));
	}	

}
