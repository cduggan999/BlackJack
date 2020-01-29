package blackjack;

import java.util.Scanner;
import java.util.Stack;

public class Player {
	
	private double balance = 0.00;
	private boolean endRound = false;
	private int BLACKJACK = 21;
	private int splitHandCount = 0;
	
	//Creates a player object
	public Player(double bal) {
		this.balance = bal;
	}
	
	// Players hand
	private Deck playersHand = new Deck("Your Hand"); 
	
	private Stack<Deck> splitHands = new Stack<Deck>();
	//Deck[] splitHands;
	
	public double getBalance( ) {
		return this.balance;
	}
	
	public void setBalance(double newBalance) {
		this.balance = newBalance;
	}
	
	public Deck getPlayerHand() {
		return this.playersHand;
	}
	
	public void setEndRound(boolean end) {
		this.endRound = end;
	}
	
	public boolean getEndRound() {
		//Check the status of all players hand to determine endRound status
		if (playersHand.getEndTurn() == false) { 
			this.endRound = false;
			return this.endRound; 
		}	
		if (this.splitHands.empty() == false) {
			for(Deck currentDeck : this.splitHands) {
				// Check statuses of all split hands
				if (currentDeck.getEndTurn() == false) {
					this.endRound = false;
					return this.endRound; 
				}		
			}
		}
		// If we get to this point all endRound statuses for each hand must be true
		this.endRound = true;
		return this.endRound; 
	}
	
	public void addCardMainHand(Deck deck) {
		playersHand.addCard(deck.dealCard());
	}
	public void setBet(double bet) {
		// Sets the bet against our primary hand
		playersHand.setBet(bet);
	}
	
	// Returns all cards from all the players hands to main deck 
	public void emptyAllHands(Deck deckReceive) {
		// Return Cards from main hand
		deckReceive.emptyFromDeck(this.playersHand);
		// Return cards from any split hands
		if (this.splitHands.empty() == false) {
			for(Deck currentDeck : this.splitHands) {
				// Check statuses of all split hands
				if (currentDeck.getDeckSize() > 0) {
					deckReceive.emptyFromDeck(currentDeck);
				}		
			}
		}
	}
	
	public boolean canSplit(Deck hand) {
		// Required conditions to allow hand to split
		if (hand.getDeckSize() == 2 && hand.getCard(0).getRank().equals(hand.getCard(1).getRank()) && (splitHands == null || splitHands.size() < 3)) {
			return true;	
		}
		else {
			return false; 
		}
	}
	
	public int splitHand(Deck originalHand) {
		// Push new Deck item to Deck Stack
		this.splitHands.add(new Deck("Split Hand " + (splitHandCount+1)));		
		// Moves card from original hand to new split hand
		System.out.println("\nSplitting " + originalHand.getCard(0).cardAsString() 
				+ ", " + originalHand.getCard(1).cardAsString() + " into 2 new hands!");
		splitHands.lastElement().addCard(originalHand.dealCard());	
		// Also return index of new Hand
		return this.splitHands.indexOf(splitHands.lastElement());
	}

	public void displaySplitHands() {
		if (this.splitHands.empty() == false) {
			for(Deck currentDeck : this.splitHands) {
				// Check statuses of all split hands
				if (currentDeck.getDeckSize() > 0) {
					currentDeck.displayHand();
				}		
			}
		}
	}
	
	public void decisionLoop(Deck playersHand, Player player, Deck deck, String dealersCardVisible, Scanner input) {
		while (true) {
			
			if(playersHand.getEndTurn() == true) {break;}
			
			int response;
		
			System.out.println(playersHand.getName() + ":");
			playersHand.displayHand();
			System.out.println("[Score: " + playersHand.getHandValue() + "]");

			System.out.println("\nDealers Hand:");
			System.out.println(dealersCardVisible);
			System.out.println("[HIDDEN]");

			System.out.println("\nWould you like to:");
			// Double Down should only be available if there is enough balance to make another bet
			if (player.balance >= playersHand.getBet() && canSplit(playersHand) == true) {
				System.out.println("(1)HIT (2)STAND (3)DOUBLE DOWN, (4)SPLIT");
			}
			else if (player.balance >= playersHand.getBet() && canSplit(playersHand) == false) {
				System.out.println("(1)HIT (2)STAND (3)DOUBLE DOWN");
			}
			else {
				System.out.println("(1)HIT (2)STAND");
			}

			// Get Response
			response = input.nextInt();

			// Player chose to Hit
			if (response == 1) {
				// Add Card to player deck from main deck
				playersHand.addCard(deck.dealCard());
				// Display the last card added to your hand
				System.out.println("You drew a " + playersHand.getLastCard().cardAsString());	
				// If Player goes over 21
				if (playersHand.getHandValue() > BLACKJACK) {
					playersHand.setResult(Result.Bust);
					playersHand.setEndTurn(true);
					break;
				}					
			}
			else if (response == 2) {
				break;
			}
			// Double the Bet, draw 1 more card
			else if (response == 3 && player.balance >= playersHand.getBet()) {		
				player.setBalance(player.getBalance() -playersHand.getBet());
				playersHand.setBet(playersHand.getBet() * 2);
				System.out.println("Your bet has doubled to " + playersHand.getBet());
				playersHand.addCard(deck.dealCard());
				// Display the last card added to your hand
				System.out.println("You drew a " + playersHand.getLastCard().cardAsString());	
				// If Player goes over 21
				if (playersHand.getHandValue() > BLACKJACK) {
					playersHand.setResult(Result.Bust);
					playersHand.setEndTurn(true);					
				}	
				break;
			}
			else if (response == 4 && playersHand.getCard(0).getRank() == playersHand.getCard(1).getRank() 
					&& player.getBalance() >= playersHand.getBet() && canSplit(playersHand) == true) {
				
				player.setBalance(player.getBalance() - playersHand.getBet());
				// Split Hand
				player.splitHand(playersHand);
				// Draw Additional Card for each hand
				playersHand.addCard(deck.dealCard());
				splitHands.lastElement().addCard(deck.dealCard());
				// Apply the bet to new Split Hand
				splitHands.lastElement().setBet(playersHand.getBet());
				// playLoop(orig hand)
				decisionLoop(player.playersHand, player, deck, dealersCardVisible, input);
				player.playersHand.setEndTurn(true);
				// playloop(split hand)
				decisionLoop(splitHands.lastElement(), player, deck, dealersCardVisible, input);
				splitHands.lastElement().setEndTurn(true);
				//break;
			}
		}
	}
	
	public int getHighestScore() {
		// Returns the highest score (apart from blackjack) from all player hands
		int highest = 0;
		int currValue = this.playersHand.getHandValue();
		
		if (currValue < 21) {
			highest = this.playersHand.getHandValue();
		}
		if (this.splitHands.empty() == false) {
			for(Deck currentDeck : this.splitHands) {
				currValue = currentDeck.getHandValue();
				if (currValue > highest && currValue < 21) {
					highest = currValue;
				}
			}
		}
		return highest;	
	}
	
	public void displayResults(Deck dealersHand) {
		// Prints the Results of your main hands
		displayHandResult(this.playersHand, dealersHand);	
		// Prints the Results of the split hands
		if (this.splitHands.empty() == false) {
			for(Deck currentDeck : this.splitHands) {
				displayHandResult(currentDeck, dealersHand);		
			}
		}
	}
	
	public void determineWinners(Deck dealersHand) {
		// Compare main players hand to dealer hand
		determineHandResult(playersHand, dealersHand);
		// Compares each split hand to dealer hand
		if (this.splitHands.empty() == false) {
			for(Deck currentDeck : this.splitHands) {
				// Skip function call if Hand is already Bust
				if (currentDeck.getResult() != Result.Bust) {
					determineHandResult(currentDeck, dealersHand);	
				}
			}
		}
	}
	
	private void determineHandResult(Deck playersHand, Deck dealersHand) {
		// Player has BlackJack
		if (playersHand.getHandValue() == BLACKJACK) {
			playersHand.setResult(Result.Blackjack);
			this.balance += (playersHand.getBet() * 5/2);

		}
		// Player is Bust, over 21
		else if (playersHand.getHandValue() > BLACKJACK) {
			playersHand.setResult(Result.Bust);

		}
		// Dealer has bust
		else if (dealersHand.getHandValue() > BLACKJACK) {
			playersHand.setResult(Result.DealerBust);
			this.balance += (playersHand.getBet() * 2);
		}
		// Dealer Wins
		else if (dealersHand.getHandValue() > playersHand.getHandValue()) {
			playersHand.setResult(Result.Lose);

		}
		// Player Wins
		else if (playersHand.getHandValue() > dealersHand.getHandValue()) {
			playersHand.setResult(Result.Win);
			this.balance += (playersHand.getBet() * 2);
		}
		// Draw
		else {
			playersHand.setResult(Result.Push);
			this.balance += playersHand.getBet();
		}
	}
	
	private void displayHandResult(Deck playerHand, Deck dealersHand) {
		System.out.println("\n[" + playerHand.getName() + " Score: " + playerHand.getHandValue() + "]");
		System.out.println("[Dealer HandScore: " + dealersHand.getHandValue() + "]");
		System.out.println(playerHand.getResultMesage());
	}
	
	public void playerReset() {
		this.endRound = false;
		this.playersHand.setEndTurn(false);
		this.splitHandCount = 0;
		if (this.splitHands.empty() == false) {
			this.splitHands.clear();
		}
	}
}
