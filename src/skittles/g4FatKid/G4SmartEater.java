package skittles.g4FatKid;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.SortedMap;
import java.util.TreeMap;

import skittles.sim.Offer;
import skittles.sim.Player;

public class G4SmartEater extends Player {

	private int numberOfKnownElements = 0;
	private boolean isLastTransactionSuccessful = false;
	private int lastOfferedSkittle;
	private SortedMap<Double, Integer> skittlesMap = new TreeMap<Double, Integer>();
	private int[] skittleBalanceArray;
	private int numberOfColors;
	private int negativeIndex = -1;
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
		int colorPresent = onlyColorPresent();
		// make the first offer only if u r sure that the only skittle you have
		// tasted is above .9
		if (colorPresent != negativeIndex) {
			mostDesired = colorPresent;
			if (tasteArray[colorPresent] > higherThreshold) {
				for (int j = 0; j < numberOfColors; j++) {
					if (j != mostDesired && skittleBalanceArray[j] > 0) {
						aintDesire[mostDesired] = skittleBalanceArray[j] / 2;
						aintOffer[j] = aintDesire[mostDesired];
						desiredResult = true;
						break;
					}
				}
			}
		} else {

			int desireQuotient = -1;
			int offerQuotient = Integer.MIN_VALUE;
			int bestRank = Integer.MAX_VALUE;
			// / go for a greedy approach when you don't know the tastes of all
			// colors
			if (!isTasteBaseCompelete()) {
				if (skittleBalanceArray[skittlesMap.get(skittlesMap.firstKey())] == 0) {
					skittlesMap.remove(skittlesMap.firstKey());
				}
				mostUndesired = skittlesMap.get(skittlesMap.firstKey());
				if (!isLastTransactionSuccessful
						&& lastOfferedSkittle == mostUndesired) {
					for (Map.Entry<Double, Integer> entry : skittlesMap
							.entrySet()) {
						System.out.println(entry.getKey());
						System.out.println(entry.getValue());
						if (lastOfferedSkittle != entry.getValue()) {
							mostUndesired = entry.getValue();
							break;
						}
					}
				}
				mostDesired = skittlesMap.get(skittlesMap.lastKey());
				if (tasteArray[mostDesired] >= 0.0) {

					/*
					 * aintOffer[mostUndesired] = Math.min(
					 * skittleBalanceArray[mostUndesired],
					 * skittleBalanceArray[mostUndesired] / 2);
					 */

					aintOffer[mostUndesired] = skittleBalanceArray[mostUndesired];
					aintDesire[mostDesired] = aintOffer[mostUndesired];
				}
			} else {
				if (verbose) {
					System.out.println("taste base complete");
				}
				desiredResult = false;
				for (int i = 0; i < numberOfKnownElements / 2; i++) {
					for (int j = numberOfColors - 1; j >= numberOfKnownElements / 2; j--) {
						if (preferredColors.getColorAtRank(j) != -1
								&& skittleBalanceArray[preferredColors
										.getColorAtRank(j)] > 0) {
							int numberPossible = opponentProfiles
									.isOfferFeasible(playerNum,
											this.playerIndex,
											preferredColors.getColorAtRank(j),
											preferredColors.getColorAtRank(i));
							if (numberPossible != -1) {
								aintOffer[preferredColors.getColorAtRank(j)] = numberPossible;
								aintDesire[preferredColors.getColorAtRank(i)] = numberPossible;
								desiredResult = true;
								break;
							}
						}
					}
					if (desiredResult)
						break;

				}

			}
		}
		lastOfferedSkittle = mostUndesired;
		offTemp.setOffer(aintOffer, aintDesire);

	}

	@Override
	public void syncInHand(int[] aintInHand) {
		// TODO Auto-generated method stub

	}

	@Override
	public void happier(double dblHappinessUp) {
		numberOfKnownElements++;
		double dblHappinessPerCandy = dblHappinessUp / Math.pow(lastEatNum, 2);
		if (tasteArray[lastEatIndex] == -2) {
			tasteArray[lastEatIndex] = dblHappinessPerCandy;
			skittlesMap.put(dblHappinessPerCandy, lastEatIndex);
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
			if (offTemp.getOfferedByIndex() == playerIndex
					|| offTemp.getOfferLive() == false)
				continue;
			int[] skittlesLost = offTemp.getDesire();
			for (int i = 0; i < numberOfColors; i++) {
				if (skittlesLost[i] != 0)
					allNullOffers = false;
			}
			// first, check if we can even fulfill the offer
			if (checkEnoughInHand(skittlesLost)) {
				int[] skittlesGained = offTemp.getOffer();
				if (preferredColors.checkIfGoodOffer(skittlesLost,
						skittlesGained)) {
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
		if (allNullOffers)
			streakOfNullOffers++;
		// else, reset the count to zero
		else
			streakOfNullOffers = 0;

		// check if the streak surpassed the threshold, and set endOfGame if it
		// has
		if (streakOfNullOffers > maxNullOfferRounds)
			endOfGame = true;

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
		isLastTransactionSuccessful = false;
		// update opponentProfiles with the new transactions
		for (Offer offer : currentOffersArray) {
			if (offer.getPickedByIndex() != -1
					&& offer.getOfferedByIndex() == this.playerIndex) {
				System.out.println("my offer got accepted");
				isLastTransactionSuccessful = true;
			}
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
		if (noExecutedOffers)
			streakOfNoTrades++;
		// else, reset the count to zero
		else
			streakOfNullOffers = 0;

		// check if the streak surpassed the threshold, and set endOfGame if it
		// has
		if (streakOfNoTrades > maxNoTradesExecuted)
			endOfGame = true;

	}

	@Override
	public void initialize(int playerNum, int playerIndex, String className,
			int[] aintInHand) {
		this.lastOfferedSkittle = -1;
		this.isLastTransactionSuccessful = true;
		this.lowerThreshold = 0.0;
		this.higherThreshold = 0.9;
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
			System.out.println("SmartEater starts");
		}

		// create PreferredColors object
		preferredColors = new PreferredColors(numberOfColors);

		// create EatStrategy object
		eatStrategy = new EatStrategy(aintInHand, numberOfColors,
				preferredColors);

		// create PlayerProfile object
		opponentProfiles = new PlayerProfiles(playerNum, numberOfColors,
				skittleBalanceArray);

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
		return "g4SmartEater";
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

	// to check if this is the first round
	private Integer onlyColorPresent() {
		int countOfDefinedTastes = 0;
		int unKnownColor = -1;
		int knownColor = 0;
		for (int i = 0; i < numberOfColors; i++) {
			if (tasteArray[i] != -2.0 && countOfDefinedTastes <= 2) {
				knownColor = i;
				countOfDefinedTastes++;
			}
		}
		if (countOfDefinedTastes < 2)
			return knownColor;
		return unKnownColor;
	}

	// to check if the taste base can be considered to be complete
	private boolean isTasteBaseCompelete() {
		int countOfDefinedTastes = 0;

		for (int i = 0; i < numberOfColors; i++) {
			if (tasteArray[i] != -2.0)
				countOfDefinedTastes++;
		}
		if (countOfDefinedTastes > (numberOfColors * 3) / 4)
			return true;
		return false;
	}

}