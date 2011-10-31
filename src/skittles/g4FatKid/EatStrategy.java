package skittles.g4FatKid;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

class EatStrategy {
	/*
	 * //double dblHappiness; //String strClassName; //int intPlayerIndex;
	 */

	private int[] skittleBalanceAray;
	private int intColorNum;
	private double approxDist;
	private int lastEatIndex;
	// private List<Integer> whatILikeMost = new ArrayList<Integer>();
	private int likeMostInitCount = 0;
	SortedMap<Integer, Double> whatILikeMostScore;
	PreferredColors prefs;
	List<Integer> ateAlready;
	int i = 1;
	int initialSkittleCount = 0;
	int currentSkittleCount = 0;
	

	public EatStrategy(int[] aintInHand, int intColorNum, PreferredColors prefs) {
		
		this.skittleBalanceAray = aintInHand;
		this.intColorNum = intColorNum;
		// this.whatILikeMostScore = whatILikeMostScore;
		lastEatIndex = 0;
		approxDist = 1 / aintInHand.length;
		likeMostInitCount = aintInHand[0];
		this.prefs = prefs;
		ateAlready = new ArrayList<Integer>();
		for (int j = 0; j < aintInHand.length; j++)
			initialSkittleCount += aintInHand[j];
		currentSkittleCount = initialSkittleCount;
	}

	public void updatePrefs(PreferredColors prefs) {
		this.prefs = prefs;
	}

	public String eatNow(double[] tasteArray , int[] skittleBalanceAray) {
		String whatToEatNow = "";
		if (isInitialStage(tasteArray)) {
			int max = Integer.MIN_VALUE;
			int maxIndex=-1;
			for (int j = 0; j < skittleBalanceAray.length; j++) {
				if (tasteArray[j] == -2 && skittleBalanceAray[j] > 0) {
					if (skittleBalanceAray[j] > max) {
						max = skittleBalanceAray[j];
						maxIndex=j;
					} 
				}
			}
			lastEatIndex = maxIndex;
			whatToEatNow = lastEatIndex + " " + 1;
			currentSkittleCount--;
		} else if(!isTasteDataComplete(tasteArray)){
			for (int j = 0; j < skittleBalanceAray.length; j++) {
				if (tasteArray[j] == -2 && skittleBalanceAray[j] > 0) {
					lastEatIndex=j;
					break;
				}
			}
			whatToEatNow = lastEatIndex + " " + 1;
			currentSkittleCount--;
		}else if(isFinalStage(skittleBalanceAray)){
			for (int j = 0; j < skittleBalanceAray.length; j++)
				if (skittleBalanceAray[j] != 0) {
					lastEatIndex = j;
					break;
				}
			whatToEatNow = lastEatIndex + " "
					+ (skittleBalanceAray[lastEatIndex]);
		}else{
			lastEatIndex = prefs.getMedianElement(prefs.getMedian());
			while ((prefs.returnTastes(lastEatIndex) < 0 || skittleBalanceAray[lastEatIndex]==0 ) && lastEatIndex<(skittleBalanceAray.length-1))
					lastEatIndex += 1;
			if(lastEatIndex==skittleBalanceAray.length){
				for (int j = 0; j < skittleBalanceAray.length; j++) {
					if (tasteArray[j] == -2 && skittleBalanceAray[j] > 0) {
						lastEatIndex=j;
						break;
					}
				}
			}
			whatToEatNow = lastEatIndex + " " + 1;
			currentSkittleCount--;
		}
		return whatToEatNow;
	}

		/*// int max = 0;
		int j;
		if (i != skittleBalanceAray.length) {
			for (j = 0; j < skittleBalanceAray.length; j++) {
				if (skittleBalanceAray[j] > max && !ateAlready.contains(j)) {
					max = j;
					ateAlready.add(max);
				}
			}
			intLastEatIndex = max;
			whatToEatNow = intLastEatIndex + " " + 1;
			i++;
			skittleNum--;
		} else if (!(skittleNum == initSkittleNum)) {
			intLastEatIndex = prefs.getMedian();
			while (prefs.returnTastes(intLastEatIndex) < 0)
				intLastEatIndex = prefs.getMedian() + 1;
			whatToEatNow = intLastEatIndex + " " + 1;
			System.out.println("eat" + whatToEatNow);
			skittleNum--;
		} else {
			for (j = 0; j < skittleBalanceAray.length; j++)
				if (skittleBalanceAray[j] != 0) {
					intLastEatIndex = j;
				}
			whatToEatNow = intLastEatIndex + " "
					+ (skittleBalanceAray[intLastEatIndex]);
		}

		return whatToEatNow;*/
	

	public boolean isInitialStage(double[] tasteArray) {
		int counter = 0;
		for (int j = 0; j < tasteArray.length; j++) {
			if (tasteArray[j] != -2) {
				counter++;
			}
		}
		if (counter < tasteArray.length / 2)
			return true;
		else
			return false;
	}

	public boolean isTasteDataComplete(double[] tasteArray){
		for (int j = 0; j < tasteArray.length; j++) {
			if(tasteArray[j]==-2 && skittleBalanceAray[j]>0){
				return false;
			}
		}
		return true;
	}
	
	public boolean isFinalStage(int[] skittleBalanceAray){
		int counter=0;
		for(int j=0; j<skittleBalanceAray.length;j++){
			if(skittleBalanceAray[j]==0)
				counter++;
		}
		if(counter==skittleBalanceAray.length-1)
			return true;
		else
			return false;
		
	}
}
