package skittles.g4FatKid;

public class PlayerProfiles {

	// netTrades[x][y] = net change for player x of candies with color y
	private int[][] netTrades;
	private int numPlayers;
	private int numColors;

	public PlayerProfiles(int numPlayers, int numColors) {
		netTrades = new int[numPlayers][numColors];
		this.numPlayers = numPlayers;
		this.numColors = numColors;
	}

	
	public void updatePlayer(int player, int[] offerArray, int[] desireArray) {

		for (int i = 0; i < numColors; i++) {
			netTrades[player][i] += offerArray[i];
			netTrades[player][i] -= desireArray[i];
		}
	}

	/*
	 * returns the total volume for the specified color
	 */
	public int getColorTotal(int color) {
		int sum = 0;
		for (int j = 0; j < numPlayers; j++) {
			if (netTrades[j][color] > 0)
				sum += netTrades[j][color];
		}
		return sum;
	}
	
	public int getDemand(int color){
		int sum = 0;
		for (int j = 0; j < numPlayers; j++) {
			if (netTrades[j][color] > 0)
				sum += netTrades[j][color];
		}
		return sum;
	}

	public int getSupply(int color){
		int sum = 0;
		for (int j = 0; j < numPlayers; j++) {
			if (netTrades[j][color] < 0)
				sum += (-netTrades[j][color]);
		}
		return sum;
	}
	public void printProfiles() {

		for (int i = 0; i < numPlayers; i++) {
			System.out.print("Player " + i + ": ");
			for (int j = 0; j < numColors; j++) {
				System.out.print(netTrades[i][j] + "\t");
			}
			System.out.println();
		}
	}
	
}
