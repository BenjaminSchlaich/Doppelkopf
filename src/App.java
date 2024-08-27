
import game.*;
import logging.Log;

public class App {

    public static void runMatch()
    {
        AAgent[] players = new AAgent[4];
        String[] names = new String[]{"Angela", "Viktor", "Bertha", "Thomas"};

        for(int i=0; i<4; i++)
            players[i] = new FullyRandomAgent(names[i]);
        
        Log l = new Log();

        
        TableMaster tm = new TableMaster(players, l);

        tm.playList(1);

        System.out.println(l);
    }

    public static void main(String[] args) throws Exception {

        runMatch();
    }
}
