package game;

import java.util.LinkedList;
import java.util.Random;

public class CardStack {
    
    public CardStack() {
        stack = new LinkedList<Card>();
        
        for(int c=0; c<Color.values().length; c++)
            for(int v=0; v<Value.values().length; v++)
            {
                stack.push(new Card(Value.values()[v], Color.values()[c]));
                stack.push(new Card(Value.values()[v], Color.values()[c]));
            }
    }

    public void shuffle() {

        Random rand = new Random();

        LinkedList<Card> newStack = new LinkedList<Card>();

        while(!stack.isEmpty())
        {
            Card c = stack.remove(rand.nextInt(stack.size()));
            newStack.push(c);
        }

        stack = newStack;
    }

    public Card draw() {
        return stack.removeFirst();
    }

    public String toString() {

        String s = "";

        for(Card c: stack)
            s += c.toString() + "\n";

        return s;
    }

    private LinkedList<Card> stack;
}
