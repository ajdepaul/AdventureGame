package game.graphics.screens;

import game.Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A scene that stops the game and displays information
 * 
 * @author Anthony DePaul
 */
public class Scene {

	/** The game the scene is in */
	private Game game;
	/** The text to be displayed during the scene */
	public ArrayList<String> text = new ArrayList<String>();
	/** Which segment of text should be displayed */
	public int textCounter = 0;
	/** If the scene is not finished */
	public boolean playing = true;

	/**
	 * A scene that stops the game and displays information
	 * 
	 * @param text
	 * text used for the scene
	 */
	public Scene(Game g, String... t) {
		game = g;
		text = new ArrayList<String>(Arrays.asList(t));
	}

	public Scene(Game g, String name, boolean x) {
		game = g;
		createTextFromFile(name);
	}

	/**
	 * Creates the String[] text from a txt file. (I did not write this: https://www.caveofprogramming.com/java/java-file-reading-and-writing-files-in-java.html)
	 * 
	 * @param path
	 * the location of the txt file
	 */
	public void createTextFromFile(String path) {
		// find the file
		File f = null;
		try {
			f = new File(Game.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		String finalpath = f.getParent() + path;

		// sets lines
		String currentLine, lineToAdd = "";

		try {
			FileReader fileReader = new FileReader(finalpath);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((currentLine = bufferedReader.readLine()) != null) {
				if (!currentLine.equals("")) {
					lineToAdd += currentLine + "\n";
				} else {
					text.add(lineToAdd);
					lineToAdd = "";
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		text.add(lineToAdd);
	}

	/**
	 * Render the currently playing scene.
	 * 
	 * @param g
	 * graphics object used to draw
	 */
	public void render(Graphics g) {
		Color tempColor = g.getColor();
		Font tempFont = g.getFont();

		g.drawImage(game.images.sceneBackground, 0, 0, null);

		g.setColor(Color.black);
		g.setFont(new Font("Monospaced", Font.BOLD, 40));
		drawText(g, text.get(textCounter), 40, 490);

		g.setColor(tempColor);
		g.setFont(tempFont);
	}

	/**
	 * Draws a string while creating new lines with \n
	 * 
	 * @param g
	 * the graphics object used to draw
	 * @param text
	 * text to draw
	 * @param x
	 * x location of drawn text
	 * @param y
	 * y location of drawn text
	 */
	private void drawText(Graphics g, String text, int x, int y) {
		for (String line : text.split("\n"))
			g.drawString(line, x, y += g.getFontMetrics().getHeight());
	}
}
