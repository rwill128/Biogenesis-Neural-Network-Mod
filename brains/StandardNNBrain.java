package brains;

import agents.AliveAgent;
import eyes.SegmentEye;
import intentionalmover.BCyanSegment;
import java.util.List;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import segments.Segment;
import smartorganisms.STOrganism;
import world.World;
import zephyr.plugin.core.api.Zephyr;
import zephyr.plugin.core.api.monitoring.annotations.Monitor;
import zephyr.plugin.core.api.synchronization.Clock;


/**
 *
 */
public class StandardNNBrain extends RLMethod
{
    @Monitor
    MLData inputData;
    @Monitor
    MLData outputData;
    BasicNetwork network;
   // Clock clock = new Clock("Simple");
    double[] inputDataArray;
    
   
    int numOutputNodes = 0;
    int numInputNodes = 0;
    
    boolean noNetwork = false;
    
    
     
    
    public StandardNNBrain(StandardNNBrain parentBrain)
    {
        inputData = parentBrain.inputData;
        outputData = parentBrain.outputData;
        network = parentBrain.network;
        
    }

    public StandardNNBrain(AliveAgent thisAgent)
    {
        STOrganism thisOrganism;
        thisOrganism = (STOrganism) thisAgent;
        int numInputEyes = thisOrganism.getNumEyes();
        numInputNodes = 0;
        
        MLData nextInputData = null;
        int nextInputCounter = 0;
        for(int i = 0; i < numInputEyes; i++) {
           numInputNodes++;
        }
        inputDataArray = new double[numInputNodes];
        for(int i = 0; i < numInputEyes; i++) {
             inputDataArray[i] = ((SegmentEye) thisOrganism.getEyeSegment(i)).getEyeFeedback();
        }
        inputData = new BasicMLData(inputDataArray);
 
        int numOutputLegs = thisOrganism.getNumLegs();
        numOutputNodes = 2 * numOutputLegs;
        if (numInputNodes >= 1 && numOutputNodes >= 1) {
            network = new BasicNetwork();
            network.addLayer(new BasicLayer(numInputNodes));
            network.addLayer(new BasicLayer(numOutputNodes));
            network.getStructure().finalizeStructure();
        } else {
            noNetwork = true;
        }
    }

    @Override
    public void randomMutate()
    {

    }

    @Override
    public RLMethod getThisBrain()
    {
        return this;
    }

    @Override
    public RLMethod clone()
    {
        return this;
    }

    @Override
    public void think(AliveAgent thisAgent, World thisWorld)
    {
        if (!noNetwork){
            outputData = network.compute(inputData);
            List<Segment> legs = ((STOrganism) thisAgent).getLegs();

            //For every leg, take 3 inputs and send a command.
            int j = 0;
            for (Segment leg : legs) {
                BCyanSegment nextLeg = (BCyanSegment) leg;
                nextLeg.setBrainOutputs(outputData.getData((j * 2)), outputData.getData((j * 2) + 1));
            }
        }
    }
}
