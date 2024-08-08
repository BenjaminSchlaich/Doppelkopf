package game;

import java.util.LinkedList;

import game.gamemodes.ACardOrder;

public class Hand {
    
    public Hand(CardStack s)
    {
        cards = new LinkedList<>();

        for(int i=0; i<12; i++)
            cards.push(s.draw());
    }

    @SuppressWarnings("unchecked")
    public LinkedList<Card> getCards()
    {
        return (LinkedList<Card>) cards.clone();
    }

    public void sortCards(ACardOrder aco)
    {
        cards.sort(aco);
    }

    public String toString() {

        String s = "";

        for(Card c: cards)
            s += c.toString() + "\n";

        return s;
    }

    private LinkedList<Card> cards;
}
