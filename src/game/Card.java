
package game;

public class Card {

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
    
    public final Value v;

    public final Color c;

    public final boolean fuchs;

    public final boolean dulle;

}
