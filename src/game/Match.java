package game;

public class Match {

    public Match(AAgent[] agents)
    {
        if(agents.length != 4)
            throw new IllegalArgumentException("Match(): must construct with exactly 4 agents.");

        this.agents = agents;
    }

    public void playMatch()
    {
        informPlayers();

        matchfinding();

        for(int round=0; round<12; round++)
            playRound();
    }

    public void informPlayers()
    {
        CardStack s = new CardStack();

        s.shuffle();

        for(int i=0; i<4; i++)
        {
            Hand h = new Hand(s);

            agents[i].receiveHand(h);
            agents[i].receivePlayers(agents[(i + 1) % 4], agents[(i + 2) % 4], agents[(i + 3) % 4]); 
        }
    }

    /**
     * This constitutes the first phase of a match.
     * The players, in order, make their Ansage ("Gesund", "Hochzeit", ...).
     */
    public void matchfinding()
    {
        
    }

    public void playRound()
    {

    }

    public Card getLastRoundOpening()
    {
        return lastRoundOpening;
    }

    private final AAgent[] agents;

    private Card lastRoundOpening;

}
