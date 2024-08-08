
package game.gamemodes;

import java.util.Comparator;
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
    }

    @Override
    public abstract int compare(Card o1, Card o2);

    protected abstract boolean isTrump(Card c);

    protected Color roundColor = null;

}
