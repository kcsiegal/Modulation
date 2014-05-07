package finalgame;

import jgame.JGColor;
import jgame.JGObject;
import jgame.JGPoint;
import jgame.platform.JGEngine;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;


public class PlayLevels extends JGEngine implements MouseListener
{
	public int levelNum;
	public Player p;
	public Enemy e;
	public String[] tilesArray;
	public ArrayList<Enemy> enemies;
	public String portalOneType = "";
	public String portalTwoType = "";
	public int offset = 24;
	public PlayLevels(int level) 
	{ 
		levelNum = level;
		initEngine(480,500);
		initCanvas();
	}
	
	public void initCanvas() 
	{
		 setCanvasSettings(10,11,48,48,null,JGColor.black,null);
	}
	
	public int[][][] loadLevel(int levelNum)
	{
		try
		{
			Scanner s = new Scanner(new File("levels/"+Integer.toString(levelNum)+".lvl"));
			tilesArray = new String[10];
			for (int i = 0; i<10; i++)
			{
				tilesArray[i] = s.nextLine();
			}
			s.close();
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		// SEARCH THROUGH tilesArray for unique characters : P/E/# to place unique token objects (player, enemies, goal). 
		// Additionally, we need to remove all unique characters with a '.' so that a blank tile is still placed at the location of these tokens.
		int numOfEnemies = 0;
		for(int i = 0; i < tilesArray.length; i++)
		{
			for (int numChars = 0; numChars < 10; numChars++)
			{
				if (tilesArray[i].charAt(numChars) == new Character('E'))
				{
					numOfEnemies++;
				}
			}
			
		}

		int[][][] objectLocs = new int[4][numOfEnemies][2];
		/* T N L
		 * Y U O
		 * P M C
		 * E B
		 *   E
		 *   R <- Starts from 0
		 */
		
		//find object locations. Replace objects' loc with "."
		int numEnemiesLocated = 0;
		for(int i = 0; i < tilesArray.length; i++)
		{
			//Loop through each character in each tilesArray string element
			for (int numChars = 0; numChars < 10; numChars++)
			{
				int currentPIndex = tilesArray[i].indexOf("P");
				if (currentPIndex != -1)
				{
					objectLocs[0][0][0] = currentPIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
					objectLocs[0][0][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
					tilesArray[i] = tilesArray[i].replaceFirst("P", "."); 
				}			
				
				int currentEIndex = tilesArray[i].indexOf("E");
				if (currentEIndex != -1)
				{
					objectLocs[1][numEnemiesLocated][0] = currentEIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
					objectLocs[1][numEnemiesLocated][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
					tilesArray[i] = tilesArray[i].replaceFirst("E", ".");
					numEnemiesLocated++;
				}
				
				int currentVIndex = tilesArray[i].indexOf("D");
				if (currentVIndex != -1)
				{
					objectLocs[2][0][0] = currentVIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
					objectLocs[2][0][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
					tilesArray[i] = tilesArray[i].replaceFirst("D", "."); 
				}
				
				//check Portals
                int currentPoIndex = tilesArray[i].indexOf("^");
                if (currentPoIndex != -1 && objectLocs[3].equals(null))
                {
                        objectLocs[3][0][0] = currentPoIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        objectLocs[3][0][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        tilesArray[i] = tilesArray[i].replace("^", ".");
                        portalOneType = "Up;";
                }
                else
                {
                	if (currentPoIndex != -1)
                	{
                		objectLocs[3][1][0] = currentPoIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        objectLocs[3][1][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        tilesArray[i] = tilesArray[i].replace("^", ".");
                        portalTwoType = "Up";
                	}
                }
                
                currentPoIndex = tilesArray[i].indexOf("v");
                if (currentPoIndex != -1 && portalOneType == "")
                {
                        objectLocs[3][0][0] = currentPoIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        objectLocs[3][0][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        tilesArray[i] = tilesArray[i].replace("v", ".");
                        portalOneType = "Down";
                }
                else
                {
	            	if (currentPoIndex != -1)
	            	{
	            		objectLocs[3][1][0] = currentPoIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
	                    objectLocs[3][1][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
	                    tilesArray[i] = tilesArray[i].replace("v", ".");
	                    portalTwoType = "Down";
	            	}
                }
                
                currentPoIndex = tilesArray[i].indexOf(">");
                if (currentPoIndex != -1 && portalOneType == "")
                {
                        objectLocs[3][0][0] = currentPoIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        objectLocs[3][0][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        tilesArray[i] = tilesArray[i].replace(">", ".");
                        portalOneType = "Right";
                }
                else
                {
	            	if (currentPoIndex != -1 && portalOneType == "")
	            	{
	            		objectLocs[3][1][0] = currentPoIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
	                    objectLocs[3][1][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
	                    tilesArray[i] = tilesArray[i].replace(">", ".");
	                    portalTwoType = "Right";
	            	}
                }
                
                
                currentPoIndex = tilesArray[i].indexOf("<");
                if (currentPoIndex != -1 && portalOneType == "")
                {
                        objectLocs[3][0][0] = currentPoIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        objectLocs[3][0][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
                        tilesArray[i] = tilesArray[i].replace("<", ".");
                        portalOneType = "Left";
                }
                else
                {
	            	if (currentPoIndex != -1)
	            	{
	            		objectLocs[3][1][0] = currentPoIndex + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
	                    objectLocs[3][1][1] = i + 1; //Add one to account for starting at 0 (our tile grid beings at (1,1)
	                    tilesArray[i] = tilesArray[i].replace("<", ".");
	                    portalTwoType = "Left";
	            	}
                }
			}
			
		}

		return objectLocs; //slot 0 is player info, slot 1 is enemy info, slot 2 is victory square info.
	}
	
	public void initGame() 
	{
		setFrameRate(20,1);
		defineMedia("game.tbl");
		
		int[][][] objectLocs = loadLevel(levelNum);
		int[] playerLoc = objectLocs[0][0];
		int[][] enemyLocs = objectLocs[1];
		int[] exitLoc = objectLocs[2][0];
		int[][] portalLoc = objectLocs[3];
		
		p = new Player(playerLoc[0],playerLoc[1]);
		enemies = new ArrayList<Enemy>();
		
		for (int i = 0; i < enemyLocs.length; i++)
		{
			enemies.add(new Enemy(enemyLocs[i][0],enemyLocs[i][1]));
		}
		
		
		VictorySquare exitDoor = new VictorySquare(exitLoc[0],exitLoc[1]);
		if (portalOneType != "")
		{
			Portal a = new Portal(portalLoc[0][0],portalLoc[0][1],portalLoc[1][0],portalLoc[1][1],portalTwoType,portalOneType);
			Portal b = new Portal(portalLoc[1][0],portalLoc[1][1],portalLoc[0][0],portalLoc[0][1],portalOneType,portalTwoType);
		}
		setTiles(0,0,tilesArray);
		
		setTileSettings("|",1,0);
	}

	public void doFrame()
	{
		//This is a bizarre solution to segmented moving. It might be the case that later on, we might need to change this implementation around.**
		while (getKey(KeyUp) || getKey(KeyDown) || getKey(KeyRight) || getKey(KeyLeft))
		{			
			delay(.05);
			moveObjects(null,0);
		}
		
		/* For collision reference:
		 * 0 - anything
		 * 1 - walls
		 * 2 - player
		 * 5 - enemy(-ies)
		 * 8 - victory square
		 * Collision detection in JGame is very poorly documented. It appears as though the collision ids specified in the .tbl file don't matter. 
		 * The ids seem to need to be far apart too, possibly because of a poorly implmented attempt to use multiple objects in one check call by 
		 * JGame (their documentation advises using checkCollision(2+3,3) for example...?).
		 */
		checkBGCollision(1,5);
		checkBGCollision(1,2);
		
		
		checkCollision(2,8);
		checkCollision(2,3);
		checkCollision(5,5);
		checkCollision(8,5);
		
		
		
		if (! p.hasMoved()) // This loop/conditional checks to see if the player has actually moved prior to telling the enemies need to move in response.
		{
			for (int i = 0; i < enemies.size(); i++)
			{
				enemies.get(i).moveBackFromWhenstThouCamest();
			}
		}
	}
	
	public class VictorySquare extends JGObject
	{
        VictorySquare(int x_tile,int y_tile)
        {
            super("victory",true,((double)x_tile)*48 - 48,((double)y_tile)*48 - 48,8,"victory",0,0);
	    }
	    
	    public void hit(JGObject obj)
	    {
	        System.out.println("VICTORY!!!!!!!!!");
	    }
	}
	
	
	public abstract class Token extends JGObject
	{
		public double oldX;
		public double oldY;
		
		public Token(int x_tile,int y_tile,String type,int collisionID)
		{
			super(type,true,((double)x_tile)*48 - 48,((double)y_tile)*48 - 48,collisionID,type,0,0);
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
			super(x_tile,y_tile,"player",2);
			
		}
		
		public void move()
		{
			//KEY HAS BEEN PRESSED BY THE TIME WE'RE IN THIS METHOD
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
			super(x_tile,y_tile,"enemy",5);
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
		
		public void hit (JGObject obj)
		{
		    Class<? extends JGObject> typeHit = obj.getClass();
			if (typeHit == Player.class || typeHit == VictorySquare.class)
		    {
		    	System.out.println("CHOMP.");
		    }
		    else
		    {
		    	if (typeHit == Enemy.class)
		    	{
					moveBackFromWhenstThouCamest();
		    	}
		    }
		}
	
	}
	
	public class Portal extends JGObject
    {
        private int bx_tile;
        private int by_tile;
        private String portDirection;
	    Portal(int x_tile,int y_tile,int b_x_tile,int b_y_tile,String direction,String graphic)
	    {
	    	super("portal",true,((double)x_tile)*48 - 48,((double)y_tile)*48 - 48,11,"portal" + graphic.charAt(0),0,0);
	        bx_tile = b_x_tile;
	        by_tile = b_y_tile;
	        portDirection = direction;
	    }
	    
        public void hit(JGObject obj)
        {
        	System.out.println("COLLISION WITH PORTAL");
        	System.out.println(obj.toString());
        	
        	if (portDirection == "Up")
        	{
        		obj.setPos(bx_tile*48-48, (by_tile - 1)*48-48);
        	}
        	if (portDirection == "Down")
        	{
        		obj.setPos(bx_tile*48-48, (by_tile + 1)*48-48);
        	}
        	if (portDirection == "Right")
        	{
        		obj.setPos((bx_tile + 1 )*48-48, (by_tile)*48-48);
        	}
        	if (portDirection == "Left")
        	{
        		obj.setPos((bx_tile - 1)*48-48, (by_tile)*48-48);
        	}
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
		PlayLevels pl = new PlayLevels(1);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		
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
	
	public Boolean isMenuButton(MouseEvent e)
	{
		if (e.getX() >= 162 && e.getX() <= 310 && e.getY() >= 480 + offset)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Boolean isResetButton(MouseEvent e)
	{
		if (e.getX() >= 155 && e.getX() <= 312 && e.getY() >= 480 + offset)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	

}
