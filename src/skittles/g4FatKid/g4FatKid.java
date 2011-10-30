package skittles.g4FatKid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import skittles.sim.*;

public class G4FatKid extends Player{

	private int[] aintInHand;
	private int intColorNum;
	double dblHappiness;
	String strClassName;
	int intPlayerIndex;
	
	private double[] adblTastes;
	private int intLastEatIndex;
	private int intLastEatNum;
	
	// set verbose to false to suppress output of debug statements
	boolean verbose = true;
	
	// PlayerProfiles tracks net changes to all players
	private PlayerProfiles opponentProfiles;
	
	// Market tracks the total volume of all trades
	private Market market;
	
	// stuff for EatStrategy
	private EatStrategy es;
	private String whatToEatNext;
	private SortedMap<Integer, Double> whatILikeMostScore;
	int turn = 0;
	
	@Override
	public void eat( int[] aintTempEat )
	{
		int intMaxColorIndex = -1;
		int intMaxColorNum = 0;
		for ( int intColorIndex = 0; intColorIndex < intColorNum; intColorIndex ++ )
		{
			if ( aintInHand[ intColorIndex ] > intMaxColorNum )
			{
				intMaxColorNum = aintInHand[ intColorIndex ];
				intMaxColorIndex = intColorIndex;
			}
		}
		aintTempEat[ intMaxColorIndex ] = intMaxColorNum;
		aintInHand[ intMaxColorIndex ] = 0;
		intLastEatIndex = intMaxColorIndex;
		intLastEatNum = intMaxColorNum;
	}
	/*
	public void eat(int[] aintTempEat) {
		
		if (turn == 0)
			whatToEatNext = es.eatNow(0);
		else
			whatToEatNext = es.eatNow(intLastEatIndex);
		String[] whichSkittle = whatToEatNext.split(" ");
		int skittleColor = Integer.parseInt(whichSkittle[0]);
		int numSkittles = Integer.parseInt(whichSkittle[1]);
		aintTempEat[ skittleColor ] = numSkittles;
		aintInHand[ skittleColor ] -= numSkittles;
		intLastEatIndex = skittleColor;
		intLastEatNum = numSkittles;
		turn++;
	}*/

	@Override
	public void offer(Offer offTemp) {
		
		// what is desired: first match between WhatILikeMostScore (from top down) and rankings from Market
		
		// what is offered: first match between WhatILikeMostScore (from bottom up) and rankings from Market
		
	}

	@Override
	public void syncInHand(int[] aintInHand) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void happier(double dblHappinessUp) 
	{
		double dblHappinessPerCandy = dblHappinessUp / Math.pow( intLastEatNum, 2 );
		if ( adblTastes[ intLastEatIndex ] == -1 )
		{
			adblTastes[ intLastEatIndex ] = dblHappinessPerCandy;
		}
		else
		{
			if ( adblTastes[ intLastEatIndex ] != dblHappinessPerCandy )
			{
				System.out.println( "Error: Inconsistent color happiness!" );
			}
		}
	}
	/*
	public void happier(double dblHappinessUp) {
		// TODO Auto-generated method stub
		double dblHappinessPerCandy = dblHappinessUp / Math.pow( intLastEatNum, 2 );
		if ( adblTastes[ intLastEatIndex ] == -1 )
		{
			adblTastes[ intLastEatIndex ] = dblHappinessPerCandy;
			whatILikeMostScore.put(intLastEatIndex, adblTastes[intLastEatIndex]);
		}
		else
		{
			if ( adblTastes[ intLastEatIndex ] != dblHappinessPerCandy )
			{
				System.out.println( "Error: Inconsistent color happiness!" );
			}
		}
	}*/

	@Override
	public Offer pickOffer(Offer[] aoffCurrentOffers) {
		// TODO Auto-generated method stub
		Offer offReturn = null;
		for ( Offer offTemp : aoffCurrentOffers )
		{
			if ( offTemp.getOfferedByIndex() == intPlayerIndex || offTemp.getOfferLive() == false )
				continue;
			int[] aintDesire = offTemp.getDesire();
			if ( checkEnoughInHand( aintDesire ) )
			{
				offReturn = offTemp;
				aintDesire = offReturn.getDesire();
				int[] aintOffer = offReturn.getOffer();
				for ( int intColorIndex = 0; intColorIndex < intColorNum; intColorIndex ++ )
				{
					aintInHand[ intColorIndex ] += aintOffer[ intColorIndex ] - aintDesire[ intColorIndex ];
				}
				break;
			}
		}

		return offReturn;
	}

	@Override
	public void offerExecuted(Offer offPicked) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateOfferExe(Offer[] aoffCurrentOffers) {
		
		// update opponentProfiles with the new transactions
		for ( Offer offTemp : aoffCurrentOffers ) {
			
			if ( offTemp.getPickedByIndex() != -1 )
			{
				int[] aintTempOffer = offTemp.getOffer();
				int[] aintTempDesire = offTemp.getDesire();
				// update the offerer and accepter's profiles (note the switched arguments)
				opponentProfiles.updatePlayer(offTemp.getOfferedByIndex(), aintTempOffer, aintTempDesire);
				opponentProfiles.updatePlayer(offTemp.getPickedByIndex(), aintTempDesire, aintTempOffer);
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
	public void initialize(int intPlayerIndex, String strClassName,int[] aintInHand) {
		// TODO Auto-generated method stub
		this.intPlayerIndex = intPlayerIndex;
		this.strClassName = strClassName;
		this.aintInHand = aintInHand;
		intColorNum = aintInHand.length;
		dblHappiness = 0;
		adblTastes = new double[ intColorNum ];
		
		for ( int intColorIndex = 0; intColorIndex < intColorNum; intColorIndex ++ ) {
			adblTastes[ intColorIndex ] = -1;
		}
		
		// create EatStrategy object
		es = new EatStrategy(aintInHand, intColorNum, whatILikeMostScore);
		
		// create PlayerProfile object; hard-coding 5 for number of Players
		// will change this to number of players when we find out how to get this value from the
		// simulator.  For now, this will work for any number of players less than 10
		opponentProfiles = new PlayerProfiles(5, intColorNum);
		
		// create Market object
		market = new Market(intColorNum);
		
	}

	@Override
	public String getClassName() {
		return "g4FatKid";
	}

	@Override
	public int getPlayerIndex() {
		return intPlayerIndex;
	}
	
	private boolean checkEnoughInHand( int[] aintTryToUse )
	{
		for ( int intColorIndex = 0; intColorIndex < intColorNum; intColorIndex ++ )
		{
			if ( aintTryToUse[ intColorIndex ] > aintInHand[ intColorIndex ] )
			{
				return false;
			}
		}
		return true;
	}
	
}