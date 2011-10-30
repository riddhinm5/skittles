package skittles.g4FatKid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import skittles.sim.*;

public class g4FatKid extends Player{

	private int[] aintInHand;
	private int intColorNum;
	double dblHappiness;
	String strClassName;
	int intPlayerIndex;
	
	private double[] adblTastes;
	private int intLastEatIndex;
	private int intLastEatNum;
	private eatStrategy es;
	private List<Integer> whatILikeMost;
	private String whatToEatNext;
	private SortedMap<Integer, Double> whatILikeMostScore;
	//private List<Double> whatILikeMost;
	int turn = 0;
	
	@Override
	public void eat(int[] aintTempEat) {
		// TODO Auto-generated method stub
		if(turn == 0)
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
	}

	@Override
	public void offer(Offer offTemp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void syncInHand(int[] aintInHand) {
		// TODO Auto-generated method stub
		
	}

	@Override
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
	}

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
		// TODO Auto-generated method stub
		
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
		for ( int intColorIndex = 0; intColorIndex < intColorNum; intColorIndex ++ )
		{
			adblTastes[ intColorIndex ] = -1;
		}
		es = new eatStrategy(aintInHand,intColorNum,whatILikeMostScore);	
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlayerIndex() {
		// TODO Auto-generated method stub
		return 0;
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
	//Todo: 
	
}