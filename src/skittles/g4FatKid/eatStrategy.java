package skittles.g4FatKid;

import java.util.ArrayList;
import java.util.List;

import skittles.sim.*;

class eatStrategy{
	private int[] aintInHand;
	private int intColorNum;
	//double dblHappiness;
	//String strClassName;
	//int intPlayerIndex;
	private double aproxDist;
	private double[] adblTastes;
	private int intLastEatIndex;
	private int intLastEatNum;
	private List<Integer> whatILikeMost = new ArrayList<Integer>();
	private int likeMostInitCount = 0;
	
	eatStrategy(int[] aintInHand, int intColorNum, List<Integer> whatILikeMost, int intLastEatIndex){	
		this.aintInHand = aintInHand;
		this.intColorNum = intColorNum;
		this.whatILikeMost = whatILikeMost;
		this.intLastEatIndex = intLastEatIndex;
		aproxDist = 1/aintInHand.length;
		likeMostInitCount = aintInHand[0];
	}
	
	public String eatNow(){
		String whatToEatNow = "";
		if (intLastEatIndex < aintInHand.length)
			whatToEatNow = (intLastEatIndex+1)+" "+1+"";
		else if(aintInHand[whatILikeMost.get(1)] == 2/3*likeMostInitCount)
			whatToEatNow = whatILikeMost.get(whatILikeMost.size()/2+1)+" "+1+"";
		return whatToEatNow;
	}
}


