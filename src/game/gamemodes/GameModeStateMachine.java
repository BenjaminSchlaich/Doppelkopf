package game.gamemodes;

import java.util.Set;

import game.AAgent;
import game.Pair;

/**
 * This state machine is used to determine the game that will be played after the bidding is over.
 * Feed it with all the Ansagen in the right order, and it will return the right game-mode when
 * calling the getState() function. Because everything is handeld statically, resetState() must be called
 * before re-using the state machine.
 */
public final class GameModeStateMachine {
    
    public static void updateState(GameMode m, AAgent ag)
    {
        if(gm == null
        || gm == GameMode.Normal
        || gm == GameMode.Armut && m == GameMode.Hochzeit
        || (gm == GameMode.Armut || gm == GameMode.Hochzeit) && solo.contains(m))
        {
            gm = m;

            if(m != GameMode.Normal)
            GameModeStateMachine.ag = ag;
        }
    }

    public static Pair<GameMode, AAgent> getState()
    {
        return new Pair<>(gm, ag);
    }

    public static void resetState()
    {
        gm = null;
        ag = null;
    }

    private static GameMode gm = null;

    private static AAgent ag = null;

    private static final Set<GameMode> solo = Set.of(GameMode.Damensolo, GameMode.Bubensolo, GameMode.Kreuzsolo, GameMode.Piksolo, GameMode.Herzsolo, GameMode.Karosolo, GameMode.Fleischlos);
}
