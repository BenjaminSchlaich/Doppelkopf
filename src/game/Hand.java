package game;

import java.util.LinkedList;

import game.gamemodes.ACardOrder;
import game.gamemodes.NormalOrder;

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

    public boolean qualifiesArmut()
    {
        // 5 or more neunen
        if(cards.stream().filter((Card c) -> c.v == Value.Neun).count() >= 5)
            return true;
        
        ACardOrder order = new NormalOrder();

        // 2 or less trumps
        if(cards.stream().filter((Card c) -> order.isTrump(c)).count() <= 2)
            return true;
        
        sortCards(order);

        // highest trump <= karo bube
        if(order.compare(new Card(Value.Bube, Color.Karo), cards.getLast()) == 1)
            return true;
        
        return false;
    }

    private LinkedList<Card> cards;
}
