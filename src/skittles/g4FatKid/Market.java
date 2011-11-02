package skittles.g4FatKid;

public class Market {

	// volumeArray[x] = total volume of trades in color x
	private int[] volumeArray;
	private int numColors;
	private int[] demandArray;
	private int[] supplyArray;
	private int[] demandRanking;
	private int[] supplyRanking;

	// rankingArray[0] = color with highest volume, array[1] = color with second
	// highest volume, etc
	private int[] rankingArray;

	public Market(int numColors) {
		volumeArray = new int[numColors];
		demandArray = new int[numColors];
		supplyArray = new int[numColors];
		demandRanking = new int[numColors];
		supplyRanking = new int[numColors];
		rankingArray = new int[numColors];
		this.numColors = numColors;
	}
	
	public void updateDemand(PlayerProfiles opponentProfiles){
		for (int color = 0; color < numColors; color++) {
			demandArray[color] = opponentProfiles.getDemand(color);
		}
		boolean[] ranked = new boolean[numColors];
		for (int i = 0; i < numColors; i++) {
			ranked[i] = false;
		}
		
		for (int rank = 0; rank < numColors; rank++) {
			int bestDemand = -1;
			int bestDemandIndex = -1;
			for (int i = 0; i < numColors; i++) {
				if (demandArray[i] > bestDemand && ranked[i] != true) {
					bestDemand = demandArray[i];
					bestDemandIndex = i;
				}
			}
			demandRanking[rank] = bestDemandIndex;
			ranked[bestDemandIndex] = true;
		}
		
		
			
	}
	
	public void updateSupply(PlayerProfiles opponentProfiles){
		for (int color = 0; color < numColors; color++) {
			supplyArray[color] = opponentProfiles.getSupply(color);
		}
		boolean[] ranked = new boolean[numColors];
		for (int i = 0; i < numColors; i++) {
			ranked[i] = false;
		}
		for (int rank = 0; rank < numColors; rank++) {
			int bestSupply = -1;
			int bestSupplyIndex = -1;
			for (int i = 0; i < numColors; i++) {
				if (supplyArray[i] > bestSupply && ranked[i] != true) {
					bestSupply = supplyArray[i];
					bestSupplyIndex = i;
				}
			}
			supplyRanking[rank] = bestSupplyIndex;
			ranked[bestSupplyIndex] = true;
		}
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
	public int getColorAtRank(int rank) {
		return rankingArray[rank];
	}

	public int getDemandRanking(int color){
		for(int i =0; i<numColors;i++){
			if(demandRanking[i]==color)
				return i;
		}
		return -1;
	}
	
	public int getSupplyRanking(int color ){
		for(int i =0; i<numColors;i++){
			if(supplyRanking[i]==color)
				return i;
		}
		return -1;
	}
	
	public void printVolumeTable() {
		System.out.println("Volume totals:");
		for (int i = 0; i < numColors; i++) {
			System.out.print(volumeArray[i] + "\t");
		}
		System.out.println();
	}

	public void printDemandTable() {
		System.out.println("Demand totals:");
		for (int i = 0; i < numColors; i++) {
			System.out.print(demandArray[i] + "\t");
		}
		System.out.println();
	}
	public void printSupplyTable() {
		System.out.println("Supply totals:");
		for (int i = 0; i < numColors; i++) {
			System.out.print(supplyArray[i] + "\t");
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
