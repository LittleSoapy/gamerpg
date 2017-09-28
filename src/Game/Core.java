import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JPanel;




public class Core extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	
	public AtomicBoolean running = new AtomicBoolean(false), paused = new AtomicBoolean(false);
	
	private int fps = 0;
	private int frameCount = 0;
	
	private static JFrame frame;
	
	
	public static double oY = 0 * 32, oX = 0 * 32;
	public static double nY = 0 * 32, nX = 0 * 32;
	public static int dir = 0;
	
	public static boolean moving = false;
	public static boolean run = false;
	
	private Image screen;
	//public static Player player;
	//public static Npc npc;
	//final ArrayList<Entity> entities = new ArrayList<>();
	
	//public static Level level;

	public static Dimension screenSize = new Dimension(17 * 32, 15 * 32);
	public static Dimension pixel = new Dimension(screenSize.width, screenSize.height);
	public static Dimension Size;
	
	private static String name = "The Game";
	
	public Core(){
		setPreferredSize(screenSize);
	}
	
	public static void main(String[] args){
		Core core = new Core();
		
		frame = new JFrame();
		frame.add(core);
		
		Size = new Dimension(frame.getWidth(), frame.getHeight());
		
		frame.pack();
		frame.setTitle(name);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		//ff.setGamePanelKeyBindings(core, player);
		core.start();
		
	}
	


	public void start(){
		requestFocus();
		//define classes
		
		//npc = new Npc(new Rectangle(32, 32), Tile.blank2);
		//level = new Level(1);
		//player = new Player(new Rectangle(0, 0, 32, 32));
		//addEntity(player);
        //addEntity(npc);
        //new Tile();
		
		running.set(true);
		new Thread(this).start();
	}
	
	public void render(){
		Graphics g = screen.getGraphics();
		
		//level.render(g, (int)oX, (int)oY, (pixel.width / Tile.size) + 2, (pixel.height / Tile.size) + 2);
		//player.render(g);
		
		g.setColor(Color.red);
		g.drawString("X: " + (int)(oX / 32) + " Y: " + (int)(oY / 32), 10, 15);
		g.drawString("FPS: " + fps, 10, 30);
		
		g = this.getGraphics();
		g.drawImage(screen, 0, 0, screenSize.width, screenSize.height, 0, 0, pixel.width, pixel.height, null);
		g.dispose();
		frameCount++;
	}
	
	public void stop(){
		run = false;
	}
	
	public void run() {
		screen = createVolatileImage(pixel.width, pixel.height);
		
        //This value would probably be stored elsewhere.
        final double GAME_HERTZ = 60.0;
        //Calculate how many ns each frame should take for our target game hertz.
        final double TIME_BETWEEN_UPDATES = 1000000000 / GAME_HERTZ;
        //At the very most we will update the game this many times before a new render.
        //If you're worried about visual hitches more than perfect timing, set this to 1.
        final int MAX_UPDATES_BEFORE_RENDER = 5;
        //We will need the last update time.
        double lastUpdateTime = System.nanoTime();
        //Store the last time we rendered.
        double lastRenderTime = System.nanoTime();

        //If we are able to get as high as this FPS, don't render again.
        final double TARGET_FPS = 60;
        final double TARGET_TIME_BETWEEN_RENDERS = 1000000000 / TARGET_FPS;

        //Simple way of finding FPS.
        int lastSecondTime = (int) (lastUpdateTime / 1000000000);

		while(running.get()){
			//frame.pack();
			double now = System.nanoTime();
            int updateCount = 0;
            
            if (!paused.get()) {
                //Do as many game updates as we need to, potentially playing catchup.
                while (now - lastUpdateTime > TIME_BETWEEN_UPDATES && updateCount < MAX_UPDATES_BEFORE_RENDER) {
                	//updateGame();
                    lastUpdateTime += TIME_BETWEEN_UPDATES;
                    updateCount++;
                }

                //If for some reason an update takes forever, we don't want to do an insane number of catchups.
                //If you were doing some sort of game that needed to keep EXACT time, you would get rid of this.
                if (now - lastUpdateTime > TIME_BETWEEN_UPDATES) {
                    lastUpdateTime = now - TIME_BETWEEN_UPDATES;
                }
                
                render();
                
                lastRenderTime = now;

                //Update the frames we got.
                int thisSecond = (int) (lastUpdateTime / 1000000000);

                if (thisSecond > lastSecondTime) {
                    System.out.println("NEW SECOND " + thisSecond + " " + frameCount);
                    fps = frameCount;
                    frameCount = 0;
                    lastSecondTime = thisSecond;
                }

                //Yield until it has been at least the target time between renders. This saves the CPU from hogging.
                while (now - lastRenderTime < TARGET_TIME_BETWEEN_RENDERS && now - lastUpdateTime < TIME_BETWEEN_UPDATES) {
                    //allow the threading system to play threads that are waiting to run.
                    Thread.yield();

                    //This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
                    //You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
                    //FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
                    //On my OS it does not unpuase the game if i take this away
                    try {
                        Thread.sleep(1);
                    } catch (Exception e) {
                    	System.out.println("ERRO - thread sleep -");
                    }
                    now = System.nanoTime();
                }
            }
		}
		
	}
	
/*	private void updateGame() {
		player.move();
        level.tick();
        
	}
	public void addEntity(Entity e) {
        entities.add(e);
    }*/
	
}
