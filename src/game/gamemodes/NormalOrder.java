package game.gamemodes;

import game.Card;
import game.Color;
import game.Value;

public class NormalOrder extends ACardOrder {

    /**
     * This has to be overriden, because for "Dulle" the inverse reflective order holds.
     */
    @Override
    public int compare(Card o1, Card o2)
    {
        if(o1.dulle)
            if(o2.dulle)
                return -1;
            else
                return 1;
        else if(o2.dulle)
            return -1;
        else if(o1.v == Value.Dame)
            if(o2.v == Value.Dame)
                return colorCompare(o1, o2);
            else
                return 1;
        else if(o2.v == Value.Dame)
            return -1;
        else if(o1.v == Value.Bube)
            if(o2.v == Value.Bube)
                return colorCompare(o1, o2);
            else
                return 1;
        else if(o2.v == Value.Bube)
            return -1;
        else if(o1.c == Color.Karo)
            if(o2.c == Color.Karo)
                return valueCompare(o1, o2);
            else
                return 1;
        else if(o2.c == Color.Karo)
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

    public boolean isTrump(Card c)
    {
        if(c.dulle || c.v == Value.Dame || c.v == Value.Bube || c.c == Color.Karo)
            return true;
        else
            return false;
    }
}
