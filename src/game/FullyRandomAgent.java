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
        // this is not uniformly random, but avoids spamming Ansagen into the log.
        if(rand.nextDouble() < 0.1)
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
        
        int cardsLeft = hand.getCards().size();

        return hand.playCard(rand.nextInt(cardsLeft));
    }

    @Override
    public void receiveCard(Card c, IAgent p) {
        // what should a random player care?
    }

    @Override
    public void receiveRoundWinner(IAgent a) {
        // what should a random player care?
    }
    
}
