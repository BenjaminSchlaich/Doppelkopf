package logging;

import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

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

    public void gameModeProposal(GameMode m, AAgent a)
    {
        logLevel(2, "Player " + a + " proposed a " + m + " match.");
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
        logLevel(3, "Player " + ag + " played a " + ca + ".");
    }

    public void roundWon(AAgent a)
    {
        logLevel(2, "Player " + a + " got the Stich.");
    }

    public void matchResult(int[] scores)
    {
        logLevel(1, "Match results: " + Arrays.toString(scores));
    }

    public void listResult(int[] scores)
    {
        logEntries.add("-".repeat(100));
        
        logEntries.add("List results: " + Arrays.toString(scores));

        logEntries.add("-".repeat(100));
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
        s = "  ".repeat(i) + s;

        logEntries.add(s);
    }

    private List<String> logEntries = new LinkedList<>();
}
