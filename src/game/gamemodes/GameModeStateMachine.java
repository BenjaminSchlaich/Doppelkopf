package game.gamemodes;

import java.util.Set;

import game.AAgent;
import game.Ansage;
import game.Pair;

/**
 * This state machine is used to determine the game that will be played after the bidding is over.
 * Feed it with all the Ansagen in the right order, and it will return the right game-mode when
 * calling the getState() function. Because everything is handeld statically, resetState() must be called
 * before re-using the state machine.
 */
public final class GameModeStateMachine {
    
    public static void updateState(Ansage an, AAgent ag)
    {
        GameMode m = ansageToGameMode(an);

        if(gm == null
        || gm == GameMode.Normal
        || gm == GameMode.Armut && m == GameMode.Hochzeit
        || (gm == GameMode.Armut || gm == GameMode.Hochzeit) && solo.contains(m))
        {
            gm = ansageToGameMode(an);

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

    private static GameMode ansageToGameMode(Ansage a)
    {
        // relevant: Gesund, Armut, Hochzeit, Damensolo, Bubensolo, Kreuzsolo, Piksolo, Herzsolo, Karosolo, Fleischlos
        switch (a) {
            case Gesund: return GameMode.Normal;
            case Armut: return GameMode.Armut;
            case Hochzeit: return GameMode.Hochzeit;
            case Damensolo: return GameMode.Damensolo;
            case Bubensolo: return GameMode.Bubensolo;
            case Kreuzsolo: return GameMode.Kreuzsolo;
            case Piksolo: return GameMode.Piksolo;
            case Herzsolo: return GameMode.Herzsolo;
            case Karosolo: return GameMode.Karosolo;
            case Fleischlos: return GameMode.Fleischlos;

            default:
                return GameMode.Normal;
        }
    }

    private static GameMode gm = null;

    private static AAgent ag = null;

    private static final Set<GameMode> solo = Set.of(GameMode.Damensolo, GameMode.Bubensolo, GameMode.Kreuzsolo, GameMode.Piksolo, GameMode.Herzsolo, GameMode.Karosolo, GameMode.Fleischlos);
}
