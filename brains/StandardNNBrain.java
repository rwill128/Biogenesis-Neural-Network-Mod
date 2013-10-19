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
public class StandardNNBrain extends Brain
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
    
    STOrganism thisOrganism;
     
    
    public StandardNNBrain(StandardNNBrain parentBrain)
    {
       
    }

    public StandardNNBrain(AliveAgent thisAgent)
    {
        
//        Zephyr.advertise(clock, inputData);
//        Zephyr.advertise(clock, outputData);
//        
//        while (clock.tick()) {

        thisOrganism = (STOrganism) thisAgent;
        int numInputEyes = thisOrganism.getNumEyes();
        numInputNodes = 0;
        
        MLData nextInputData = null;
        
        //nextInputCounter
        int nextInputCounter = 0;
        
        //For every eye.
       // for (int j = 0; j < numInputEyes; j++)
        
        //Grab input data for each eye.
        for(int i = 0; i < numInputEyes; i++) {
           numInputNodes++;
        }
        
        inputDataArray = new double[numInputNodes];
        for(int i = 0; i < numInputEyes; i++) {
             inputDataArray[i] = ((SegmentEye) thisOrganism.getEyeSegment(i)).getEyeFeedback();
        }

        
        inputData = new BasicMLData(inputDataArray);
        
        //Copy that data into larger MLData.
//        for (int k = 0; k < nextInputData.size(); k++) {
//            inputData.add(nextInputCounter, nextInputData.getData(k));
//            nextInputCounter++;
//        }
        
        
        
        //Do same thing for outputs.
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
     //   }
    }

    @Override
    public void randomMutate()
    {

    }

    @Override
    public Brain getThisBrain()
    {
        return this;
    }

    @Override
    public Brain clone()
    {
     //   StandardNNBrain newBrain = new StandardNNBrain(this.thisOrganism);
        return this;
    }

//    @Override
//    public void receiveNextInput(Input input)
//    {
//        this.input.add((SevenDoubleInput) input);
//    }

//    @Override
//    public Output getNextOutput()
//    {
//        return output.get(0);
//    }

    @Override
    public void think(AliveAgent thisAgent, World thisWorld)
    {
//        input.clear();
//        output.clear();
  //      while (clock.tick()) {
        if (!noNetwork){
        
//        for (SegmentEye eye : eyeSegs) {
//            //input.add(eye.getEyeFeedback());
//            double[] thisEyeFeedback = eye.getEyeFeedback();
//            for (int p = 0; p < thisEyeFeedback.length; p++) {
//            inputArray[numInputDoubles] = thisEyeFeedback[p];
//            numInputDoubles++;
//            }
//        }
//        
//        MLData inputData = new BasicMLData(numInputDoubles);
        
//        int k = 0;
//        //for (SevenDoubleInput in : input) {
//            k++;
//            for (int i = 0; i < 7; i++) {
//                inputData.add(k, in.getInputAtIndex(i));
//            }
////        }
       outputData = network.compute(inputData);
       List<Segment> legs = thisOrganism.getLegs();
       
       //For every leg, take 3 inputs and send a command.
       int j = 0;
       for (Segment leg : legs) {
           BCyanSegment nextLeg = (BCyanSegment) leg;
           nextLeg.setBrainOutputs(outputData.getData((j * 2)), outputData.getData((j * 2) + 1));
       }
     //   }
//       double[] outputArray = outputData.getData();
//       
////       RegressiveTripletOutput singleOutput = new RegressiveTripletOutput();
////       for (int j = 0; j < outputArray.length; j++) {
////           singleOutput.add(outputArray[j]);
////           if ((j + 1) % 3 == 0) {
////               output.add(singleOutput);
////           }
////       }
//       
//       
//        
//       // train = trainFactory.create(feedForwardNN, inputDataSet, "TYPE_BACKPROP", null);
//        
//        
//        int w =0;
//        int s = 0;
//        for (BCyanSegment mover : moveSegs) {
//            double[] thisLegOutput = null;
//            thisLegOutput[s] = outputArray[w];
//            s++;
//            w++;
//            if ((s+1)%3 == 0) {
//                s = 0;
//                mover.setBrainOutputs(thisLegOutput);
//            }
        
         

        //Find out how many outputs we'll need
        
        }

        
    }
//
//    @Override
//    public List<Output> getOutputCollection()
//    {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }

 
}
