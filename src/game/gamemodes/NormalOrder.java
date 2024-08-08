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
            return valueCompare(o1, o2);        // both Fehl
    }

    protected int colorCompare(Card c1, Card c2)
    {
        if(c1.c.ordinal() <= c2.c.ordinal())
            return 1;
        else
            return -1;
    }

    protected int valueCompare(Card c1, Card c2)
    {
        if(c1.v.ordinal() <= c2.v.ordinal())
            return 1;
        else
            return -1;
    }

    protected boolean isTrump(Card c)
    {
        if(c.dulle || c.v == Value.Dame || c.v == Value.Bube || c.c == Color.Karo)
            return true;
        else
            return false;
    }
}
