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
        if(!madeAnsage)
        {
            madeAnsage = true;
            return super.sendAnsage(valid);
        }
        else
            return Ansage.Nothing;
    }
    
    boolean madeAnsage = false;
}
