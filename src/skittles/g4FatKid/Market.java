package skittles.g4FatKid;

public class Market {

	// volumeArray[x] = total volume of trades in color x
	private int[] volumeArray;
	private int numColors;
	
	// rankingArray[0] = color with highest volume, array[1] = color with second highest volume, etc
	private int[] rankingArray;
	
	public Market(int numColors) {
		volumeArray = new int[numColors];
		rankingArray = new int[numColors];
		this.numColors = numColors;
	}
	
	public void updateTrades(PlayerProfiles opponentProfiles) {
		for (int color = 0; color < numColors; color++) {
			volumeArray[color] = opponentProfiles.getColorTotal(color);
		}
		
		// also update rankingArray
		boolean[] ranked = new boolean[numColors];
		for (int i = 0; i < numColors; i++) {
			ranked[i] = false;
		}
		for (int rank = 0; rank < numColors; rank++) {
			int best = -1;
			int indexOfBest = -1;
			for (int i = 0; i < numColors; i++) {
				if (volumeArray[i] > best && ranked[i] != true) {
					best = volumeArray[i];
					indexOfBest = i;
				}
			}
			rankingArray[rank] = indexOfBest;
			ranked[indexOfBest] = true;
		}

	}
	/*
	 * takes rank as input, returns the color with that rank
	 */
	public int getColorWithRank(int rank) {
		return rankingArray[rank];
	}
	
	public void printVolumeTable() {
		System.out.println("Volume totals:");
		for (int i = 0; i < numColors; i++) {
			System.out.print(volumeArray[i] + "\t");
		}
		System.out.println();
	}
	
	public void printRankings() {
		System.out.println("Color rankings:");
		for (int i = 0; i < numColors; i++) {
			System.out.print(rankingArray[i] + "\t");
		}
		System.out.println();
	}
	
}
