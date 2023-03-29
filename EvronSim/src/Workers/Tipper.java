package Workers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import equipment.Bowl;
import equipment.PoweredMixer;
import equipment.Vemag;
import ingredients.DryIngredient;
import ingredients.IngredientBin;
import recipes.Drawable;

//******************************************************************//
//Third Mixer man                                                   //
//Mixer man keeps ingredient bins filled and tips mixes into vemag  //
//
//Checks bins current volume and fills them if they are too low
//When the mixing machine finishes takes the bowl to the vemag and
//tips the mix into the vemag resulting in an empty bowl
//******************************************************************//



public class Tipper implements Runnable, Drawable {

	private List<PoweredMixer> mixers = new ArrayList<>();
	
	private Map<DryIngredient, IngredientBin> bins;
	List<Bowl> bowls = new ArrayList<>();
	private boolean finished = false;
	private Vemag vemag;
	
	private int numMixes = 10;
	
	ConcurrentLinkedQueue <String> previousStatus;
	
	//Rendering info
	private String status = "";
	private int x = 30;
	private int y = 30;

	public Tipper(Map<DryIngredient, IngredientBin> bins, 
			List<Bowl> bowls, List<PoweredMixer> mixers, Vemag vemag, int numMixes) {
		this.bins = bins;
		this.bowls = bowls;
		this.mixers = mixers;
		this.vemag = vemag;
		this.numMixes = numMixes;
		
		previousStatus = new ConcurrentLinkedQueue<>();
		// fill bins as long as they arent being used
		// when mix is ready tip mix

	}

	@Override
	public void run() {
		
		status = "Started Working";

		while (!finished) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
			checkBins();
			
			for (Bowl bowl : bowls) {
				if(bowl.isFinishedMixing())
					tipMix(bowl);
				
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	private void updateStatus(String status) {
		
		if(previousStatus.size()> 5) previousStatus.poll();
		previousStatus.offer(this.status);
		this.status = status;
	}

	private void checkBins() {
		for (IngredientBin bin : bins.values()) {
			if (bin.beingUsed() == false && bin.getCurrentVolume() < bin.getMaxVolume() / 2) {
				bin.useBin();
				
				fillBin(bin);
				
				bin.releaseBin();
				
			}
		}
	}

	private void fillBin(IngredientBin bin) {
		System.out.println(bin.getType() + " " + bin.getCurrentVolume());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		bin.setCurrentVolume(bin.getMaxVolume());
		System.out.println("Bin filled " + bin.getType());
		updateStatus("Bin filled " + bin.getType());
	}

	private void tipMix(Bowl bowl) {
		System.out.println("-------------Tipped Mix into Vemag");
		updateStatus("-------------Tipped Mix into Vemag");
		bowl.setEmpty();
		vemag.tipMix(100000); //bowl should be aware of volume?
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.BOLD, 16));
		
		int index = 0;
		for(String oldStatus: previousStatus) {
			g.drawString(oldStatus, 550, 510-((previousStatus.size() - index)*20));
			index++;
		}
		
		g.setColor(Color.black);
		g.setFont(new Font("times new roman", Font.BOLD, 16));
		g.drawString(status, 560, 520);
		
	}

}
