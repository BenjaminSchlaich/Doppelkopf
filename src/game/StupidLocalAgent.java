package game;

import java.util.List;

public class StupidLocalAgent extends FullyRandomAgent
{
    public StupidLocalAgent(String name) {
        super(name);
    }

    @Override
    public Ansage sendAnsage(List<Ansage> valid)
    {
        if(!saidGesund)
        {
            saidGesund = true;
            return Ansage.Gesund;
        }
        else
            return Ansage.Nothing;
    }
    
    boolean saidGesund = false;
}
