
package game.gamemodes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import game.Card;
import game.Color;

/**
 * This class provides a Order for the Cards registered in a match.
 * The match is used to figure out the opening card for each round.
 * Subclasses represent the orderings in the various game modes.
 * Starting with the first round, each Match has one CardOrder associated with it.
 */
public abstract class ACardOrder implements Comparator<Card> {

    public void receiveRoundOpening(Card c)
    {
        if(isTrump(c))
            roundColor = null;
        else
            roundColor = c.c;

        computeOrder();
    }

    @Override
    public int compare(Card o1, Card o2)
    {
        if(findOrdinal(o1) <= findOrdinal(o2))
            return 1;
        else
            return -1;
    }

    private int findOrdinal(Card c)
    {
        for(int i=0; i<order.size(); i++)
            if(order.get(i).contains(c))
                return i;
        
        return -1;
    }

    protected abstract boolean isTrump(Card c);

    protected abstract void computeOrder();

    protected Color roundColor = null;

    protected ArrayList<Set<Card>> order;
    
}
