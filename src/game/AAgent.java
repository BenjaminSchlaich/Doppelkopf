package game;

/**
 * This interface declares the functionality that an agent (i.e. player) in the game must provide.
 * For a player to be able to participate, he must be able to:
 *  -   receive a match, i.e. in the beginning of a match,
 *      he is informed about the other players, his hand of cards, and the gamemode that is to be played.
 */
public abstract class AAgent implements IAgent {
    
    public final void receiveHand(Hand h)
    {
        this.hand = h;
    }

    public final void receivePlayers(IAgent left, IAgent middle, IAgent right)
    {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    protected Hand hand;
    protected IAgent left;
    protected IAgent middle;
    protected IAgent right;

}
