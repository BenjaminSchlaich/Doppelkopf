
import game.CardStack;

public class App {
    public static void main(String[] args) throws Exception {
        
        CardStack s = new CardStack();

        s.shuffle();

        System.out.println("Shuffled card stack:\n" + s);

        s.sort();

        System.out.println("Sorted card stack:\n" + s);
    }
}
