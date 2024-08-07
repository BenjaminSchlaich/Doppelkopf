
package game.gamemodes;

import java.util.Comparator;

import game.Card;

/**
 * This class provides a Order for the Cards registered in a match.
 * The match is used to figure out the opening card for each round.
 * Subclasses represent the orderings in the various game modes.
 * Starting with the first round, each Match has one CardOrder associated with it.
 */
public abstract class ACardOrder implements Comparator<Card> {

    public void receiveRoundOpening(Card c)
    {
        lastRoundOpening = c;

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
        for(int i=0; i<order.length; i++)
            for(int j=0; j<order[i].length; j++)
                if(order[i][j] == c)
                    return i;
        
        return -1;
    }

    protected abstract void computeOrder();

    protected Card lastRoundOpening;

    protected static Card[][] order;
    
}
