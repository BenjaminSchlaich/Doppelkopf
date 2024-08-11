package game.gamemodes;

import game.Card;
import game.Value;

public class BubensoloOrder extends ACardOrder {

    @Override
    public int compare(Card o1, Card o2) {
        
        if(o1.v == Value.Bube)
            if(o2.v == Value.Bube)
                return colorCompare(o1, o2);
            else
                return 1;
        else if(o2.v == Value.Bube)
            return -1;
        else if(o1.c == roundColor)
            if(o2.c == roundColor)
                return valueCompare(o1, o2);
            else
                return 1;
        else if(o2.c == roundColor)
            return -1;
        else
            return 2*colorCompare(o1, o2) + valueCompare(o1, o2);        // both Fehl
    }

    @Override
    public boolean isTrump(Card c) {
        return c.v == Value.Bube;
    }
    
}
