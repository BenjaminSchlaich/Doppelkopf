package game;

import java.util.LinkedList;

public class Hand {
    
    public Hand(CardStack s)
    {
        cards = new LinkedList<>();

        for(int i=0; i<12; i++)
            cards.push(s.draw());
    }

    public LinkedList<Card> getCards()
    {
        return cards;
    }

    private LinkedList<Card> cards;
}
