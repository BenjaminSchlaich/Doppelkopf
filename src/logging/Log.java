package logging;

import java.util.LinkedList;
import java.util.List;

import game.AAgent;
import game.Ansage;
import game.Card;
import game.gamemodes.GameMode;

public class Log {

    public void matchStarted(int counter, AAgent[] agents)
    {
        logEntries.add("-".repeat(100));

        String s = "Match no. " + counter + " has started with the agents ";
        
        for(int i=0; i<4; i++)
            s += agents[i] + (i < 3 ? ", " : ".");

        logEntries.add(s);

        logEntries.add("-".repeat(100));
    }

    public void gameModeChosen(GameMode m, AAgent a)
    {
        if(m == GameMode.Normal)
            logLevel(1, "This will be a Normal match.");
        else
            logLevel(1, "Player " + a + " has chosen a " + m + " match.");
    }

    public void ansageMade(Ansage an, AAgent ag)
    {
        logLevel(2, "Player " + ag + " announced " + an + ".");
    }
    
    public void cardPlayed(Card ca, AAgent ag)
    {
        logLevel(2, "Player " + ag + " played a " + ca + ".");
    }

    @Override
    public String toString()
    {
        String all = "";

        for(String s: logEntries)
            all += s + "\n";
        
        return all;
    }

    private void logLevel(int i, String s)
    {
        s += "  ".repeat(i);

        logEntries.add(s);
    }

    private List<String> logEntries = new LinkedList<>();
}
