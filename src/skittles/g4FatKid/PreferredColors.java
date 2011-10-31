package skittles.g4FatKid;

public class PreferredColors {

	// ranks[0] = the highest ranked color, ranks[1] is second, etc.
	private int[] ranks;
	private int numColors;
	private int median;
	private double[] adblTastes;
	
	public PreferredColors(int numColors) {
		ranks = new int[numColors];
		this.numColors = numColors;
		for (int i = 0; i < numColors; i++) {
			ranks[i] = -1;
		}
	}
	
	public void updateTastes(double[] adblTastes){
		for(int j=0;j<numColors;j++)
			this.adblTastes[j] = adblTastes[j];
	}
	
	public double returnTastes(int index){
		double taste = this.adblTastes[index];
		return taste;
	}
	public void rerank(double[] adblTastes) {
		
		this.adblTastes = adblTastes;
		boolean[] ranked = new boolean[numColors];
		for (int i = 0; i < numColors; i++) {
			ranked[i] = false;
		}
		
		for (int rank = 0; rank < numColors; rank++) {
			int indexOfBest = -1;
			double best = 0;
			for (int i = 0; i < numColors; i++) {
				if (adblTastes[i] > best && ranked[i] != true) {
					best = adblTastes[i];
					indexOfBest = i;
				}
			}
			ranks[rank] = indexOfBest;
			if (indexOfBest != -1) ranked[indexOfBest] = true;
		}
		
		int medRank = numColors/2;  // medRank is the middle rank
		while (medRank < 0 && adblTastes[ranks[medRank]] < 0.0) {
			medRank--;
		}
		this.median = medRank;  // median is the median value, or the smallest non-zero number if median is negative
		
	}
	
	public int getHighestRankedColor() {
		return ranks[0];
	}
	
	public int getLowestRankedColor() {
		int i = 0;
		while(ranks[i] != -1) {
			i++;
		}
		return ranks[i-1];
	}
	
	public int getColorAtRank(int rank) {
		return ranks[rank];
	}
	
	public int getMedian() {
		return median;
	}
	
	public void printRanks() {
		System.out.println("Personal preferences for each color (descending order):");
		for (int i = 0; i < numColors; i++) {
			System.out.print("Color " + ranks[i] + ", happiness value = ");
			if (ranks[i] == -1) System.out.println("unknown");
			else System.out.println(adblTastes[ranks[i]]);
		}
		System.out.println();
	}
	
	/*
	 * goodOffer evaluates offers
	 * Returns true if offer is "good," false otherwise
	 */
	public boolean goodOffer(int[] skittlesToLose, int[] skittlesToGain) {
		
		// basic good offer algorithm
		// takes offers that satisfy "nothing gained worst than median, nothing lost better than median"
		for (int i = 0; i < numColors; i++) {
			if (skittlesToLose[i] != 0 && i <= median) return false;
			if (skittlesToGain[i] != 0 && i > median) return false;
		}
		return true;
	}
	
}
