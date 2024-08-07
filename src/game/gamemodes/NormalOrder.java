package game.gamemodes;

import game.Card;
import game.Color;
import game.Value;

public class NormalOrder extends ACardOrder {

    @Override
    protected void computeOrder() {
        
        // Dullen
        order[0] = new Card[] {new Card(Value.Ten, Color.Hearts)};
        order[1] = new Card[] {new Card(Value.Queen, Color.Cross)};
        
        // Queens
        for(int o=2; o<=4; o++)
            order[o] = new Card[] {new Card(Value.Queen, Color.values()[o])};

        // Jacks
        for(int o=5; o<=7; o++)
            order[o] = new Card[] {new Card(Value.Jack, Color.values()[o])};

        // Diamonds
        order[8] = new Card[] {new Card(Value.Ace, Color.Diamonds)};
        order[9] = new Card[] {new Card(Value.Ten, Color.Diamonds)};
        order[10] = new Card[] {new Card(Value.King, Color.Diamonds)};
    }
}
