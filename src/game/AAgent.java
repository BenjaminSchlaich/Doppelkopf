package game;

import java.util.List;

/**
 * This interface declares the functionality that an agent (i.e. player) in the game must provide.
 * For a player to be able to participate, he must be able to:
 *  -   receive a match, i.e. in the beginning of a match,
 *      he is informed about the other players, his hand of cards, and the gamemode that is to be played.
 */
public abstract class AAgent implements IAgent {

    public AAgent(String name)
    {
        this.name = name;
    }
    
    public final void receiveHand(Hand h)
    {
        this.hand = h;
    }

    public final void receivePlayers(IAgent left, IAgent middle, IAgent right, TableMaster tm)
    {
        this.left = left;
        this.middle = middle;
        this.right = right;
        this.tableMaster = tm;
    }
    
    /**
     * This can be used by agents for convenience, so they don't have to check what cards can be played.
     * Use this to avoid making mistakes when sending your card to the tablemaster.
     * @return a list with all cards from the hand that could be played atm.
     */
    protected final List<Card> validPlays()
    {
        return tableMaster.validForPlayer(this);
    }

    @Override
    public final String toString()
    {
        return name;
    }

    private String name;
    protected Hand hand;
    protected IAgent left;
    protected IAgent middle;
    protected IAgent right;
    private TableMaster tableMaster;
}
