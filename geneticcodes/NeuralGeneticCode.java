/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geneticcodes;

import genes.EyeGene;
import genes.NeuralGene;
import genes.Gene;
import agents.AliveAgent;
import auxiliar.Vector2D;
import biogenesis.Utils;
import brains.Brain;
import brains.BrainFactory;
import eyes.SegmentEye;
import genes.BrainGene;
import genes.SegmentEyeGene;
import static geneticcodes.GeneticCode.MAX_SEGMENTS;
import static geneticcodes.GeneticCode.MIN_SEGMENTS;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import organisms.Pigment;
import segments.SegmentFactory;
import stbiogenesis.STUtils;

/**
 *
 */
public class NeuralGeneticCode extends GeneticCode
{
    
        protected BrainGene brainGene;
        
        int nSegmentEyeGenes;
        private SegmentEyeGene[] segmentEyeGenes;
        
        
        protected int eyeSymmetry;
	protected boolean eyeMirror;
        
        
        public int getEyeSymmetry() { return eyeSymmetry; }
        public void setEyeSymmetry(int eyeSymmetry) { this.eyeSymmetry = eyeSymmetry; }
        
       // public void setRandomEyeSymmetry() { randomEyeSymmetry(); }
        protected void randomSegmentEyeSymmetry() { eyeSymmetry = STUtils.random.nextInt(8)+1; }
        protected void randomSegmentEyeMirror() { mirror = Utils.random.nextBoolean(); }
        
//        public int getNumBrainSegments() 
//        {
//            int numBrainSegments = 0;
//            for(int i =0; i < genes.length; i++) {
//                if (genes[i].getPigment() == Pigment.BCYAN)
//                    numBrainSegments++;
//            }
//            return numBrainSegments;
//        }
        
//        public int getNumEyeGenes() { return eyeGenes.length; }
        
//        public BrainType getBrainGeneType() { return brainGene.getBrainType(); }
    
        public NeuralGeneticCode() {
		randomMirror(); 
		randomSymmetry();
		randomGenes();
                
                randomSegmentEyeMirror();
                randomSegmentEyeSymmetry();
                randomSegmentEyeGenes();
                
                randomBrainGenes();

		randomDisperseChildren();
		calculateReproduceEnergy();
		maxAge = STUtils.getMAX_AGE();
	}
        
        public NeuralGeneticCode(List<Gene> genes, int symmetry, boolean mirror, List<EyeGene> eyeGenes, int eyeSymmetry, boolean eyeMirror, BrainGene brainGene, boolean disperseChildren) {
		int nGenes = genes.size();
		this.genes = new NeuralGene[nGenes];
		genes.toArray(this.genes);
                
                int nSegmentEyeGenes = eyeGenes.size();
		this.segmentEyeGenes = new SegmentEyeGene[nSegmentEyeGenes];
		eyeGenes.toArray(this.segmentEyeGenes);
                
                
                
		this.maxAge = STUtils.getMAX_AGE();                
		this.mirror = mirror;
		this.symmetry = symmetry;
                
                this.eyeMirror = eyeMirror;
                this.eyeSymmetry = eyeSymmetry;                
		this.disperseChildren = disperseChildren;                
		calculateReproduceEnergy();
	}
    
	public NeuralGeneticCode(NeuralGeneticCode parentCode) {
		int i,j;
		int addedGene = -1;
		int removedGene = -1;
		int nGenes;
		boolean randomLength;
		boolean randomTheta;
		boolean randomColor;
		boolean randomBack;
                
                //Brain code. The same principle of applying random mutations should happen here as well.
               // brain = parentCode.getBrain().clone();
               // brain.randomMutate();
                
                //Put an Eye out in front, since we don't have genetic code that describes that yet, either.
               	

                
		if (STUtils.randomMutation())
			randomMirror();
		else
			mirror = parentCode.getMirror();
		if (STUtils.randomMutation()) {
			// change symmetry
			if (STUtils.random.nextInt(10) < 2)
				randomSymmetry();
			else
				symmetry = STUtils.between(symmetry+STUtils.randomSign(), 1, 8);
			nGenes = parentCode.getNGenes();
			if (nGenes * symmetry > MAX_SEGMENTS) {
				symmetry = parentCode.getSymmetry();
			}
		} else {
			// keep symmetry
			symmetry = parentCode.getSymmetry();
			if (STUtils.randomMutation()) {
			// change number of segments
				if (STUtils.random.nextBoolean()) {
				// increase segments
					if (parentCode.getNGenes() * parentCode.getSymmetry() >= MAX_SEGMENTS)
						nGenes = parentCode.getNGenes();
					else {
						nGenes = parentCode.getNGenes() + 1;
						addedGene = STUtils.random.nextInt(nGenes);
					}
				} else {
				// decrease segments
					if (parentCode.getNGenes() * parentCode.getSymmetry() <= MIN_SEGMENTS)
						nGenes = parentCode.getNGenes();
					else {
						nGenes = parentCode.getNGenes() - 1;
						removedGene = STUtils.random.nextInt(parentCode.getNGenes());
					}
				}
			} else {
			// keep number of segments
				nGenes = parentCode.getNGenes();
			}
		}
		// Create genes
		genes = new NeuralGene[nGenes];
		for (i=0,j=0; i<nGenes; i++,j++) {
			if (removedGene == j) {
				i--;
				continue;
			}
			if (addedGene == i) {
				genes[i] = new NeuralGene();
				genes[i].randomize();
				j--;
				continue;
			}
			randomLength = randomTheta = randomColor = randomBack = false;
			if (STUtils.randomMutation())
				randomLength = true;
			if (STUtils.randomMutation())
				randomTheta = true;
			if (STUtils.randomMutation())
				randomColor = true;
			if (STUtils.randomMutation())
				randomBack = true;
			if (randomLength || randomTheta || randomColor || randomBack) {
				genes[i] = new NeuralGene();
				if (randomLength)
					genes[i].randomizeLength();
				else
					genes[i].setLength(parentCode.getGene(j).getLength());
				if (randomTheta)
					genes[i].randomizeTheta();
				else
					genes[i].setTheta(parentCode.getGene(j).getTheta());
				if (randomColor)
					genes[i].randomizeColor();
				else
					genes[i].setPigment(parentCode.getGene(j).getPigment());
			} else
				genes[i] = parentCode.getGene(j);
		}

		if (STUtils.randomMutation())
			randomDisperseChildren();
		else
			disperseChildren = parentCode.getDisperseChildren();
		calculateReproduceEnergy();
		maxAge = STUtils.getMAX_AGE();
	}
        
        /**
	 * Calculates the energy required to reproduce this genetic code.
	 * This energy is 40 plus 3 for each segment.
	 */
        @Override
	protected void calculateReproduceEnergy() {
		reproduceEnergy = 40 + 3 * genes.length * symmetry * segmentEyeGenes.length * eyeSymmetry;
	}
        
        @Override
         protected void randomGenes() {
		int nSegments = MIN_SEGMENTS + STUtils.random.nextInt(MAX_SEGMENTS-MIN_SEGMENTS+1); // 4 - 64
		if (nSegments % symmetry != 0)
		    nSegments += (symmetry - (nSegments % symmetry));
		int nGenes = nSegments / symmetry;
		genes = new NeuralGene[nGenes];
		for (int i=0; i<nGenes; i++) {
			genes[i] = new NeuralGene();
			genes[i].randomize();
		}
	}
         
     public SegmentEye[] synthesizeEyes(AliveAgent agent) 
        {
                SegmentEye[] eyes = new SegmentEye[getNEyeGenes() * getEyeSymmetry()];
		SegmentFactory factory = SegmentFactory.getInstance();
		
		for (int i = 0; i < eyes.length; i++)
			eyes[i] = (SegmentEye) factory.createSegment(agent, Pigment.SUPERMAGENTA);
		return eyes;
        }
     
     public Brain synthesizeBrain(AliveAgent agent)
     {
         Brain brain;
         BrainFactory factory = BrainFactory.getInstance();
         
         brain = factory.createBrain(agent, getBrainGene().getBrainType());
         return brain;
     }

//    public void randomEyes()
//    {
//     //   eyes.add(new SegmentEye());
//    }

    public int getNEyeGenes()
    {
        return segmentEyeGenes.length;
    }

    public EyeGene getEyeGene(int i)
    {
        return segmentEyeGenes[i];
    }
    
    public BrainGene getBrainGene()
    {
        return brainGene;
    }

    //TODO: create random number of eye genes, and ask each eye to randomize itself, following the same model as random genes.
    private void randomSegmentEyeGenes()
    {
       int nSegments = MIN_SEGMENTS + STUtils.random.nextInt(MAX_SEGMENTS-MIN_SEGMENTS+1); // 4 - 64
		if (nSegments % eyeSymmetry != 0)
		    nSegments += (eyeSymmetry - (nSegments % eyeSymmetry));
		int nGenes = nSegments / eyeSymmetry;
		segmentEyeGenes = new SegmentEyeGene[nGenes];
		for (int i=0; i<nGenes; i++) {
			segmentEyeGenes[i] = new SegmentEyeGene();
			segmentEyeGenes[i].randomize();
		}
    }

    //TODO: create brain gene, and ask it to randomize itself, following the same model as randomGenes.
    private void randomBrainGenes()
    {
        brainGene = new BrainGene();
        brainGene.randomize();
    }
    
    
    @Override
    public void draw(Graphics g, int width, int height) {
		int[][] x0 = new int[symmetry][genes.length];
		int[][] y0 = new int[symmetry][genes.length];
		int[][] x1 = new int[symmetry][genes.length];
		int[][] y1 = new int[symmetry][genes.length];
		int maxX = 0;
		int minX = 0;
		int maxY = 0;
		int minY = 0;
		double scale = 1.0;
		Vector2D v = new Vector2D();
		Graphics2D g2 = (Graphics2D) g;

		for (int i=0; i<symmetry; i++) {
			for (int j=0; j<genes.length; j++) {
				v.setModulus(genes[j].getLength());
				if (j==0) {
					x0[i][j]=y0[i][j]=0;
					if (!mirror || i%2==0)
						v.setTheta(genes[j].getTheta()+i*2*Math.PI/symmetry);
					else {
						v.setTheta(genes[j].getTheta()+(i-1)*2*Math.PI/symmetry);
						v.invertX();
					}
				} else {
					x0[i][j] = x1[i][j-1];
					y0[i][j] = y1[i][j-1];
					if (!mirror || i%2==0)
						v.addDegree(genes[j].getTheta());
					else
						v.addDegree(-genes[j].getTheta());
				}
				
				x1[i][j] = (int) Math.round(v.getX() + x0[i][j]);
				y1[i][j] = (int) Math.round(v.getY() + y0[i][j]);
				
				maxX = Math.max(maxX, Math.max(x0[i][j], x1[i][j]));
				maxY = Math.max(maxY, Math.max(y0[i][j], y1[i][j]));
				minX = Math.min(minX, Math.min(x0[i][j], x1[i][j]));
				minY = Math.min(minY, Math.min(y0[i][j], y1[i][j]));
			}
		}
		
		g2.translate(width/2, height/2);
		if (maxX-minX > width)
			scale = (double)width/(double)(maxX-minX);
		if (maxY-minY > height)
			scale = Math.min(scale, (double)height/(double)(maxY-minY));
		g2.scale(scale, scale);
		
		for (int i=0; i<symmetry; i++) {
			for (int j=0; j<genes.length; j++) {
				x0[i][j]+=(-minX-maxX)/2;
				x1[i][j]+=(-minX-maxX)/2;
				y0[i][j]+=(-minY-maxY)/2;
				y1[i][j]+=(-minY-maxY)/2;
				g2.setColor(genes[j].getColor());
				g2.drawLine(x0[i][j],y0[i][j],x1[i][j],y1[i][j]);
			}
		}
                
//                
//                
//                for (int i=0; i<symmetry; i++) {
//			for (int j=0; j<eyeGenes.length; j++) {
//				v.setModulus(((SegmentEyeGene) eyeGenes[j]).getLength());
//				if (j==0) {
//					x0[i][j]=y0[i][j]=0;
//					if (!mirror || i%2==0)
//						v.setTheta(((SegmentEyeGene)eyeGenes[j]).getTheta()+i*2*Math.PI/symmetry);
//					else {
//						v.setTheta(((SegmentEyeGene)eyeGenes[j]).getTheta()+(i-1)*2*Math.PI/symmetry);
//						v.invertX();
//					}
//				} else {
//					x0[i][j] = x1[i][j-1];
//					y0[i][j] = y1[i][j-1];
//					if (!mirror || i%2==0)
//						v.addDegree(((SegmentEyeGene)eyeGenes[j]).getTheta());
//					else
//						v.addDegree(-((SegmentEyeGene)eyeGenes[j]).getTheta());
//				}
//				
//				x1[i][j] = (int) Math.round(v.getX() + x0[i][j]);
//				y1[i][j] = (int) Math.round(v.getY() + y0[i][j]);
//				
//				maxX = Math.max(maxX, Math.max(x0[i][j], x1[i][j]));
//				maxY = Math.max(maxY, Math.max(y0[i][j], y1[i][j]));
//				minX = Math.min(minX, Math.min(x0[i][j], x1[i][j]));
//				minY = Math.min(minY, Math.min(y0[i][j], y1[i][j]));
//			}
//		}
//		
//		g2.translate(width/2, height/2);
//		if (maxX-minX > width)
//			scale = (double)width/(double)(maxX-minX);
//		if (maxY-minY > height)
//			scale = Math.min(scale, (double)height/(double)(maxY-minY));
//		g2.scale(scale, scale);
//		
//		for (int i=0; i<symmetry; i++) {
//			for (int j=0; j<eyeGenes.length; j++) {
//				x0[i][j]+=(-minX-maxX)/2;
//				x1[i][j]+=(-minX-maxX)/2;
//				y0[i][j]+=(-minY-maxY)/2;
//				y1[i][j]+=(-minY-maxY)/2;
//				g2.setColor(((SegmentEyeGene)eyeGenes[j]).getColor());
//				g2.drawLine(x0[i][j],y0[i][j],x1[i][j],y1[i][j]);
//			}
//		}
              
	}
}
    
    
    
    

