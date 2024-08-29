package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import game.gamemodes.ACardOrder;
import game.gamemodes.BubensoloOrder;
import game.gamemodes.DamensoloOrder;
import game.gamemodes.FarbsoloOrder;
import game.gamemodes.FleischlosOrder;
import game.gamemodes.GameMode;
import game.gamemodes.GameModeStateMachine;
import game.gamemodes.NormalOrder;
import logging.Log;

/**
 * The table master represents the entire table that plays.
 * He is responsible for administering the communication between the players (i.e. agents) who participate,
 * start matches and rounds, hand out cards and summarize the list.
 * A list consists of multiple matches, which, in turn, consist of 12 rounds each.
 */
public final class TableMaster {

    public TableMaster(AAgent[] agents, Log log)
    {
        if(agents.length != 4)
            throw new IllegalArgumentException("Match(): must construct with exactly 4 agents.");

        this.agents = agents;

        this.initialHands = new Hand[4];

        this.matchStartIndex = 0;

        this.agentAnsagen = new ArrayList<List<Ansage>>(4);

        for(int i=0; i<4; i++)
            agentAnsagen.add(new LinkedList<Ansage>());
        
        this.matchCardLog = null;

        this.log = log;
    }

    /**
     * Play matchCount matches with the agents that were specified.
     */
    public void playList(int matchCound)
    {
        initializePlayers();

        for(int i=0; i<matchCound; i++)
        {
            log.matchStarted(matchCound, agents);

            playMatch();

            matchStartIndex++;
        }
    }

    /**
     * Checks what cards the given player <a> is allowed to play right now.
     * If it isn't <a>'s turn, and the player to come out hasn't played yet,
     * it is unknown what cards <a> is allowed to play, and so an empty list will be returned.
     * If, however, it is <a>'s turn, the method returns just the cards that can be legally played.
     * @param a the agent to check for
     * @return the list of all playable cards atm.
     */
    public List<Card> validForPlayer(AAgent a)
    {
        int id = -1;

        for(int i=0; i<4; i++)
            if(agents[i] == a)
            {
                id = i;
                break;
            }
        
        var l = new LinkedList<Card>();

        if(id < 0)
            return l;                           // this player doesn't even play here, so no valid cards exist!
        
        l.addAll(initialHands[id].getCards());

        for(var cs: matchCardLog)
            l.remove(cs[id]);                   // remove all cards that have been played by a before.
        
        if(roundStartLog.getLast() == id)       // if this player comes out, he can play whatever he wants
            return l;
        
        // otherwise he has to obey the color!
        if(matchCardLog.getLast()[roundStartLog.getLast()] == null)
            return new LinkedList<>();          // the first player hasn't even played, so nothing is possible
        
        Card openingCard = matchCardLog.getLast()[roundStartLog.getLast()];
        
        boolean hasSameColor = false;

        if(cardOrder.isTrump(openingCard))
        {
            for(Card c: l)
                if(cardOrder.isTrump(c))
                {
                    hasSameColor = true;
                    break;
                }
            
            if(hasSameColor)
                l.removeIf(c -> !cardOrder.isTrump(c));
        }
        else
        {
            for(Card c: l)
                if(c.c == openingCard.c)
                {
                    hasSameColor = true;
                    break;
                }
            
            if(hasSameColor)
                l.removeIf(c -> c.c != openingCard.c);
        }

        return l;
    }

    /*
     * Organize the playing of one match, consisting of the following steps:
     * 1. Hand out the cards to the players
     * 2. Ask them for their Ansagen for the GameMode that will be played.
     * 3. Play 12 rounds.
     * 4. Update the matchStartIndex, so for the next match in the list the next player will start.
     */
    private void playMatch()
    {
        handOutCards();

        matchfinding();

        int roundStartIndex = matchStartIndex;

        matchCardLog = new LinkedList<Card[]>();

        roundStartLog = new LinkedList<Integer>();

        for(int round=0; round<12; round++)
            roundStartIndex = playRound(roundStartIndex);
        
        matchStartIndex++;
    }

    // tell the players about their neighbors.
    private void initializePlayers()
    {
        for(int i=0; i<4; i++)
            agents[i].receivePlayers(agents[(i + 1) % 4], agents[(i + 2) % 4], agents[(i + 3) % 4], this); 
    }

    // give a random hand of cards to each player.
    private void handOutCards()
    {
        CardStack s = new CardStack();

        s.shuffle();

        for(int i=0; i<4; i++)
        {
            Hand h = new Hand(s);

            initialHands[i] = h;

            agents[i].receiveHand(new Hand(h));
        }
    }

    /**
     * This constitutes the first phase of a match.
     * The players, in order, make their Ansage ("Gesund", "Hochzeit", ...).
     */
    private void matchfinding()
    {
        int i = matchStartIndex;

        GameModeStateMachine.resetState();

        do
        {
            askForAnsagen(i);

            i = (i+1) % 4;                                      // select the next agent

        } while(i != matchStartIndex);                          // until we reach the agent who made the Ansage

        Pair<GameMode, AAgent> p = GameModeStateMachine.getState();

        for(i=0; i<4; i++)
            agents[i].receiveGameMode(p.first, p.second);
        
        log.gameModeChosen(p.first, p.second);
        
        switch(p.first)
        {
            case Normal:
            case Armut:
            case Hochzeit:
                cardOrder = new NormalOrder();
                break;
            case Damensolo:
                cardOrder = new DamensoloOrder();
                break;
            case Bubensolo:
                cardOrder = new BubensoloOrder();
            case Kreuzsolo:
                cardOrder = new FarbsoloOrder(Color.Kreuz);
            case Piksolo:
                cardOrder = new FarbsoloOrder(Color.Pik);
            case Herzsolo:
                cardOrder = new FarbsoloOrder(Color.Herz);
            case Karosolo:
                cardOrder = new FarbsoloOrder(Color.Karo);
            case Fleischlos:
                cardOrder = new FleischlosOrder();
        }
    }

    /**
     * Play one round in a match.
     * @param roundStartIndex Who will start playing?
     * @return returns the next roundStartIndex, i.e. the player who won the current round.
     */
    private int playRound(int roundStartIndex)
    {
        Card[] cardsPlayed = new Card[4];

        int i = roundStartIndex;

        // edit: i moved this up before the loop, so that i have access during this match.
        // this is important to check whether the colors are obeyed!
        matchCardLog.add(cardsPlayed);              // log the cards played, so that consistency can be checked!

        roundStartLog.add(roundStartIndex);         // without this log the matchCardLog is useless.

        do
        {
            askForAnsagen(i);

            var canPlay = validForPlayer(agents[i]);

            cardsPlayed[i] = agents[i].sendCard();

            if(!canPlay.contains(cardsPlayed[i]))
                throw new IllegalStateException("Player " + agents[i] + " illegaly played a " + cardsPlayed[i]);

            log.cardPlayed(cardsPlayed[i], agents[i]);

            for(int j=0; j<4; j++)
                agents[j].receiveCard(cardsPlayed[i], agents[i]);

            i = (i+1) % 4;

        } while(i != roundStartIndex);

        // find the winner
        int iMax = 0;
        Card cMax = cardsPlayed[0];
        for(i=1; i<4; i++)
            if(cardOrder.compare(cMax, cardsPlayed[i]) < 0)
            {
                iMax = i;
                cMax = cardsPlayed[i];
            }
        
        for(i=0; i<4; i++)
            agents[i].receiveRoundWinner(agents[iMax]);
        
        log.roundWon(agents[iMax]);

        return iMax;
    }

    /**
     * Ask the given player for an arbitrary number of Ansagen.
     * If isMatchfinding is true, and the player announces "Nothing" before any GameMode, by default this will
     * be interpreted as a proposal for "Normal". Agents should just well-behave lol
     * @param agentIndex
     * @param isMatchfinding if this is true, players must answer with a game-mode before they can say "Nothing"
     */
    private void askForAnsagen(int agentIndex)
    {
        Ansage a;
        List<Ansage> valids = new LinkedList<>(Arrays.asList(Ansage.values()));

        do
        {
            a = agents[agentIndex].sendAnsage(valids);                  // ask the current agent for his Ansage
            valids.remove(a);                                           // prohibit the same one twice

            if(a != Ansage.Nothing)
            {
                agentAnsagen.get(agentIndex).add(a);                    // memorize all Ansagen, of course.
                log.ansageMade(a, agents[agentIndex]);                  // also log this event externally.
            }
            
            GameModeStateMachine.updateState(a, agents[agentIndex]);    // figures out the game that will be played
            
            for(int j=0; j<4; j++)                                      // now tell all other agents about it
            {
                if(j == agentIndex)
                    continue;
                else
                    agents[j].receiveAnsage(a, agents[agentIndex]);     // the agent himself must not be informed
            }

        } while(a != Ansage.Nothing);                                   // and let any agent make as many unique Ansagen as they want.
    }

    private final AAgent[] agents;                  // the agents playing at this table

    private final Hand[] initialHands;              // and their respective initial hands

    private ArrayList<List<Ansage>> agentAnsagen;   // logs the Ansagen that have been made during the current/last match

    private List<Card[]> matchCardLog;              // logs the cards that have been played during the current/last match

    private List<Integer> roundStartLog;            // provides matchCardLog with the indices of the agent that came out.

    private ACardOrder cardOrder;                   // the card order that holds currently (during matches/rounds)

    private int matchStartIndex;                    // index of the agent who starts the current/next match

    private Log log;                                // this is just for external printing/logging of game info
}
