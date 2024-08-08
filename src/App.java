import game.Card;
import game.CardStack;
import game.Color;
import game.Hand;
import game.Value;
import game.gamemodes.ACardOrder;
import game.gamemodes.NormalOrder;

public class App {
    public static void main(String[] args) throws Exception {
        
        CardStack s = new CardStack();

        s.shuffle();

        ACardOrder order = new NormalOrder();

        Card c = new Card(Value.Ten, Color.Spades);
        Card d = new Card(Value.Ten, Color.Spades);

        System.out.println(c.equals(d));
        
        for(int i=0; i<5; i++)
        {
            Card c1 = s.draw();
            Card c2 = s.draw();

            if(order.compare(c1, c2) < 0)
                System.out.println(c1 + " < " + c2);
            else if(order.compare(c1, c2) == 0)
                System.out.println(c1 + " == " + c2);
            else
                System.out.println(c1 + " > " + c2);
        }
        //*/
        /*
        Hand h = new Hand(s);

        System.out.println("Drew a hand from it:\n" + h);

        NormalOrder no = new NormalOrder();

        h.sortCards(no);

        System.out.println("The sorted hand:\n" + h);
        */
    }
}
