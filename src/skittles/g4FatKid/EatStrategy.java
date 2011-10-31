package skittles.g4FatKid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

class EatStrategy{
	private int[] aintInHand;
	private int intColorNum;

	private int intLastEatIndex;

	PreferredColors prefs;
	List<Integer> ateAlready;
	int i = 1;
	double initSkittleNum = 0;
	double skittleNum = 0;

	public EatStrategy(int[] aintInHand, int intColorNum, PreferredColors prefs) {	
		this.aintInHand = aintInHand;
		this.intColorNum = intColorNum;
		
		intLastEatIndex = 0;

		this.prefs = prefs;
		ateAlready = new ArrayList<Integer>();
		for(int j=0;j<aintInHand.length;j++)
			initSkittleNum += aintInHand[j];
		skittleNum = initSkittleNum;
	}

	public void updatePrefs(PreferredColors prefs){
		this.prefs = prefs;
	}

	public int[] eatNow() {
		int[] whatToEatNow = new int[2];
		int max = 0;
		int j;
		int min = 100;

		//Rounds to taste each of the skittles to check if we like them
		if (i != aintInHand.length){
			for (j = 0; j < aintInHand.length; j++) {
				if (aintInHand[j] > max && !ateAlready.contains(j)) {
					if (aintInHand[j] != 0) {
						max = j;
						ateAlready.add(max);
					}
				}
			}
			intLastEatIndex = max;
			whatToEatNow[0] = intLastEatIndex;
			whatToEatNow[1] = 1;
			i++;
			skittleNum--;
		}
		
		else if(i <= 2*initSkittleNum) {
			// eat the lower colors one by one in the meanwhile we hoarde the color we want
			intLastEatIndex = prefs.getLowestRankedColor();
			while(aintInHand[intLastEatIndex] == 0) {
				intLastEatIndex += 1;
				if(intLastEatIndex == aintInHand.length-1)
					intLastEatIndex = 0;
			}	
			whatToEatNow[0] = intLastEatIndex;
			whatToEatNow[1] = 1;
		}
		else {
			// after we're finished hoarding just eat all of one color together
			for (j = 0; j < aintInHand.length; j++) {
				if (aintInHand[j] < min)
					if (aintInHand[j] != 0)
						min = j;
			}
			intLastEatIndex = min;
			whatToEatNow[0] = intLastEatIndex;
			whatToEatNow[1] = aintInHand[intLastEatIndex];
			i++;
		}
	
		return whatToEatNow;
	}
}
