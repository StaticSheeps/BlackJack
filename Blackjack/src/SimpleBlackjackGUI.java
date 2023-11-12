import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleBlackjackGUI extends JFrame {

    private List<Card> deck;
    private List<Card> playerHand;
    private List<Card> dealerHand;

    private JLabel playerLabel;
    private JLabel dealerLabel;
    private JButton dealButton;
    private JButton hitButton;
    private JButton standButton;

    public SimpleBlackjackGUI() {
        initializeDeck();
        initializeUI();
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        playerHand = new ArrayList<>();
        dealerHand = new ArrayList<>();

        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};

        for (String suit : suits) {
            for (String rank : ranks) {
                Card card = new Card(rank, suit);
                deck.add(card);
            }
        }

        Collections.shuffle(deck);
    }

    private void initializeUI() {
        setTitle("Simple Blackjack");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        playerLabel = new JLabel("Player Hand: ");
        dealerLabel = new JLabel("Dealer Hand: ");
        dealButton = new JButton("Deal");
        hitButton = new JButton("Hit");
        standButton = new JButton("Stand");

        dealButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deal();
                updateUI();
            }
        });

        hitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hit();
                updateUI();
            }
        });

        standButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stand();
                updateUI();
            }
        });

        setLayout(new FlowLayout());
        add(playerLabel);
        add(dealerLabel);
        add(dealButton);
        add(hitButton);
        add(standButton);

        updateUI();
    }

    private void deal() {
        playerHand.clear();
        dealerHand.clear();

        for (int i = 0; i < 2; i++) {
            playerHand.add(deck.remove(0));
            dealerHand.add(deck.remove(0));
        }
    }

    private void hit() {
        playerHand.add(deck.remove(0));

        if (calculateHandValue(playerHand) > 21) {
            JOptionPane.showMessageDialog(this, "Bust! You lose.");
            deal();
        }
    }

    private void stand() {
        while (calculateHandValue(dealerHand) < 17) {
            dealerHand.add(deck.remove(0));
        }

        int playerValue = calculateHandValue(playerHand);
        int dealerValue = calculateHandValue(dealerHand);

        if (dealerValue > 21 || playerValue > dealerValue) {
            JOptionPane.showMessageDialog(this, "You win!");
        } else if (playerValue < dealerValue) {
            JOptionPane.showMessageDialog(this, "You lose.");
        } else {
            JOptionPane.showMessageDialog(this, "It's a tie!");
        }

        deal();
    }

    private int calculateHandValue(List<Card> hand) {
        int value = 0;
        int numAces = 0;

        for (Card card : hand) {
            if (card.getRank().equals("Ace")) {
                value += 11;
                numAces++;
            } else if (card.getRank().equals("King") || card.getRank().equals("Queen") || card.getRank().equals("Jack")) {
                value += 10;
            } else {
                value += Integer.parseInt(card.getRank());
            }
        }

        while (value > 21 && numAces > 0) {
            value -= 10;
            numAces--;
        }

        return value;
    }

    private void updateUI() {
        playerLabel.setText("Player Hand: " + handToString(playerHand));
        if (!dealerHand.isEmpty()) {
            dealerLabel.setText("Dealer Hand: " + handToString(dealerHand.subList(0, Math.min(1, dealerHand.size()))));
        }

        hitButton.setEnabled(!playerHand.isEmpty() && calculateHandValue(playerHand) < 21);
        standButton.setEnabled(!playerHand.isEmpty() && calculateHandValue(playerHand) < 21);
    }

    private String handToString(List<Card> hand) {
        StringBuilder sb = new StringBuilder();
        for (Card card : hand) {
            sb.append(card.toString()).append(", ");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleBlackjackGUI().setVisible(true);
            }
        });
    }
}

class Card {
    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
