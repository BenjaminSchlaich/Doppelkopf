package game;

import java.util.List;
import java.util.Random;

import game.gamemodes.GameMode;

public final class FullyRandomAgent extends AAgent {

    public FullyRandomAgent(String name) {
        super(name);
    }

    private Random rand = new Random();

    @Override
    public Ansage sendAnsage(List<Ansage> valid) 
    {
        if(valid.size() > 0 && rand.nextDouble() < 0.1)
                return valid.get(rand.nextInt(valid.size()));
        else
            return Ansage.Nothing;
    }

    @Override
    public void receiveAnsage(Ansage a, IAgent p)
    {
        // what should a random player care?
    }

    @Override
    public void receiveGameMode(GameMode m, IAgent a) {
        // what should a random player care?
    }

    @Override
    public Card sendCard() {
        
        var valid = validPlays();

        Card c = valid.get(rand.nextInt(valid.size()));

        // find the index of this card.
        int ind = 0;

        for(Card cc: hand.getCards())
            if(c.equals(cc))
                break;
            else
                ind++;

        return hand.playCard(ind);
    }

    @Override
    public void receiveCard(Card c, IAgent p) {
        // what should a random player care?
    }

    @Override
    public void receiveRoundWinner(IAgent a) {
        // what should a random player care?
    }

    int ansageCount = 0;

    @Override
    public GameMode sendGameMode() {
        // TODO Auto-generated method stub
        return GameMode.values()[rand.nextInt(GameMode.values().length)];
    }
}
