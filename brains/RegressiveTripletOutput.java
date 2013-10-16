/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package brains;

import java.util.ArrayList;

/**
 *
 * @author Rick Williams
 */
public class RegressiveTripletOutput extends Output
{
    public ArrayList<Double> outputs = new ArrayList<>(3);
    
    public RegressiveTripletOutput() {
        for (int i = 0; i < 3; i++) {
            outputs.add(0d);
            outputs.set(i, 0d);
        }
        
    }

    public int getNumOutputs()
    {
        return outputs.size();
    }
    
    public void add(double d) 
    {
        outputs.add(d);
    }
    
    public Double getOutputAtIndex(int index) {
        return outputs.get(index);
    }
    
    public void setOutput (ArrayList<Double> output) {
        outputs = output;
    }
    
    public ArrayList<Double> getOutputs() 
    {
        return outputs;
    }
    
    public void clear() 
    {
        outputs.clear();
    }

            
}
