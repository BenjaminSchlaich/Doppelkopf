
package game;

public class Card {

    public Card(Value v, Color c) {

        this.v = v;
        this.c = c;
        fuchs = v == Value.Ace && c == Color.Diamonds;
        dulle = v == Value.King && c == Color.Hearts;
    }

    public String toString() {
        return "(" + c.toString() + ", " + v.toString() + ")";
    }
    
    public final Value v;

    public final Color c;

    public final boolean fuchs;

    public final boolean dulle;

}
