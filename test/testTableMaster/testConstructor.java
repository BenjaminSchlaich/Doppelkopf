
package testTableMaster;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import game.AAgent;
import game.TableMaster;
import testUtilities.DummyTestAgent;

public class testConstructor {
    
    @Test
    void basicTests()
    {
        AAgent[][] brokenAgents = new AAgent[][] {
            null,
            new AAgent[0],
            new AAgent[3],
            new AAgent[5]
        };

        for(var ags: brokenAgents)
        {
            assertThrowsExactly(
                IllegalArgumentException.class,
                () -> {new TableMaster(ags, null);},
                "Expected TableMaster(AAgent[] agents, Log log) to throw an IllegalArgumentException, but it didn't");
        }

        AAgent[] correctAgents = new AAgent[4];

        for(int i=0; i<4; i++)
            correctAgents[i] = new DummyTestAgent("Agent " + i);

        assertDoesNotThrow(() -> {new TableMaster(correctAgents, null);});
    }

}
