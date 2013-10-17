/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genes;

import biogenesis.Utils;
import organisms.Pigment;

/**
 *
 * @author Rick Williams
 */
public class NeuralGene extends Gene
{
    
    public NeuralGene() {
        
    }
    
        public NeuralGene(double length, double theta, Pigment color) {
		this.length = length;
		this.theta = theta;
		this.pigment = color;
	}
        /**
	 * Chooses a random segment color, based on the probabilities specified in
	 * user's preferences.
	 */
        @Override
	public void randomizeColor() {
		int max_prob = Utils.getRED_PROB() + Utils.getGREEN_PROB() + Utils.getBLUE_PROB()
				+ Utils.getWHITE_PROB() + Utils.getGRAY_PROB() + Utils.getCYAN_PROB() +
				+ Utils.getYELLOW_PROB();
		int prob = Utils.random.nextInt(max_prob);
		int ac_prob = Utils.getRED_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.RED);
			return;
		}
		ac_prob += Utils.getGREEN_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.GREEN);
			return;
		}
		ac_prob += Utils.getBLUE_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.BLUE);
			return;
		}
		ac_prob += Utils.getWHITE_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.WHITE);
			return;
		}
                ac_prob += Utils.getCYAN_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.BCYAN);
			return;
		}
		ac_prob += Utils.getGRAY_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.GRAY);
			return;
		}
		this.setPigment(Pigment.YELLOW);
	}
        
       
}
