package game;

import java.util.List;

import game.gamemodes.GameMode;

/**
 * This is the interface that players have of each other.
 */
public interface IAgent {

    /**
     * Asks the agent to tell a Ansage, like Re, Kontra, Unter 90, ...
     * In case he doesn't want, the agent can simply return Nothing.
     * In case that the Ansage is not within valid (e.g. Re after more than 2 turns),
     * the Ansage will be treated as if Nothing would've been returned (ignored).
     * @return
     */
    public Ansage sendAnsage(List<Ansage> valid);

    /**
     * Receive the Ansage that another player in the match has made via sendAnsage().
     * @param a the ansage made
     * @param p the agent that made it
     */
    public void receiveAnsage(Ansage a, IAgent p);

    /**
     * The player is informed about the game mode that will be played, and, if applicable, who initiated it (for non-Normal)
     * @param m the mode that will be played
     * @param a null in case of another game mode but Normal.
     */
    public void receiveGameMode(GameMode m, IAgent a);

    /**
     * Agent plays a card.
     * @return returns the card that he want's to play
     */
    public Card sendCard();

    /**
     * Agent is informed that agent p played card c.
     * @param c
     * @param p
     */
    public void receiveCard(Card c, IAgent p);
    
}
