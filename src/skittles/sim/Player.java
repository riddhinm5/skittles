package skittles.sim;

public abstract class Player 
{
	public abstract void eat(int[] aintTempEat);
	public abstract void offer( Offer offTemp );
	public abstract void syncInHand( int[] aintInHand );		// this method is for debugging mode. the simulator will call this method to let the player know what is aintInHand on server. the player can use this method to make sure local aintInHand is synced as on simulator
	public abstract void happier( double dblHappinessUp );
	public abstract Offer pickOffer(final Offer[] aoffCurrentOffers);
	public abstract void offerExecuted(final Offer offPicked);
	public abstract void updateOfferExe(final Offer[] aoffCurrentOffers);
	public abstract void initialize(int intPlayerNum, double dblTasteMean, int intPlayerIndex, String strClassName, int[] aintInHand);
	public abstract String getClassName();
	public abstract int getPlayerIndex();
}
