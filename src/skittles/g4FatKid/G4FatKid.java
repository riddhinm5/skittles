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
	private double lowerThreshold;
	private double higherThreshold;

	private double[] tasteArray;
	private int lastEatIndex;
	private int lastEatNum;

	private boolean endOfGame;
	private int maxNullOfferRounds;
	private int streakOfNullOffers;
	private int maxNoTradesExecuted;
	private int streakOfNoTrades;
	
	// set verbose to false to suppress output of debug statements
	boolean verbose = false;
	boolean desiredResult = false;
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
		whatToEat = eatStrategy.eatNow(skittleBalanceArray, endOfGame);

		for (int i = 0; i < numberOfColors; i++) {
			aintTempEat[i] = whatToEat[i];
			if (aintTempEat[i] != 0) {
				lastEatIndex = i;
				lastEatNum = aintTempEat[i];
			}
		}
		// aintTempEat[lastEatIndex] = lastEatNum;
		skittleBalanceArray[lastEatIndex] -= lastEatNum;
		// turn++;
	}

	@Override
	public void offer(Offer offTemp) {
		int mostDesired = -1;
		int mostUndesired = -1;
		int[] aintOffer = new int[numberOfColors];
		int[] aintDesire = new int[numberOfColors];
		
		if (!isTasteKnowledgeSufficient()) {
			
			for (int i = 0; i < numberOfColors; i++) {
				if (tasteArray[i] != -2.0) {
					if (tasteArray[i] < lowerThreshold) {
						mostUndesired = i;
						break;
					} else if (tasteArray[i] > higherThreshold) {
						mostDesired = i;
						break;
					}
				}
			}
			for (int i = 0; i < numberOfColors; i++) {
				if(desiredResult)break;
				aintOffer[i] = 0;
				aintDesire[i] = 0;
				if (mostDesired == i && skittleBalanceArray[i] > 0) {
					for (int j = 0; j < numberOfColors; j++) {
						if (j != mostDesired && skittleBalanceArray[j] > 0) {
							if(skittleBalanceArray[mostDesired]>skittleBalanceArray[j])
							aintDesire[i] = Math.min(
									skittleBalanceArray[mostDesired],
									skittleBalanceArray[j]);
							else
								aintDesire[i] = Math.max(
										skittleBalanceArray[mostDesired],
										skittleBalanceArray[j]);
							aintOffer[j] = aintDesire[i];
							desiredResult=true;
							break;
						}
					}
				}
				if (mostUndesired == i && skittleBalanceArray[i] > 0) {
					for (int j = 0; j < numberOfColors; j++) {
						if (j != mostDesired && skittleBalanceArray[j] > 0) {
							aintOffer[i] = Math.max(
									skittleBalanceArray[mostUndesired],
									skittleBalanceArray[j]);
							aintDesire[j] = aintOffer[i];
							break;
						}
					}
				}
			}
			
		} else {
			// what is desired: first match between personal prefs (from top
			// down) and rankings from Market
			// desirability = (personal rank of color x) + (market volume rank
			// of color x)
			// will take color with minimum value
			mostDesired = Integer.MIN_VALUE;
			mostUndesired = Integer.MAX_VALUE;
			int desireQuotient = -1;
			int offerQuotient = Integer.MIN_VALUE;
			for (int i = 0; i < numberOfColors; i++) {
				if (market.getSupplyRanking(i) != -1
						&& preferredColors.getRankOfColor(i) != -1) {
					if ((preferredColors.getRankOfColor(i) <preferredColors.getMedian()) &&(market.getSupplyRanking(i)>desireQuotient) ) {
						desireQuotient = market.getSupplyRanking(i) ;
						mostDesired = i;
					}
				}
			}

			// what is offered: first match between personal prefs (from bottom
			// up)
			// and rankings from Market
			int max = Integer.MIN_VALUE;
			for (int i = 0; i < numberOfColors; i++) {
				if (preferredColors.getRankOfColor(i) == -1
						|| preferredColors.getRankOfColor(i) >= preferredColors
								.getMedian()) {
					int temp = preferredColors.getRankOfColor(i);
					if (((temp==-1|| temp > offerQuotient) && i != mostDesired) && skittleBalanceArray[i]> max ) {
						offerQuotient = temp;
						mostUndesired = i;
						max=skittleBalanceArray[i];
					}
				}
			}
			desiredResult=false;
			for (int i = 0; i < numberOfColors; i++) {
				if(desiredResult)break;
				aintOffer[i] = 0;
				aintDesire[i] = 0;
				if (mostDesired == i && skittleBalanceArray[i] > 0){
					for(int j=0;j<numberOfColors;j++){
						if(mostUndesired ==j && skittleBalanceArray[j]>0){
							if(skittleBalanceArray[mostUndesired]>skittleBalanceArray[mostDesired])
								aintDesire[i] = Math.max(skittleBalanceArray[mostDesired],skittleBalanceArray[mostUndesired]);
							else
								aintDesire[i] = Math.min(skittleBalanceArray[mostDesired],skittleBalanceArray[mostUndesired]);
							aintOffer[j]=aintDesire[i];
							desiredResult=true;
							break;
						}
					}
				}
			}
		}
		// trade as many Undesired color as possible (at most 4) for Desired
		// color
		
		offTemp.setOffer(aintOffer,aintDesire );

	}

	@Override
	public void syncInHand(int[] aintInHand) {
		// TODO Auto-generated method stub

	}

	@Override
	public void happier(double dblHappinessUp) {
		double dblHappinessPerCandy = dblHappinessUp / Math.pow(lastEatNum, 2);
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
			System.out.println("Median color is "
					+ preferredColors.getMedianElement());
		}
	}
	
	@Override
	public Offer pickOffer(Offer[] currentOffers) {

		boolean allNullOffers = true;
		Offer offReturn = null;
		for (Offer offTemp : currentOffers) {
			if (offTemp.getOfferedByIndex() == playerIndex || offTemp.getOfferLive() == false)
				continue;
			int[] skittlesLost = offTemp.getDesire();
			for (int i = 0; i < numberOfColors; i++) {
				if (skittlesLost[i] != 0) allNullOffers = false;
			}
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
		// if all the offers are null, increment the streak count
		if (allNullOffers) streakOfNullOffers++;
		// else, reset the count to zero
		else streakOfNullOffers = 0;
		
		// check if the streak surpassed the threshold, and set endOfGame if it has
		if (streakOfNullOffers > maxNullOfferRounds) endOfGame = true;
		
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
		if (verbose) {
			System.out.println("after updation");
		}
		int[] offerList = offer.getOffer();
		int[] desiredList = offer.getDesire();
		for (int intColorIndex = 0; intColorIndex < numberOfColors; intColorIndex++) {
			skittleBalanceArray[intColorIndex] += desiredList[intColorIndex]
					- offerList[intColorIndex];
		}

	}

	@Override
	public void updateOfferExe(Offer[] currentOffersArray) {

		boolean noExecutedOffers = true;
		// update opponentProfiles with the new transactions
		for (Offer offer : currentOffersArray) {

			if (offer.getPickedByIndex() != -1) {
				noExecutedOffers = false;
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
		market.updateDemand(opponentProfiles);
		market.updateSupply(opponentProfiles);
		if (verbose) {
			market.printVolumeTable();
			market.printRankings();
			market.printSupplyTable();
		}
		
		// update count of streaks of no trades
		if (noExecutedOffers) streakOfNoTrades++;
		// else, reset the count to zero
		else streakOfNullOffers = 0;
		
		// check if the streak surpassed the threshold, and set endOfGame if it has
		if (streakOfNoTrades > maxNoTradesExecuted) endOfGame = true;

	}

	@Override
	public void initialize(int playerNum, int playerIndex, String className,
			int[] aintInHand) {

		this.lowerThreshold = 0.0;
		this.higherThreshold = 0.5;
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

		// create EatStrategy object
		eatStrategy = new EatStrategy(aintInHand, numberOfColors,
				preferredColors);

		// create PlayerProfile object
		opponentProfiles = new PlayerProfiles(playerNum, numberOfColors);

		// create Market object
		market = new Market(numberOfColors);
		
		endOfGame = false;
		maxNullOfferRounds = 3;
		streakOfNullOffers = 0;
		maxNoTradesExecuted = 5;
		streakOfNoTrades = 0;

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

	// really basic function written to written false when
	// taste of only color is known ..else return true
	private boolean isTasteKnowledgeSufficient() {
		int countOfDefinedTastes = 0;
		for (int i = 0; i < numberOfColors; i++) {
			if (tasteArray[i] != -2.0)
				countOfDefinedTastes++;
		}
		if (countOfDefinedTastes > 1)
			return true;
		return false;
	}

}