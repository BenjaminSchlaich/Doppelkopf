package game.gamemodes;

import java.util.Set;

import game.AAgent;
import game.Ansage;
import game.IAgent;
import game.Pair;

public final class GameModeStateMachine {
    
    public static void updateState(Ansage an, AAgent ag)
    {
        if(!irrelevant.contains(an))
        {
            if(gm == null
            || gm == GameMode.Normal
            || gm == GameMode.Armut && an == Ansage.Hochzeit
            || (gm == GameMode.Armut || gm == GameMode.Hochzeit) && solo.contains(an))
                gm = ansageToGameMode(an);
        }
    }

    public static Pair<GameMode, AAgent> getState()
    {
        return new Pair<>(gm, ag);
    }

    public static void resetState()
    {
        gm = null;
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

    private static final Set<Ansage> irrelevant = Set.of(Ansage.Re, Ansage.Kontra, Ansage.Unter_90, Ansage.Unter_60, Ansage.Unter_30, Ansage.Nothing);

    private static final Set<Ansage> solo = Set.of(Ansage.Damensolo, Ansage.Bubensolo, Ansage.Kreuzsolo, Ansage.Piksolo, Ansage.Herzsolo, Ansage.Karosolo, Ansage.Fleischlos);
}
