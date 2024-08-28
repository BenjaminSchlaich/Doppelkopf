
package game;

public final class Card {

    public Card(Value v, Color c) {

        this.v = v;
        this.c = c;
        fuchs = v == Value.Ass && c == Color.Karo;
        dulle = v == Value.Zehn && c == Color.Herz;
    }

    public String toString() {
        return "(" + c.toString() + ", " + v.toString() + ")";
    }

    @Override
    public boolean equals(Object o)
    {
        if(o instanceof Card)
        {
            Card c = (Card) o;

            if(c != null && c.c == this.c && c.v == this.v)
                return true;
        }

        return false;
    }

    public int getPoints()
    {
        switch(v)
        {
            case Neun: return 0;
            case Zehn: return 10;
            case Bube: return 2;
            case Dame: return 3;
            case König: return 4;
            case Ass: return 11;

            default:
                return 0;
        }
    }
    
    public final Value v;

    public final Color c;

    public final boolean fuchs;

    public final boolean dulle;

}
