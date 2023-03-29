package equipment;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.concurrent.ConcurrentLinkedQueue;

import recipes.Drawable;

public class Vemag implements Runnable, Drawable {

	// private int clip;
	private int speed;
	private int currentVolume = 0;
	private int unitSize;
	private boolean switchedOn = true;

	ConcurrentLinkedQueue<String> previousStatus;

	// Rendering info
	private String status = "";
	private int x = 30;
	private int y = 30;

	private Object lock = new Object();

	public Vemag(int speed, int unitSize) {
		this.speed = speed;
		this.unitSize = unitSize;
		switchedOn = true;
		previousStatus = new ConcurrentLinkedQueue<>();
		status = "Started";
	}

	private void updateStatus(String status) {

		if (previousStatus.size() > 5)
			previousStatus.poll();
		previousStatus.offer(this.status);
		this.status = status;
	}

	@Override
	public void run() {
		while (switchedOn) {
			synchronized (lock) {
				while (currentVolume < unitSize) {
					try {
						System.out.println(" VEMAG EMPTY  " + currentVolume);
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			currentVolume = currentVolume - unitSize;
			// System.out.println("Running VEMAG volume remaining - " + currentVolume + " "
			// + (60/speed) * unitSize);
			updateStatus("Volume remaining - " + currentVolume + " speed " + (60 / speed) * unitSize);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void tipMix(int size) {
		this.currentVolume += size;
		System.out.println("------Vemag filled" + currentVolume);
		updateStatus("Vemag filled");
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public int getSpeed() {
		return speed;
	}

	public int getCurrentVolume() {
		return currentVolume;
	}

	public int getUnitSize() {
		return unitSize;
	}

	public boolean isSwitchedOn() {
		return switchedOn;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 16));

		int index = 0;
		for (String oldStatus : previousStatus) {
			g.drawString(oldStatus, 600, 110 - ((previousStatus.size() - index) * 20));
			index++;
		}
		g.setColor(Color.black);
		g.setFont(new Font("times new roman", Font.BOLD, 16));
		g.drawString(status, 600, 120);

	}

}
