package skittles.g4FatKid;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;

=======
>>>>>>> Updates to EatStrategy and PreferredColors
class EatStrategy{
	
	private int[] aintInHand;
	private int intColorNum;
	private int intLastEatIndex;
<<<<<<< HEAD
	PreferredColors prefs;
	List<Integer> ateAlready;
	int i=1;
=======
	
	int i = 0;

	PreferredColors prefs;
>>>>>>> Updates to EatStrategy and PreferredColors
	double initSkittleNum = 0;
	double skittleNum = 0;

	public EatStrategy(int[] inHand, int intColorNum, PreferredColors prefs) {	
		
		aintInHand = new int[intColorNum];
		for (int j = 0; j < intColorNum; j++) {
			this.aintInHand[j] = inHand[j];
		}
		this.intColorNum = intColorNum;
	
		intLastEatIndex = 0;
	
		this.prefs = prefs;
		for (int j = 0; j < aintInHand.length; j++)
			initSkittleNum += aintInHand[j];
		skittleNum = initSkittleNum;
	}

	public void updatePrefs(PreferredColors prefs){
		this.prefs = prefs;
	}

<<<<<<< HEAD
	public int[] eatNow(){
		int[] whatToEatNow = new int[aintInHand.length];
		Arrays.fill(whatToEatNow, 0);
		int max = 0;
		int j;
		int min = 100;
		
		//Rounds to taste each of the skittles to check if we like them
		if(i <= aintInHand.length){
			for(j=0;j<aintInHand.length;j++)
				if(aintInHand[j]>max | !ateAlready.contains(j)){
					if(aintInHand[j]!=0){
						max = j;
						ateAlready.add(max);
					}
				}
			intLastEatIndex = max;
			whatToEatNow[intLastEatIndex] = 1;
			i++;
		}
		
		else if(i <= initSkittleNum){
			// eat the lower colors one by one in the meanwhile we hoarde the color we want
			intLastEatIndex = prefs.getLowestRankedColor()+1;
			int k=1;
			while(aintInHand[intLastEatIndex] == 0){
				if(prefs.getColorAtRank(prefs.getLowestRankedColor()+k) < aintInHand.length){
					intLastEatIndex = Math.abs(prefs.getColorAtRank(prefs.getLowestRankedColor()+k));
					k++;
				}
				else break;
			}	
			whatToEatNow[intLastEatIndex] = 1;
			//whatToEatNow[1] = 1;
=======
	public int[] eatNow(int[] inHand) {
		
		for (int j = 0; j < intColorNum; j++) {
			this.aintInHand[j] = inHand[j];
		}
		int[] whatToEatNow = new int[intColorNum];
		int min = Integer.MAX_VALUE;
		int minIndex = 0;

		// Rounds to taste each of the skittles to check if we like them
		// if some preferences are still unknown...
		if (!prefs.allPreferencesKnown()) {
			// find color with smallest amount from the colors we still don't know
			for (int j = 0; j < aintInHand.length; j++) {
				// if taste of color j is unknown...
				if (prefs.returnTaste(j) == -2.0) {
					if (aintInHand[j] < min && aintInHand[j] > 0) {
						min = aintInHand[j];
						minIndex = j;
					}
				}
			}
			// after for loop, minIndex should be the index of the smallest non-zero color
			intLastEatIndex = minIndex;
			// eat one of this min color
			whatToEatNow[intLastEatIndex] = 1;
			skittleNum--;
		}
		
		// for now (for submit) just eat something
		else {
			for (int j = 0; j < intColorNum; j++) {
				if (aintInHand[j] > 0) {
					whatToEatNow[j] = 1;
					break;
				}
			}
		}
		
		// else, all preferences are known, and we can move to phase 2: eat the lower colors one by one
		// this phase goes until only one color is left
		// TODO change to a better criteria for ending phase 2
		/*
		else if (i <= 2*initSkittleNum) {
			intLastEatIndex = prefs.getLowestRankedColor();
			while(aintInHand[intLastEatIndex] == 0) {
				intLastEatIndex += 1;
				if(intLastEatIndex == aintInHand.length-1)
					intLastEatIndex = 0;
			}	
			whatToEatNow[0] = intLastEatIndex;
			whatToEatNow[1] = 1;
>>>>>>> Updates to EatStrategy and PreferredColors
			i++;
		}
		
		else{
			// after we're finished hoarding just eat all of one color together
			for(j=0;j<aintInHand.length;j++)
				if(aintInHand[j]<min)
					if(aintInHand[j]!=0)
						min = j;
			intLastEatIndex = min;
			whatToEatNow[intLastEatIndex] = aintInHand[intLastEatIndex];
			i++;
		}
<<<<<<< HEAD
		
=======
		*/
>>>>>>> Updates to EatStrategy and PreferredColors
		return whatToEatNow;
	}
}
