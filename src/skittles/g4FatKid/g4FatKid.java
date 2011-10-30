package skittles.g4FatKid;

import java.util.ArrayList;
import java.util.List;

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
	//private List<Double> whatILikeMost;
	
	@Override
	public void eat(int[] aintTempEat) {
		// TODO Auto-generated method stub
		whatToEatNext = es.eatNow();
		String[] whichSkittle = whatToEatNext.split(" ");
		int skittleColor = Integer.parseInt(whichSkittle[0]);
		int numSkittles = Integer.parseInt(whichSkittle[1]);
		aintTempEat[ skittleColor ] = numSkittles;
		aintInHand[ skittleColor ] -= numSkittles;
		intLastEatIndex = skittleColor;
		intLastEatNum = numSkittles;
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
		
	}

	@Override
	public Offer pickOffer(Offer[] aoffCurrentOffers) {
		// TODO Auto-generated method stub
		return null;
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
		es = new eatStrategy(aintInHand,intColorNum,whatILikeMost,intLastEatIndex);	
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
	
	//Todo: 
	
}