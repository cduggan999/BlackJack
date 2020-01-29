package blackjack;

import java.util.Scanner;
import java.text.NumberFormat;

public class BlackJack {
	
	private static int BLACKJACK = 21;
	private static NumberFormat df2 = NumberFormat.getCurrencyInstance();
	private static double bet;
	private static double balance;
	
	public static void main(String[] args) {

		//double bet;

		// Welcome to BlackJack message
		System.out.println("-------------------------------------------------------"); 
        System.out.println("-              Welcome to BLACK JACK                  -");
        System.out.println("-------------------------------------------------------"); 
        
      //  NumberFormat df2 = NumberFormat.getCurrencyInstance();

        // Main deck where both dealers and players card are drawn from
		Deck deck = new Deck("Main Deck");
		// Create a deck with 3 full sets of playing cards
		deck.createDeck(3);	
		// Dealers hand
		Deck dealersHand = new Deck("Dealer's Hand");
		
		// Create player and initialise starting balance
		Player player = new Player(100);	
		Scanner input = new Scanner(System.in);

		// Continue to loop as long as player has money
		while(player.getBalance() > 0) {	
			player.setEndRound(false);

			// Randomise the order of the cards
			deck.shuffleCards();
			
			balance = player.getBalance();
			System.out.println("\nYour Balance: " + df2.format(balance));
			// Players Bet
			System.out.println("\nEnter the amount you wish to bet:");
			bet = input.nextDouble();
			
			
			// Checks player has enough to cover bet
			if (balance >= bet) {
				balance -= bet;
				player.setBalance(balance);
				player.setBet(bet);
			} 
			else {
				System.out.println("\nInsufficient balance, betting " + balance + " instead!");
				player.setBet(balance);
				balance = 0;
				player.setBalance(balance);
			}
			// Deal the player 2 cards
			player.addCardMainHand(deck);
			player.addCardMainHand(deck);
			// Deal the dealer 2 cards
			dealersHand.addCard(deck.dealCard());
			dealersHand.addCard(deck.dealCard());
			
			if (player.getPlayerHand().getHandValue() == BLACKJACK) {
				player.getPlayerHand().displayHand();
				System.out.println("\nBlack Jack! Congratulations!");
				// BlackJack payout is 3:2
				System.out.println("You won " + (bet * 3 / 2));
				player.setBalance(balance + (bet * 5/2));
			}
			else {
				// Keeps looping until all player decisions are competed
				player.decisionLoop(player.getPlayerHand(), player, deck, dealersHand.getCard(0).cardAsString(), input);
		
				// Display Dealers full hand
				System.out.println("\nDealers Hand:");
				dealersHand.displayHand();
				
				boolean endRound = player.getEndRound();
				int dealerHandValue = dealersHand.getHandValue();
				int highestPlrScore = player.getHighestScore();
				
				// Dealer continues to draw until they have a total of 17 or higher unless dealer already has highest score
				if (endRound == false && dealerHandValue <= highestPlrScore){		
					while((dealersHand.getHandValue() < 17)) {
						dealersHand.addCard(deck.dealCard());
						System.out.println("\nDealer drew a " + dealersHand.getLastCard().cardAsString());
					}			
					// Check if Dealer Bust
					if (dealersHand.getHandValue() > BLACKJACK && endRound == false) {
						//resultMessage = "\nDealer BUST! You WIN";	
						System.out.println("\nDealer Bust!");
					}	
				}
				
				player.determineWinners(dealersHand);
				player.displayResults(dealersHand);
			}
			
			// Move cards back into main deck
			player.emptyAllHands(deck);
			deck.emptyFromDeck(dealersHand);
			player.playerReset();
			
			System.out.println("-------------------------------------------------------"); 
			input.nextLine();
		}		
		// Game Over
		System.out.println("Game Over!");
	}
}


