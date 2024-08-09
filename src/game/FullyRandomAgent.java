package game;

import java.util.List;
import java.util.Random;

public class FullyRandomAgent extends AAgent {

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
    
}
