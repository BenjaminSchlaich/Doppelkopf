package game.gamemodes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import game.Card;
import game.Color;
import game.Value;

public class NormalOrder extends ACardOrder {

    private static final int fixed = 12;    // the number of ordinals that don't change with roundColor

    public NormalOrder()
    {
        order = new ArrayList<Set<Card>>(fixed);

        for(int i=0; i<fixed; i++)
            order.add(null);

        // Dullen
        order.set(0, Set.of(new Card(Value.Ten, Color.Hearts)));
        
        // Queens
        for(int o=1; o<=4; o++)
            order.set(o, Set.of(new Card(Value.Queen, Color.values()[o-1])));

        // Jacks
        for(int o=5; o<=8; o++)
            order.set(o, Set.of(new Card(Value.Jack, Color.values()[o-5])));

        // Diamonds
        order.set(9, Set.of(new Card(Value.Ace, Color.Diamonds)));
        order.set(10, Set.of(new Card(Value.King, Color.Diamonds)));
        order.set(11 , Set.of(new Card(Value.Nine, Color.Diamonds)));

        computeOrder();
    }

    @Override
    protected void computeOrder() 
    {    
        // Round opening color
        if(roundColor != null)
        {
            order.add(Set.of(new Card(Value.Ace, roundColor)));
            order.add(Set.of(new Card(Value.Ten, roundColor)));
            order.add(Set.of(new Card(Value.King, roundColor)));
            order.add(Set.of(new Card(Value.Nine, roundColor)));
        }

        int index = order.size();

        // all other colors are below
        for(int i=0; i<((roundColor == null) ? 12 : 16); i++)
            order.add(new HashSet<Card>());

        for(Color c: Color.values())
        {
            if(c == roundColor || c == Color.Diamonds)
                continue;
            
            order.get(index+0).add(new Card(Value.Ace, c));
            order.get(index+1).add(new Card(Value.Ten, c));
            order.get(index+2).add(new Card(Value.King, c));
            order.get(index+3).add(new Card(Value.Nine, c));
        }
    }

    /**
     * This has to be overriden, because for "Dulle" the inverse reflective order holds.
     */
    @Override
    public int compare(Card o1, Card o2)
    {
        if(o1.dulle && o2.dulle)
            return -1;
        else
            return super.compare(o1, o2);
    }

    @Override
    protected boolean isTrump(Card c)
    {
        if(c.dulle || c.v == Value.Queen || c.v == Value.Jack || c.c == Color.Diamonds)
            return true;
        else
            return false;
    }
}
