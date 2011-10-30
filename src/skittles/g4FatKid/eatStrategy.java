package skittles.g4FatKid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

class EatStrategy{
	private int[] aintInHand;
	private int intColorNum;
/*	//double dblHappiness;
	//String strClassName;
	//int intPlayerIndex;*/
	private double approxDist;
	private double[] adblTastes;
	private int intLastEatIndex;
	private int intLastEatNum;
	//private List<Integer> whatILikeMost = new ArrayList<Integer>();
	private int likeMostInitCount = 0;
	private SortedMap<Integer, Double> whatILikeMostScore;
	
	public EatStrategy(int[] aintInHand, int intColorNum, SortedMap<Integer, Double> whatILikeMostScore) {	
		this.aintInHand = aintInHand;
		this.intColorNum = intColorNum;
		this.whatILikeMostScore = whatILikeMostScore;
		//this.intLastEatIndex = intLastEatIndex;
		approxDist = 1/aintInHand.length;
		likeMostInitCount = aintInHand[0];
	}
	
	public String eatNow(int intLastEatIndex){
		String whatToEatNow = "";
		int i = 1;
		int max = 0;
		this.intLastEatIndex = intLastEatIndex;
		if(intLastEatIndex == 0)
			for(int j=0;j<aintInHand.length;j++){
				if(aintInHand[j] > max)
					max = j;
				whatToEatNow = (max)+" "+1+"";
				intLastEatIndex = max;
			}
		if (intLastEatIndex+1 < aintInHand.length){
			whatToEatNow = (intLastEatIndex+1) + " " + 1 + "";
			intLastEatIndex += 1;
		}
		else if(aintInHand[Integer.parseInt(whatILikeMostScore.get(1)+"")] == 2/3*(Double.parseDouble(likeMostInitCount+"")))
			if(whatILikeMostScore.get(whatILikeMostScore.size()/2-1) > 0)
				whatToEatNow = whatILikeMostScore.get(whatILikeMostScore.size()/2-1)+" "+1+"";
			else{
				while(!(whatILikeMostScore.get((whatILikeMostScore.size()/2-1)+i) > 0))
					i++;
			}
		whatToEatNow = whatILikeMostScore.get((whatILikeMostScore.size()/2-1)+i)+" "+1+"";
		return whatToEatNow;
	}
}


