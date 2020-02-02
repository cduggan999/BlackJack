package blackjack;

import java.text.NumberFormat;
import java.util.Scanner;

public final class ConsoleView {
	
	private static NumberFormat df2 = NumberFormat.getCurrencyInstance();
	private static Scanner input = new Scanner(System.in);
	
	//private double balance = 0.00;
	
	//Creates a player object
		private ConsoleView() {
			//this.balance = bal;
		}
		
		// Welcome to BlackJack message
		public static void welcome() {
				System.out.println("-------------------------------------------------------"); 
		        System.out.println("-              Welcome to BLACK JACK                  -");
		        System.out.println("-------------------------------------------------------"); 
		}
		
		public static void displayBalance(double bal) {
			System.out.println("\nYour Balance: " + df2.format(bal));
		}
		
		public static void insufficientBalance(double bal) {		
			System.out.println("\nInsufficient balance, betting " + bal + " instead!");	
		}
		
		public static double enterBet() {
			System.out.println("\nEnter the amount you wish to bet:");
			return input.nextDouble();
		}
		
		public static void blackJackMessage(double bet) {
			System.out.println("\nBlack Jack! Congratulations!");
			System.out.println("You won " + df2.format(bet * 3 / 2));
		}
		
		public static void displayDealersFullHand(Deck hand) {
			// Display Dealers full hand
			System.out.println("\nDealers Hand:"); 
			hand.displayHand();
		}
		
		public static void displayDealersCardDrawn(Card card) {
			// Displays the card the dealer drew
			System.out.println("\nDealer drew a " + card.cardAsString());
		}
		
		public static void DealerBustMessage() {
			System.out.println("\nDealer Bust!");
		}
		
		public static void GameOverMessage() {
			System.out.println("Game Over!");
		}
		
		public static Scanner getInput() {
			return input;
		}
		
		public static void nextLine() {
			// Advances this scanner past the current line
			input.nextLine();
		}
		
		public static void displayCard(Card card) {
			// Displays the passed in card to the console
			System.out.println(card.cardAsString());
		}
		
		public static void displayHandResult(Deck playerHand, Deck dealersHand) {
			System.out.println("\n[" + playerHand.getName() + " Score: " + playerHand.getHandValue() + "]");
			System.out.println("[Dealer HandScore: " + dealersHand.getHandValue() + "]");
			System.out.println(playerHand.getResultMesage());
		}	
		
}
