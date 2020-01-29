package blackjack;

public enum Rank {
	Ace("Ace"), Two("2"), Three("3"), Four("4"), Five("5"), Six("6"), Seven("7"), Eight("8"),
	Nine("9"), Ten("Ten"), Jack("Jack"), Queen("Queen"), King("King");
	private String rankName;
	private Rank(String stringRank) {
		rankName = stringRank;
	}
	public String getRankName() {
		return rankName;
	}
}