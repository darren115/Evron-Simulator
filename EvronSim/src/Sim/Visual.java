package Sim;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;

import recipes.Drawable;

public class Visual implements Runnable{
	
	private JFrame frame;
	private Canvas canvas;
	
	private int WIDTH = 800;
	private int HEIGHT = 600;
	
	private int UPS = 60;
	private int FPS = 60;
	
	private boolean running = true;
	
	private ArrayList<Drawable> drawables;
	

	public Visual(Drawable ...drawables ) {
		
		this.drawables = new ArrayList<>();
		this.drawables.addAll(Arrays.asList(drawables));
		
		frame = new JFrame();
		canvas = new Canvas();
		canvas.setFocusable(false);
		canvas.setBounds(new Rectangle(WIDTH, HEIGHT));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}
	
	
	
	@Override
	public void run() {

	long initialTime = System.nanoTime();
	final double timeU = 1000000000 / UPS;
	final double timeF = 1000000000 / FPS;
	double deltaU = 0, deltaF = 0;
	int frames = 0, ticks = 0;
	long timer = System.currentTimeMillis();

	    while (running) {

	        long currentTime = System.nanoTime();
	        deltaU += (currentTime - initialTime) / timeU;
	        deltaF += (currentTime - initialTime) / timeF;
	        initialTime = currentTime;

	        if (deltaU >= 1) {
	            ticks++;
	            deltaU--;
	        }

	        if (deltaF >= 1) {
	            draw();
	            frames++;
	            deltaF--;
	        }

	        if (System.currentTimeMillis() - timer > 1000) {
//	            if (RENDER_TIME) {
//	                System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
//	            }
	            frames = 0;
	            ticks = 0;
	            timer += 1000;
	        }
	    }
	}
	
	
	private void draw() {
		BufferStrategy bs = canvas.getBufferStrategy();
		if (bs == null) {
			canvas.createBufferStrategy(2);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		g.setColor(new Color(230, 230, 230));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		for(Drawable dr: drawables) {
			dr.draw(g);
		}		
		
		g.dispose();
		bs.show();
	}
	
	
}
