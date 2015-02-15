package com.frost.dataspace;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.frost.dataspace.graphics.Screen;


public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static int width = 900;
	public static int height = width / 16 * 9;
	public static String title="JonnySpace";

	boolean running = false;
	private JFrame frame;
	private Thread thread;
	Screen screen;
	private BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	private int [] pixels=((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	public Game() {
		Dimension size = new Dimension(width, height);
		this.setPreferredSize(size);
		screen=new Screen(width,height);
		frame = new JFrame();
		
		

	}
  
	public synchronized void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();

	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
		}

	}

	public void run() {
		long timeBefore = System.nanoTime();
		long timer=System.currentTimeMillis();
		double ups = 60.0;
		final double ns= 1000000000.0/ups;
		double delta = 0.0;
		int frames=0;
		int updates=0;
		requestFocus();
		while (running) {
		

			long timeNow = System.nanoTime();
			delta += (timeNow - timeBefore)/ ns;
			timeBefore=timeNow;

			while (delta >= 1) {
				update();
				updates++;
				delta--;
			}
			render();
			frames++;
			if (System.currentTimeMillis()-timer>1000){
				timer+=1000;
				frame.setTitle(title+"  |  "+updates+"ups ,"+ frames +"fps");
				updates=0;
				frames=0;
			}
		}
		stop();
	}
	public void update(){
		
		
	}
	public void render(){
		BufferStrategy bs=getBufferStrategy();
		if( bs==null){
			createBufferStrategy(3);
			return;
		}
		screen.clear();
		screen.render();
		for (int i=0;i<pixels.length;i++){
			pixels[i]=screen.pixels[i];
		}
		
		Graphics g=bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(image, 0, 0, getWidth(), getHeight(),null);
		g.dispose();
		bs.show();
		
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.setTitle(title);
		game.frame.add(game);
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.start();
		game.frame.setVisible(true);

	}

}