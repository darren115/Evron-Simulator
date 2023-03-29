package equipment;

import java.awt.Color;
import java.awt.Graphics;

import recipes.Drawable;

public class Bowl implements Drawable{

	private volatile boolean isEmpty = true;
	private boolean hasWater = false;
	private boolean hasWetMix = false;
	private boolean hasDryMix = false;
	private volatile boolean finishedMixing = false;
	private volatile boolean isMixing = false;
	private int id = 0;
	
	
	private int HEIGHT = 50;
	private int WIDTH = 40;
	private int x = 360;
	private int y = 100;

	public Bowl(int id) {
		this.id = id;
		this.x += id*80;
	}


	public synchronized void setEmpty() {
		System.out.println("----------------Emptied bowls");
		hasDryMix = false;
		hasWetMix = false;
		hasWater = false;
		isMixing = false;
		finishedMixing = false;
	}

	public synchronized boolean isHasDryMix() {
		return hasDryMix;
	}

	public synchronized void setHasDryMix() {

		hasDryMix = true;
	}

	public synchronized boolean isMixing() {

		return isMixing;
	}

	public void setMixing() {
//		System.out.println("isMixing" +id);
		isMixing = true;
	}

	public boolean isHasWater() {

		return hasWater;
	}

	public void setHasWater() {
		//System.out.println("has water " + id);
		this.hasWater = true;
	}

	public boolean isHasWetMix() {
		return hasWetMix;
	}

	public void setHasWetMix() {
		//System.out.println("has wet mix " + id);
		this.hasWetMix = true;
	}

	public boolean isFinishedMixing() {
		return finishedMixing;
	}

	public void setFinishedMixing() {
		finishedMixing = true;
	}

	public int getId() {
		return id;
	}


	@Override
	public void draw(Graphics g) {
		
		
		g.setColor(Color.gray);
		g.drawLine(x, y, x, y+HEIGHT);
		g.drawLine(x, y+HEIGHT, x+WIDTH, y+HEIGHT);
		g.drawLine(x+WIDTH, y, x+WIDTH, y+HEIGHT);
		
		g.setColor(Color.blue);
		if(isHasDryMix()) {
			g.fillRect(x+1, y+5, WIDTH-1, HEIGHT-1);
		}
//		hasDryMix = false;
//		hasWetMix = false;
//		hasWater = false;
//		isMixing = false;
		g.setColor(Color.black);
		g.drawString(String.valueOf(hasDryMix), x, y);
		g.drawString(String.valueOf(hasWetMix), x, y+10);
		g.drawString(String.valueOf(hasWater), x, y+20);
		g.drawString(String.valueOf(isMixing), x, y+30);
		g.drawString(String.valueOf(finishedMixing), x, y+40);
	}

}
