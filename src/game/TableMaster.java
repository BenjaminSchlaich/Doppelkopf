package game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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
 * 
 * Some more nomenclature:
 * Value = The value of a card for beating other cards in play.
 * Point = The points that cards award when winning them for a certain match.
 * Score = The score that is evaluated at the end of each match for all players, and summed up for the whole list.
 */
public final class TableMaster {

    /**
     * Construct a tablemaster for a new table with the agents given, logging into <log>
     * @param agents    must be a non-null array with exactly 4 non-null agents.
     * @param log       the log where the tablemaster dumps status info on the games that run.
     */
    public TableMaster(AAgent[] agents, Log log)
    {
        if(agents == null || agents.length != 4)
            throw new IllegalArgumentException("Match(): must construct with exactly 4 non-null agents.");

        this.agents = agents;

        this.playerAcceptedArmut = null;

        this.initialHands = new Hand[4];

        this.matchStartIndex = 0;

        this.agentAnsagen = new ArrayList<List<Ansage>>(4);

        for(int i=0; i<4; i++)
        {
            if(agents[i] == null)
                throw new IllegalArgumentException("Match(): cannot construct with null-agents.");

            agentAnsagen.add(new LinkedList<Ansage>());
        }
        
        this.matchCardLog = null;

        this.roundStartLog = null;

        this.listScore = null;

        this.log = log;
    }

    /**
     * Play matchCount matches with the agents that were specified.
     */
    public void playList(int matchCount)
    {
        initializePlayers();

        listScore = new int[4];

        for(int i=0; i<matchCount; i++)
        {
            log.matchStarted(matchCount, agents);

            playMatch();

            var matchScore = countScoreForMatch();

            log.matchResult(matchScore);

            for(int id=0; id<4; id++)
                listScore[id] += matchScore[id];
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
        {
            roundStartIndex = playRound(roundStartIndex);
        }
        
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
            GameMode gm = agents[i].sendGameMode();

            log.gameModeProposal(gm, agents[i]);

            GameModeStateMachine.updateState(gm, agents[i]);

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

        // don't allow the same Ansage twice!
        valids.removeIf(aa -> agentAnsagen.get(agentIndex).contains(aa));

        do
        {
            a = agents[agentIndex].sendAnsage(valids);                  // ask the current agent for his Ansage
            valids.remove(a);                                           // prohibit the same one twice

            if(a != Ansage.Nothing)
            {
                agentAnsagen.get(agentIndex).add(a);                    // memorize all Ansagen, of course.
                log.ansageMade(a, agents[agentIndex]);                  // also log this event externally.
            }
            
            for(int j=0; j<4; j++)                                      // now tell all other agents about it
            {
                if(j == agentIndex)
                    continue;
                else
                    agents[j].receiveAnsage(a, agents[agentIndex]);     // the agent himself must not be informed
            }

        } while(a != Ansage.Nothing);                                   // and let any agent make as many unique Ansagen as they want.
    }

    private int[] countScoreForMatch()
    {
        int[] points = new int[4];

        for(var round: matchCardLog)
            for(int i=0; i<4; i++)
                points[i] += round[i].getPoints();
        
        Pair<GameMode, AAgent> matchConfig = GameModeStateMachine.getState();

        List<Integer> reTeamInds = new LinkedList<>();
        List<Integer> contraTeamInds = new LinkedList<>();

        boolean solo = false;

        switch(matchConfig.first)
        {
            case Normal:
                // find the players with Kreuzdamen
                Card kd = new Card(Value.Dame, Color.Kreuz);

                for(int i=0; i<4; i++)
                    if(initialHands[i].getCards().contains(kd))
                        reTeamInds.add(i);
                    else
                        contraTeamInds.add(i);

                break;

            case Armut:

                // find the players who announced and accepted the armut
                for(int i=0; i<4; i++)
                    if(agents[i] == matchConfig.second || agents[i] == playerAcceptedArmut)
                        reTeamInds.add(i);
                    else
                        contraTeamInds.add(i);

                break;
            
            case Hochzeit:

                // the player who announced the Hochzeit is Re for sure, find him!
                for(int i=0; i<4; i++)
                    if(agents[i] == matchConfig.second)
                        reTeamInds.add(i);
                
                // in the first or second round, there might have been anothe winner, if so, find him and add to Re!
                for(int i=1; i<=2; i++)
                    if(roundStartLog.get(i) != reTeamInds.getFirst())
                    {
                        reTeamInds.add(i);
                        break;
                    }
                
                // the players who aren't re by now are contra
                for(int i=0; i<4; i++)
                    if(!reTeamInds.contains(i))
                        contraTeamInds.add(i);
                
                break;
            
            default:
                // solo play, only the player who announced is re
                solo = true;
                
                for(int i=0; i<4; i++)
                    if(agents[i] == matchConfig.second)
                        reTeamInds.add(i);
                    else
                        contraTeamInds.add(i);
        }

        int rePoints = 0;
        int contraPoints = 0;

        for(int i: reTeamInds)
            rePoints += points[i];
        
        for(int i: contraTeamInds)
            contraPoints += points[i];
        
        if(rePoints + contraPoints != 240)
            throw new Error("Re + Kontra points should equal to 240, but equals to " + rePoints + contraPoints);
        
        // COMPUTE THE BASE SCORE
        int reScore = rePoints > 120 ? 1 : -1;

        // COMPUTE EXTRA POINTS FOR HIGH POINTS
        int diff = Math.abs(120 - rePoints);

        if(diff > 90)       // unter 30
            reScore *= 4;
        else if(diff > 60)  // unter 60
            reScore *= 3;
        else if(diff > 30)  // unter 90
            reScore *= 2;
        
        // COMPUTE EXTRA POINTS FOR DOPPELKOPF
        Iterator<Card[]> itCards = matchCardLog.iterator();
        Iterator<Integer> itStart = roundStartLog.iterator();

        while(itCards.hasNext())
        {
            int sumCardPoints = 0;
            
            Card[] cs = itCards.next();
            int startI = itStart.next();

            for(Card c:  cs)
                sumCardPoints += c.getPoints();
            
            if(sumCardPoints >= 40)
            {
                if(reTeamInds.contains(startI))
                    reScore++;
                else
                    reScore--;
            }
        }

        // COMPUTE EXTRA POINTS FOR CATCHING FUCHS
        // TODO: implement


        int[] score = new int[4];

        for(int i: reTeamInds)
            if(solo)
                score[i] = reScore*3;
            else
                score[i] = reScore;
        
        for(int i: contraTeamInds)
            score[i] = -reScore;

        return score;
    }

    private final AAgent[] agents;                  // the agents playing at this table

    private final AAgent playerAcceptedArmut;       // if this is non-null, a armut is/was played, and this is the accepting player.

    private final Hand[] initialHands;              // and their respective initial hands

    private ArrayList<List<Ansage>> agentAnsagen;   // logs the Ansagen that have been made during the current/last match

    private List<Card[]> matchCardLog;              // logs the cards that have been played during the current/last match

    private List<Integer> roundStartLog;            // provides matchCardLog with the indices of the agent that came out.

    private int[] listScore;                        // counter of all the list-score collected so far.

    private ACardOrder cardOrder;                   // the card order that holds currently (during matches/rounds)

    private int matchStartIndex;                    // index of the agent who starts the current/next match

    private Log log;                                // this is just for external printing/logging of game info
}
