package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import game.gamemodes.ACardOrder;
import game.gamemodes.GameMode;
import game.gamemodes.GameModeStateMachine;
import game.gamemodes.NormalOrder;

/**
 * The table master represents the entire table that plays.
 * He is responsible for administering the communication between the players (i.e. agents) who participate,
 * start matches and rounds, hand out cards and summarize the list.
 * A list consists of multiple matches, which, in turn, consist of 12 rounds each.
 */
public class TableMaster {

    public TableMaster(AAgent[] agents)
    {
        if(agents.length != 4)
            throw new IllegalArgumentException("Match(): must construct with exactly 4 agents.");

        this.agents = agents;

        this.matchStartIndex = 0;

        this.agentAnsagen = new ArrayList<List<Ansage>>(4);

        for(int i=0; i<4; i++)
            agentAnsagen.add(new LinkedList<Ansage>());
        
        this.matchCardLog = null;
    }

    /**
     * Play matchCount matches with the agents that were specified.
     */
    public void playList(int matchCound)
    {
        initializePlayers();

        for(int i=0; i<matchCound; i++)
        {
            playMatch();
            matchStartIndex++;
        }
    }

    private void playMatch()
    {
        handOutCards();

        matchfinding();

        int roundStartIndex = matchStartIndex;

        matchCardLog = new LinkedList<Card[]>();

        for(int round=0; round<12; round++)
            roundStartIndex = playRound(roundStartIndex);
        
        matchStartIndex++;
    }

    // tell the players about their neighbors.
    private void initializePlayers()
    {
        for(int i=0; i<4; i++)
            agents[i].receivePlayers(agents[(i + 1) % 4], agents[(i + 2) % 4], agents[(i + 3) % 4]); 
    }

    // give a random hand of cards to each player.
    private void handOutCards()
    {
        CardStack s = new CardStack();

        s.shuffle();

        for(int i=0; i<4; i++)
        {
            Hand h = new Hand(s);

            agents[i].receiveHand(h);
        }
    }

    /**
     * This constitutes the first phase of a match.
     * The players, in order, make their Ansage ("Gesund", "Hochzeit", ...).
     */
    private void matchfinding()
    {
        int i = matchStartIndex;

        do
        {
            Ansage a;
            List<Ansage> valids = Arrays.asList(Ansage.values());

            do
            {
                a = agents[i].sendAnsage(valids);               // ask the current agent for his Ansage
                valids.remove(a);                               // prohibit the same one twice

                if(a != Ansage.Nothing)
                    agentAnsagen.get(i).add(a);                 // memorize all Ansagen, of course.
                
                GameModeStateMachine.updateState(a);            // figures out the game that will be played

                for(int j=0; j<4; j++)                          // now tell all other agents about it
                {
                    if(j == i)
                        continue;
                    else
                        agents[j].receiveAnsage(a, agents[i]);  // the agent himself must not be informed
                }

            } while(a != Ansage.Nothing);                       // and let any agent make as many unique Ansagen as they want.

            i = (i+1) % 4;                                      // select the next agent

        } while(i != matchStartIndex);                          // until we reach the agent who made the Ansage

        Pair<GameMode, IAgent> p = GameModeStateMachine.getState();

        for(i=0; i<4; i++)
            agents[i].receiveGameMode(p.first, p.second);
        
        switch(p.first)
        {
            case Normal:
                cardOrder = new NormalOrder();
                break;
        
            default:
                break;
        }
    }

    /**
     * Play one round in a match.
     * @param roundStartIndex Who will start playing?
     * @return returns the next roundStartIndex, i.e. the player who won the current round.
     */
    public int playRound(int roundStartIndex)
    {
        Card[] cardsPlayed = new Card[4];

        int i = roundStartIndex;

        do
        {
            cardsPlayed[i] = agents[i].sendCard();

            for(int j=0; j<4; j++)
                agents[j].receiveCard(cardsPlayed[i], agents[i]);

            i = (i+1) % 4;

        } while(i != roundStartIndex);

        matchCardLog.add(cardsPlayed);

        return roundStartIndex;
    }

    private final AAgent[] agents;                  // the agents playing at this table

    private ArrayList<List<Ansage>> agentAnsagen;   // logs the Ansagen that have been made during the current/last match

    private List<Card[]> matchCardLog;              // logs the cards that have been played during the current/last match

    private ACardOrder cardOrder;                   // the card order that holds currently (during matches/rounds)

    private int matchStartIndex;                    // index of the agent who starts the current/next match
}
