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
	SortedMap<Integer, Double> whatILikeMostScore;
	PreferredColors prefs;
	List<Integer> ateAlready;
	int i=1;
	int initSkittleNum = 0;
	int skittleNum = 0;
	
	public EatStrategy(int[] aintInHand, int intColorNum, PreferredColors prefs) {	
		this.aintInHand = aintInHand;
		this.intColorNum = intColorNum;
		//this.whatILikeMostScore = whatILikeMostScore;
		intLastEatIndex = 0;
		approxDist = 1/aintInHand.length;
		likeMostInitCount = aintInHand[0];
		this.prefs = prefs;
		ateAlready = new ArrayList<Integer>();
		for(int j=0;j<aintInHand.length;j++)
			initSkittleNum += aintInHand[j];
		skittleNum = initSkittleNum;
	}

	public void updatePrefs(PreferredColors prefs){
		this.prefs = prefs;
	}
	
	public String eatNow(){
		String whatToEatNow = "";
		int max = 0;
		int j;
		
		if(i != aintInHand.length){
			for(j=0;j<aintInHand.length;j++)
				if(aintInHand[j]>max && !ateAlready.contains(j)){
					max = j;
					ateAlready.add(max);
				}
			intLastEatIndex = max;
			whatToEatNow = intLastEatIndex+" "+1;
			i++;
			skittleNum--;
		}
<<<<<<< HEAD
		else if(!(skittleNum == initSkittleNum)){
			intLastEatIndex = prefs.getMedian();
			while(prefs.returnTastes(intLastEatIndex) < 0)
				intLastEatIndex = prefs.getMedian()+1;
=======
		else{
			intLastEatIndex = prefs.getLowestRankedColor();
			//if(prefs.)
>>>>>>> 72ad54e7331f2bbb8c6176053cd106b3c5ae726a
			whatToEatNow = intLastEatIndex+" "+1;
			skittleNum--;
		}
		else{
			for(j=0;j<aintInHand.length;j++)
				if(aintInHand[j] !=0){
					intLastEatIndex = j;
				}
			whatToEatNow = intLastEatIndex+" "+(aintInHand[intLastEatIndex]);
		}
		
		return whatToEatNow;
	}
}


