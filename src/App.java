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

        System.out.println("Shuffled card stack:\n" + s);

        s.sort();

        System.out.println("Sorted card stack:\n" + s);
    }
}
