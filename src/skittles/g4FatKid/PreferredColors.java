package skittles.g4FatKid;

public class PreferredColors {

	// ranks[0] = the highest ranked color, abdlTasteRank[1] is second, etc.
	private int[] ranks;
	private int numColors;
	
	public PreferredColors(int numColors) {
		ranks = new int[numColors];
		this.numColors = numColors;
		
		for (int i = 0; i < numColors; i++) {
			ranks[i] = -1;
		}
	}
	
	public void rerank(double[] adblTastes) {
		
		boolean[] ranked = new boolean[numColors];
		for (int i = 0; i < numColors; i++) {
			ranked[i] = false;
		}
		
		for (int rank = 0; rank < numColors; rank++) {
			int indexOfBest = 0;
			double best = 0;
			for (int i = 0; i < numColors; i++) {
				if (adblTastes[i] > best && ranked[i] != true) {
					best = adblTastes[i];
					indexOfBest = i;
				}
			}
			ranks[rank] = indexOfBest;
			ranked[indexOfBest] = true;
		}
		
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
	
	public void printRanks() {
		System.out.println("Personal preferences for each color (descending order):");
		for (int i = 0; i < numColors; i++) {
			System.out.print(ranks[i] + " ");
		}
		System.out.println();
	}
	
}
