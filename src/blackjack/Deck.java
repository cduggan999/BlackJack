package blackjack;

import java.util.Collections;
import java.util.Stack;
import java.text.NumberFormat;

public class Deck {
	
	// Creates a stack to hold our 52 cards
	private Stack<Card> deckOfCards;
	
	private double bet = 0.00;
	private String name;
	
	// Used to record whether the hand wins, loses or draws(push)
	private Result result = null; 
	private boolean endTurn = false; 
	private NumberFormat df2 = NumberFormat.getCurrencyInstance();
	
	public Deck(String name){
		this.deckOfCards = new Stack<Card>();
		this.name = name;
	}
	
	public void createDeck(int numDecks){
		this.deckOfCards.clear();
		
		// Iterate through each deck
		for(int i = 0; i < numDecks; i++ ) {
			// Iterates through every suit and rank
			for( Suit suitIndex : Suit.values() )
			{   
				for( Rank rankIndex : Rank.values() )
	            {
	            	//adds all 52 cards to the stack 
	            	this.deckOfCards.add(new Card(rankIndex, suitIndex));
	            }
			}
		}
		
	}
	
	public void shuffleCards(){
		Collections.shuffle(this.deckOfCards );
	}
	
	//Returns a card and removes it from the deck 
	public Card dealCard(){
		return this.deckOfCards.pop();
	}
	
	public Card getCard(int index){
		return this.deckOfCards.get(index);
	}
	
	public int getDeckSize(){
		return this.deckOfCards.size();
	}
	
	public void removeCard(Card card){
		this.deckOfCards.remove(card);
	}
	
	public void addCard(Card card){
		this.deckOfCards.add(card);
	}
	// Empties the contents of a deck to another
	public void emptyFromDeck(Deck deckGive) {
		while (deckGive.getDeckSize() > 0) {
			this.deckOfCards.add(deckGive.dealCard());
		}
	}
	
	public int getHandValue(){
		int totalScore = 0;
		int aceCounter = 0;
		for(Card currentCard : this.deckOfCards) {
			// If an ace increment aceCounter
			if (currentCard.getRankValue() == 1) {
				aceCounter++;
			}
			// Otherwise add value to score
			else {
				totalScore += currentCard.getRankValue();
			}
		}
		
		for(int i = 0; i < aceCounter; i++) {
			if(totalScore > 10) {
				// We want ace to be one to avoid bust
				totalScore += 1;
			}
			else {
				totalScore += 11;
			}
		}
		
		return totalScore;
	}
	public void displayHand() {
		for (int i = 0; i < getDeckSize(); i++) {
			ConsoleView.displayCard(getCard(i));
		}
	}
	public Card getLastCard() {
		return getCard(getDeckSize() -1);
	}
	
	public void setBet(double bet) {
		this.bet = bet;
	}
	
	public double getBet() {
		return this.bet;
	}
	
	public Result getResult() {
		return this.result;
	}
	
	public void setResult(Result res) {
		this.result = res;
	}
	 
	public String getResultMesage() {
		if (this.result == Result.Blackjack) {
			return "\nBlackjack! For " + this.name + "! " + this.name + " won " + (df2.format(bet * 3 / 2)).toString();
		}
		if (this.result == Result.Bust) {
			return "\nBUST! " + this.name + " is values at " + this.getHandValue() + "!";
		}
		else if (this.result == Result.Lose) {
			return "\n" + this.name + " Lost! Dealer Wins this Hand!";
		}
		else if (this.result == Result.Win) {	
			return "\n" + this.name + " won " + df2.format(getBet()).toString() + "!";
		}
		else if (this.result == Result.Push) {
			return "\nPush! " + this.name + " ties with dealers's hand!";
		}
		else if (this.result == Result.DealerBust) {
			return "\nDealer Bust! " + this.name + " won " + df2.format(getBet()).toString() + "!";
		}
		return null;
	}
	
	public void setEndTurn(boolean end) {
		this.endTurn = end;
	}
	
	public boolean getEndTurn() {
		return this.endTurn;
	}
	
	public String getName() {
		return this.name;
	}
	
}
