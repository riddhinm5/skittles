package skittles.manualplayer;

import java.util.Scanner;

import skittles.sim.Game;
import skittles.sim.Offer;
import skittles.sim.Player;

public class ManualP extends Player
{
	private int[] aintInHand;
	private int intColorNum;
	double dblHappiness;
	String strClassName;
	int intPlayerIndex;
	
	public ManualP( int[] aintInHand )
	{
		this.aintInHand = aintInHand;
		intColorNum = aintInHand.length;
		dblHappiness = 0;
	}
	
	public ManualP()
	{
	}
	
	@Override
	public void initialize( int intPlayerIndex, String strClassName, int[] aintInHand ) 
	{
		this.intPlayerIndex = intPlayerIndex;
		this.strClassName = strClassName;
		this.aintInHand = aintInHand;
		intColorNum = aintInHand.length;
		dblHappiness = 0;
	}

	@Override
	public void eatAndOffer(int[] aintTempEat, Offer offTemp) 
	{
		// let the manual player know what's in hand
		System.out.println( "Player #" + intPlayerIndex + " (" + strClassName + "), You have: " + Game.arrayToString( aintInHand ) );
		
		// ask for action
//		Scanner scnInput = new Scanner( System.in );
		System.out.println( "What do you eat? (" + intColorNum + " colors)" );
		String strEat = Game.scnInput.nextLine();
		String[] astrEat = strEat.split( "," );
		System.out.println( "What do you offer? (" + intColorNum + " colors)" );
		String strOffer = Game.scnInput.nextLine();
		String[] astrOffer = strOffer.split( "," );
		System.out.println( "What do you desire? (" + intColorNum + " colors)" );
		String strDesire = Game.scnInput.nextLine();
		String[] astrDesire = strDesire.split( "," );
		
		int[] aintTempOffer = new int[ intColorNum ];
		int[] aintTempDesire = new int[ intColorNum ];
		
		for ( int intColorIndex = 0; intColorIndex < intColorNum; intColorIndex ++ )
		{
			int intEatCurrentColorNum = Integer.parseInt( astrEat[ intColorIndex ] );
			if ( intEatCurrentColorNum > aintInHand[ intColorIndex ] )
			{
				System.out.println( "You eat more than you have. Take it out of your mouth!" );			// need more code to make it more robust
			}
			else
			{
				aintInHand[ intColorIndex ] -= intEatCurrentColorNum;
			}
			aintTempEat[ intColorIndex ] = intEatCurrentColorNum;
			aintTempOffer[ intColorIndex ] = Integer.parseInt( astrOffer[ intColorIndex ] );
			aintTempDesire[ intColorIndex ] = Integer.parseInt( astrDesire[ intColorIndex ] );
		}
		offTemp.setOffer( aintTempOffer, aintTempDesire );
	}

	@Override
	public void syncInHand(int[] aintInHand) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void happier(double dblHappinessUp) 
	{
		// TODO Auto-generated method stub
		dblHappiness += dblHappinessUp;
		System.out.println( "You eat, and you are happier by: " + dblHappinessUp );
		System.out.println();
		System.out.println();
	}

	@Override
	public Offer pickOffer(Offer[] aoffCurrentOffers) 
	{
		System.out.println( "Player #" + intPlayerIndex + " (" + strClassName + "), your turn to pick a offer from: " );
		for ( int intOfferIndex = 0; intOfferIndex < aoffCurrentOffers.length; intOfferIndex ++ )
		{
			if ( aoffCurrentOffers[ intOfferIndex ].getOfferLive() && aoffCurrentOffers[ intOfferIndex ].getOfferedByIndex() != intPlayerIndex )
			{
				System.out.println( "Offer #" + intOfferIndex + ": " + aoffCurrentOffers[ intOfferIndex ].toString() );
			}
		}
		System.out.println( "(You have " + Game.arrayToString( aintInHand ) + ") " );
		String strPickedOffer = Game.scnInput.nextLine();
		int intPickedOffer = Integer.parseInt( strPickedOffer );
		if ( intPickedOffer >= 0 && intPickedOffer < aoffCurrentOffers.length )
		{
			if ( aoffCurrentOffers[ intPickedOffer ].getOfferLive() )
			{
				int[] aintOffer = aoffCurrentOffers[ intPickedOffer ].getOffer();
				int[] aintDesire = aoffCurrentOffers[ intPickedOffer ].getDesire();
				for ( int intColorIndex = 0; intColorIndex < intColorNum; intColorIndex ++ )
				{
					aintInHand[ intColorIndex ] += aintOffer[ intColorIndex ] - aintDesire[ intColorIndex ];
				}
				return aoffCurrentOffers[ intPickedOffer ];
			}
		}
		return null;
		
	}

	@Override
	public void offerExecuted(Offer offPicked) 
	{
		int[] aintOffer = offPicked.getOffer();
		int[] aintDesire = offPicked.getDesire();
		for ( int intColorIndex = 0; intColorIndex < intColorNum; intColorIndex ++ )
		{
			aintInHand[ intColorIndex ] += aintDesire[ intColorIndex ] - aintOffer[ intColorIndex ];
		}
	}

	@Override
	public void updateOfferExe(Offer[] aoffCurrentOffers) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getClassName() 
	{
		// TODO Auto-generated method stub
		return strClassName;
	}

	@Override
	public int getPlayerIndex() 
	{
		// TODO Auto-generated method stub
		return intPlayerIndex;
	}	
}
