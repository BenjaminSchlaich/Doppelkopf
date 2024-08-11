package game.gamemodes;

import game.Card;
import game.Color;

public class FarbsoloOrder extends ACardOrder {

    public FarbsoloOrder(Color c)
    {
        this.trumpColor = c;
    }

    @Override
    public int compare(Card o1, Card o2)
    {
        if(isTrump(o1))
            if(isTrump(o2))
                return colorCompare(o1, o2);
            else
                return 1;
        else if(isTrump(o2))
            return -1;
        else if(o1.c == roundColor)
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
        return c.c == trumpColor;
    }

    private Color trumpColor;
    
}
