package blackjack;

public class Card {
	
	private final Rank rank;
	private final Suit suit;
	
	//Creates a card object
	public Card(Rank rank, Suit suit) {
		this.rank = rank;
		this.suit = suit;
	}
	//returns the rank and suit of the card
	public Rank getRank() { return rank; }
	public Suit getSuit() { return suit; }
	
	//Prints the rank and suit of the card
	public String cardAsString() { return this.rank.getRankName() + " of " + this.suit.toString(); }
	
	//Returns the value of the card, e.g Two = 2, Jack = 11, Ace = 14 etc
	public int getRankValue() {
		int val = this.getRank().ordinal()+1;
		// Jacks Queens and Kings should also be 10
		if (val > 10) val = 10; 
		return val;
	}
}