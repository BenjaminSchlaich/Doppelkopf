
package testUtilities;
import java.util.List;

import game.AAgent;
import game.Ansage;
import game.Card;
import game.IAgent;
import game.gamemodes.GameMode;

public class DummyTestAgent extends AAgent {

    public DummyTestAgent(String name) {
        super(name);
    }

    @Override
    public Ansage sendAnsage(List<Ansage> valid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendAnsage'");
    }

    @Override
    public void receiveAnsage(Ansage a, IAgent p) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveAnsage'");
    }

    @Override
    public void receiveGameMode(GameMode m, IAgent a) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveGameMode'");
    }

    @Override
    public Card sendCard() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendCard'");
    }

    @Override
    public void receiveCard(Card c, IAgent p) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveCard'");
    }

    @Override
    public void receiveRoundWinner(IAgent a) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'receiveRoundWinner'");
    }

    @Override
    public GameMode sendGameMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendGameMode'");
    }
    
}
