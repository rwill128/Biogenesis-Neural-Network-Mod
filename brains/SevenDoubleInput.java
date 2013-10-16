package brains;

import java.util.ArrayList;

/**
 *
 */
public class SevenDoubleInput extends Input
{
    ArrayList<Double> inputDoubles = new ArrayList<>();
    
    public SevenDoubleInput()
    {
        for (int i = 0; i < 7; i++) {
            inputDoubles.add(0d);
            inputDoubles.set(i, 0d);
        }
    }
    
    public void setInputAtIndex(int i, double input) 
    {
        inputDoubles.set(i , input);
    }
    
    public double getInputAtIndex(int i) 
    {
        return inputDoubles.get(i);
    }
    
    public void clearInput() {
        inputDoubles.clear();
    }
}
