package com.frost.dataspace;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;


public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	public static int width = 900;
	public static int height = width / 16 * 9;
	public static String title="JonnySpace";

	boolean running = false;
	private JFrame frame;
	private Thread thread;
	public Game() {//okkkjgfgftyfhfghfg
		Dimension size = new Dimension(width, height);
		this.setPreferredSize(size);
		frame = new JFrame();
//TEST
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
		
		
		Graphics g=bs.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, width, height);
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