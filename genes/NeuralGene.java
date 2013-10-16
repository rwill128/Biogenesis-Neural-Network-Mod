/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genes;

import organisms.Pigment;
import stbiogenesis.STUtils;

/**
 *
 * @author Rick Williams
 */
public class NeuralGene extends Gene
{
        /**
	 * Chooses a random segment color, based on the probabilities specified in
	 * user's preferences.
	 */
        @Override
	public void randomizeColor() {
		int max_prob = STUtils.getRED_PROB() + STUtils.getGREEN_PROB() + STUtils.getBLUE_PROB()
				+ STUtils.getWHITE_PROB() + STUtils.getGRAY_PROB() + STUtils.getCYAN_PROB() +
				+ STUtils.getYELLOW_PROB();
		int prob = STUtils.random.nextInt(max_prob);
		int ac_prob = STUtils.getRED_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.RED);
			return;
		}
		ac_prob += STUtils.getGREEN_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.GREEN);
			return;
		}
		ac_prob += STUtils.getBLUE_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.BLUE);
			return;
		}
		ac_prob += STUtils.getWHITE_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.WHITE);
			return;
		}
                ac_prob += STUtils.getCYAN_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.BCYAN);
			return;
		}
		ac_prob += STUtils.getGRAY_PROB();
		if (prob < ac_prob) {
			this.setPigment(Pigment.GRAY);
			return;
		}
		this.setPigment(Pigment.YELLOW);
	}
        
       
}
