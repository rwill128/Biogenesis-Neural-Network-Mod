package brains;

import agents.AliveAgent;
import java.io.Serializable;
import java.util.List;
import world.World;

/**
 *
 */
public abstract class Brain implements Cloneable, Serializable
{
    
     public BrainType brainType;
     
     public abstract void think(AliveAgent thisAgent, World thisWorld);
    
     public abstract void randomMutate();
     
     public abstract Brain getThisBrain();
     
     @Override
     public abstract Brain clone();
     
//     public abstract void receiveNextInput(Input input);
//     
//     public abstract List<Output> getOutputCollection();
//     
//     public abstract Output getNextOutput();
     
}
