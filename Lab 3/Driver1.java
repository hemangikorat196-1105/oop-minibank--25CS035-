public class Driver1 {
    public static void main(String[] args) {

        Card[] cards = {
            new Card("Ace", "Spades"),
            new Card("King", "Hearts"),
            new Card("Queen", "Diamonds"),
            new Card("Ace", "Spades"),   // Duplicate
            new Card("Jack", "Clubs")
        };

        boolean duplicateFound = false;

        for (int i = 0; i < cards.length; i++) {

            for (int j = 0; j < i; j++) {

                if (cards[i].equals(cards[j])) {
                    System.out.println("Duplicate found: " + cards[i]);
                    duplicateFound = true;
                    break;
                }
            }

            if (duplicateFound)
                break;
        }
    }
}