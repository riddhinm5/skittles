package skittles.g4FatKid;

import skittles.sim.Offer;
import skittles.sim.Player;

public class G4FatKid extends Player {
	private int[] skittleBalanceArray;
	private int numberOfColors;
	double happiness;
	String className;
	int playerIndex;
	int playerNum;

	private double[] tasteArray;
	private int lastEatIndex;
	private int lastEatNum;

	// set verbose to false to suppress output of debug statements
	boolean verbose = true;

	// PlayerProfiles tracks net changes to all players
	private PlayerProfiles opponentProfiles;

	// Market tracks the total volume of all trades
	private Market market;

	// PreferredColors is a ranking of the colors we like
	private PreferredColors preferredColors;

	// stuff for EatStrategy
	private EatStrategy eatStrategy;
	int turn = 0;

	
	@Override
	public void eat(int[] aintTempEat) {
		
		int[] whatToEat;
		whatToEat = eatStrategy.eatNow(skittleBalanceArray);
		
		for (int i = 0; i < numberOfColors; i++) {
			aintTempEat[i] = whatToEat[i];
			if(aintTempEat[i] !=0){
				lastEatIndex = i;
				lastEatNum = aintTempEat[i]; 
			}
		}
		//aintTempEat[lastEatIndex] = lastEatNum;
		skittleBalanceArray[lastEatIndex] -= lastEatNum;
		//turn++;
	}

	@Override
	public void offer(Offer offTemp) {

		// what is desired: first match between personal prefs (from top down)
		// and rankings from Market
		// desirability = (personal rank of color x) + (market volume rank of
		// color x)
		// will take color with minimum value
		int mostDesired = Integer.MIN_VALUE;
		for (int i = 0; i < numberOfColors; i++) {
			int temp = preferredColors.getColorAtRank(i) + market.getColorAtRank(i);
			if (temp > mostDesired)
				mostDesired = i;
		}

		// what is offered: first match between personal prefs (from bottom up)
		// and rankings from Market
		int mostUndesired = Integer.MAX_VALUE;
		for (int i = 0; i < numberOfColors; i++) {
			int temp = preferredColors.getColorAtRank(numberOfColors - i - 1)
					+ market.getColorAtRank(i);
			if (temp < mostUndesired && i != mostDesired)
				mostUndesired = i;
		}

		// trade as many Undesired color as possible (at most 4) for Desired
		// color
		int amountToTrade = Math.min(skittleBalanceArray[mostUndesired], 4);
		int[] aintOffer = new int[numberOfColors];
		int[] aintDesire = new int[numberOfColors];
		for (int i = 0; i < numberOfColors; i++) {
			aintOffer[i] = 0;
			aintDesire[i] = 0;
			if (mostDesired == i)
				aintDesire[i] = amountToTrade;
			if (mostUndesired == i)
				aintOffer[i] = amountToTrade;
		}
		offTemp.setOffer(aintOffer, aintDesire);

	}

	@Override
	public void syncInHand(int[] aintInHand) {
		// TODO Auto-generated method stub

	}

	@Override
	public void happier(double dblHappinessUp) {
		double dblHappinessPerCandy = dblHappinessUp
				/ Math.pow(lastEatNum, 2);
		if (tasteArray[lastEatIndex] == -2) {
			tasteArray[lastEatIndex] = dblHappinessPerCandy;
			// update ranks in adblTastRanks (takes n^2 time)
			preferredColors.rerank(tasteArray);
			eatStrategy.updatePrefs(preferredColors);

			preferredColors.updateTastes(tasteArray);
		} else {
			if (tasteArray[lastEatIndex] != dblHappinessPerCandy) {
				System.out.println("Error: Inconsistent color happiness!");
			}
		}

		if (verbose) {
			// print the rankings of the colors
			preferredColors.printRanks();
			// print the tastes of each color
			preferredColors.printTastes();
			// print the median happiness color
			System.out.println("Median color is " + preferredColors.getMedian());
		}
	}

	@Override
	public Offer pickOffer(Offer[] currentOffers) {

		Offer offReturn = null;
		for (Offer offTemp : currentOffers) {
			if (offTemp.getOfferedByIndex() == playerIndex
					|| offTemp.getOfferLive() == false)
				continue;
			int[] skittlesLost = offTemp.getDesire();
			// first, check if we can even fulfill the offer
			if (checkEnoughInHand(skittlesLost)) {
				int[] skittlesGained = offTemp.getOffer();
				if (preferredColors.checkIfGoodOffer(skittlesLost, skittlesGained)) {
					offReturn = offTemp;
					for (int intColorIndex = 0; intColorIndex < numberOfColors; intColorIndex++) {
						skittleBalanceArray[intColorIndex] += skittlesGained[intColorIndex]
								- skittlesLost[intColorIndex];
					}
					break;
				} else
					continue;
			}
		}
		if (verbose) {
			if (offReturn != null) {
				System.out.println("Offer taken");
			}
		}
		// TODO instead of taking first "good" offer, make array of good offers,
		// then take the best of these

		return offReturn;
	}

	@Override
	public void offerExecuted(Offer offer) {
		if(verbose){
			System.out.println("after updation");
		}
		int[] offerList = offer.getOffer();
		int[] desiredList = offer.getDesire();
		for ( int intColorIndex = 0; intColorIndex < numberOfColors; intColorIndex ++ )
		{
			skittleBalanceArray[ intColorIndex ] += desiredList[ intColorIndex ] - offerList[ intColorIndex ];
		}
		
	}

	@Override
	public void updateOfferExe(Offer[] currentOffersArray) {

		// update opponentProfiles with the new transactions
		for (Offer offer : currentOffersArray) {

			if (offer.getPickedByIndex() != -1) {
				int[] aintTempOffer = offer.getOffer();
				int[] aintTempDesire = offer.getDesire();
				// update the offerer and accepter's profiles (note the switched
				// arguments)
				opponentProfiles.updatePlayer(offer.getOfferedByIndex(),
						aintTempOffer, aintTempDesire);
				opponentProfiles.updatePlayer(offer.getPickedByIndex(),
						aintTempDesire, aintTempOffer);
			}
		}

		if (verbose) {
			System.out.println("Net changes for all players:");
			opponentProfiles.printProfiles();
		}

		// update market
		market.updateTrades(opponentProfiles);
		if (verbose) {
			market.printVolumeTable();
			market.printRankings();
		}

	}

	@Override
	public void initialize(int playerNum, int playerIndex, String className, int[] aintInHand) {

		this.playerNum = playerNum;
		this.playerIndex = playerIndex;
		this.className = className;
		this.skittleBalanceArray = aintInHand;
		numberOfColors = aintInHand.length;
		happiness = 0;
		tasteArray = new double[numberOfColors];

		for (int intColorIndex = 0; intColorIndex < numberOfColors; intColorIndex++) {
			tasteArray[intColorIndex] = -2.0;
		}

		if (verbose) {
			System.out.println("FatKid starts");
		}

		// create PreferredColors object
		preferredColors = new PreferredColors(numberOfColors);
		eatStrategy = new EatStrategy(aintInHand, numberOfColors, preferredColors);

		// create EatStrategy object
		// es = new EatStrategy(aintInHand, intColorNum, preferredColors);

		// create PlayerProfile object; hard-coding 5 for number of Players
		// will change this to number of players when we find out how to get
		// this value from the
		// simulator. For now, this will work for any number of players less
		// than 10
		opponentProfiles = new PlayerProfiles(5, numberOfColors);

		// create Market object
		market = new Market(numberOfColors);

	}

	@Override
	public String getClassName() {
		return "g4FatKid";
	}

	@Override
	public int getPlayerIndex() {
		return playerIndex;
	}

	private boolean checkEnoughInHand(int[] aintTryToUse) {
		for (int intColorIndex = 0; intColorIndex < numberOfColors; intColorIndex++) {
			if (aintTryToUse[intColorIndex] > skittleBalanceArray[intColorIndex]) {
				return false;
			}
		}
		return true;
	}

}