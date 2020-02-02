package blackjack;

public class BlackJack {
	
	private static int BLACKJACK = 21;
	private static double bet;
	
	public static void main(String[] args) {

		// Welcome to BlackJack message
		ConsoleView.welcome();

        // Main deck where both dealers and players card are drawn from
		Deck deck = new Deck("Main Deck");
		// Create a deck with 3 full sets of playing cards
		deck.createDeck(3);	
		// Dealers hand
		Deck dealersHand = new Deck("Dealer's Hand");
		
		// Create player and initialise starting balance
		Player player = new Player(100);	
		//Scanner input = new Scanner(System.in);

		// Continue to loop as long as player has money
		while(player.getBalance() > 0) {	
			player.setEndRound(false);

			// Randomise the order of the cards
			deck.shuffleCards();
			
			//System.out.println("\nYour Balance: " + df2.format(balance));
			ConsoleView.displayBalance(player.getBalance());
			// Players Bet
			bet = ConsoleView.enterBet();
		//	bet = input.nextDouble();					
			player.makeBet(bet);

			// Deal the player 2 cards
			player.addCardMainHand(deck);
			player.addCardMainHand(deck);
			// Deal the dealer 2 cards
			dealersHand.addCard(deck.dealCard());
			dealersHand.addCard(deck.dealCard());
			
			if (player.getPlayerHand().getHandValue() == BLACKJACK) {
				player.getPlayerHand().displayHand();
				ConsoleView.blackJackMessage(bet);
				// BlackJack payout is 3:2
				player.setBalance(player.getBalance() + (bet * 5/2));
			}
			else {
				
				// Keeps looping until all player decisions are competed
				player.decisionLoop(player.getPlayerHand(), player, deck, dealersHand.getCard(0).cardAsString(), ConsoleView.getInput());
		
				// Display Dealers full hand
				ConsoleView.displayDealersFullHand(dealersHand);
				
				boolean endRound = player.getEndRound();
				int dealerHandValue = dealersHand.getHandValue();
				int highestPlrScore = player.getHighestScore();
				
				// Dealer continues to draw until they have a total of 17 or higher unless dealer already has highest score
				if (endRound == false && dealerHandValue <= highestPlrScore){		
					while((dealersHand.getHandValue() < 17 || dealerHandValue < highestPlrScore)) {
						dealersHand.addCard(deck.dealCard());
						// Displays the card the dealer drew
						ConsoleView.displayDealersCardDrawn(dealersHand.getLastCard());
						// Update the dealers hand value
						dealerHandValue = dealersHand.getHandValue();
					}			
					// Check if Dealer Bust
					if (dealersHand.getHandValue() > BLACKJACK && endRound == false) {
						ConsoleView.DealerBustMessage();
					}	
				}
				
				player.determineWinners(dealersHand);
				player.displayResults(dealersHand);
			}
			
			// Move cards back into main deck
			player.emptyAllHands(deck);
			deck.emptyFromDeck(dealersHand);
			player.playerReset();
			
			// Advances this scanner past the current line
			ConsoleView.nextLine();
		}		
		// Game Over
		ConsoleView.GameOverMessage();
		//System.out.println("Game Over!");
	}
}


