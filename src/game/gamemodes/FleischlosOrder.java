package game.gamemodes;

import game.Card;

public class FleischlosOrder extends ACardOrder {

    @Override
    public int compare(Card o1, Card o2) {
        
        if(o1.c == roundColor)
            if(o2.c == roundColor)
                return valueCompare(o1, o2);
            else
                return 1;
        else if(o2.c == roundColor)
            return -1;
        else
            return 2*colorCompare(o1, o2) + valueCompare(o1, o2);

    }

    @Override
    public boolean isTrump(Card c) {
        return false;
    }
    
}
