import game.CardStack;

public class App {
    public static void main(String[] args) throws Exception {
        
        CardStack s = new CardStack();

        System.out.println("Created new stack:\n" + s);

        s.shuffle();

        System.out.println("The shuffled stack:\n" + s);
    }
}
