

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Tile {
	
	public static int[] blank = {-1, -1};
	public static int[][] blank2 = {{-1, -1}}; 
	//background
	public static int[] grass = {0, 0};
	public static int[] road = {1, 0};
	public static int[] leaves = {2, 0};
	
	//collsion
	public static int[] wall = {0, 0};
	
	//itens
	public static int[] nothing = {0, 0};
	public static int[] pot = {1, 0};
	//characters
	public static int[][] Npc = {{0, 0}, {1, 0}, {2, 0}, {1, 0}};
	
	
	public static int size = 32;
	//Nome das imagens
	public static BufferedImage collision, terrain, background, itens, characters;
	
	public Tile(){
		try{
			Tile.background = ImageIO.read(new File("res/bg.png"));
			Tile.collision = ImageIO.read(new File("res/collision.png"));
			//Tile.terrain = ImageIO.read(new File("res/terrain.png"));
			Tile.itens = ImageIO.read(new File("res/itens.png"));
			Tile.characters = ImageIO.read(new File("res/chars.png"));
		}catch(Exception e){
			System.out.println("ERRO - carregar imagens -");
		}
	}
}
