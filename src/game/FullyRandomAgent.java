package game;

import java.util.List;
import java.util.Random;

import game.gamemodes.GameMode;

public class FullyRandomAgent extends AAgent {

    public FullyRandomAgent(String name) {
        super(name);
    }

    private Random rand = new Random();

    @Override
    public Ansage sendAnsage(List<Ansage> valid) 
    {
        return valid.get(rand.nextInt(valid.size()));
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
