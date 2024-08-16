package game;

import java.util.LinkedList;
import java.util.Random;

import game.gamemodes.NormalOrder;

/**
 * This class implements a card stack that can be used in the game.gamemodes
 * Upon creation with the CardStack() constructor, the stack is sorted from high to low (NormalOrder).
 * Therefore, it should be sorted before using it in a match.
 * Then, the method draw() can be used to draw cards, until the stack is empty.
 */
public class CardStack {
    
    /**
     * Creates a sorted, full card stack containing all cards for one match.
     */
    public CardStack() {
        stack = new LinkedList<Card>();
        
        for(int c=0; c<Color.values().length; c++)
            for(int v=0; v<Value.values().length; v++)
            {
                stack.push(new Card(Value.values()[v], Color.values()[c]));
                stack.push(new Card(Value.values()[v], Color.values()[c]));
            }
    }

    /**
     * Uses java.util.Random object for randomness. Shuffles the card entirely randomly.
     */
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

    // must remove this, just for testing!!
    public void sort()
    {
        stack.sort(new NormalOrder());
    }

    /**
     * Draw a card from the stack.
     * isEmpty() should be called before, to check if any cards are left.
     * @return returns the uppermost card, if there are any cards left.
     */
    public Card draw() {
        return stack.removeFirst();
    }

    /**
     * Are cards left on the stack?
     * @return true, iff the stack is non-empty.
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    @Override
    public String toString() {

        String s = "";

        for(Card c: stack)
            s += c.toString() + "\n";

        return s;
    }

    private LinkedList<Card> stack;
}
