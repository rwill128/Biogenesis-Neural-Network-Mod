package geneticcodes;

import genes.EyeGene;
import genes.NeuralGene;
import agents.AliveAgent;
import auxiliar.Vector2D;
import biogenesis.Utils;
import brains.Brain;
import brains.BrainFactory;
import eyes.SegmentEye;
import genes.BrainGene;
import genes.Gene;
import genes.SegmentEyeGene;
import static geneticcodes.GeneticCode.MAX_SEGMENTS;
import static geneticcodes.GeneticCode.MIN_SEGMENTS;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import organisms.Pigment;
import segments.SegmentFactory;

/**
 *
 */
public class NeuralGeneticCode extends GeneticCode
{
    
        protected BrainGene brainGene;
        
        private SegmentEyeGene[] segmentEyeGenes;
        
        
        public int eyeSymmetry;
	protected boolean eyeMirror;
        
        protected void randomSegmentEyeMirror() { eyeMirror = Utils.random.nextBoolean(); }
        public boolean getEyeMirror() { return eyeMirror; }
        public void setEyeMirror(boolean eyeMirror) { this.eyeMirror = eyeMirror; }
        
        private void randomBrainGene()
        {
            brainGene = new BrainGene();
            brainGene.randomize();
        }
        
        
        public int getNSegmentEyeGenes() { return segmentEyeGenes.length; }
        public SegmentEyeGene getSegmentEyeGene (int i) { return segmentEyeGenes[i]; }
        public SegmentEyeGene[] getEyeGenes() { return segmentEyeGenes; }

        protected void randomSegmentEyeSymmetry() { eyeSymmetry = Utils.random.nextInt(8)+1; } 
        public int getEyeSymmetry() { return eyeSymmetry; }
        public void setEyeSymmetry(int eyeSymmetry) { this.eyeSymmetry = eyeSymmetry; }
        
        public BrainGene getBrainGene() { return brainGene; }

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
                super();
                randomSegmentEyeMirror();
                randomSegmentEyeSymmetry();
                randomEyeGenes();
                randomBrainGene();
	}
        
//        public NeuralGeneticCode(List<Gene> genes, int symmetry, boolean mirror, boolean disperseChildren) 
//        {
//		super(genes, symmetry, mirror, disperseChildren);
//	}
        
        public NeuralGeneticCode(List<Gene> genes, int symmetry, boolean mirror, List<EyeGene> eyeGenes, int eyeSymmetry, boolean eyeMirror, boolean disperseChildren) 
        {
            super(genes, symmetry, mirror, disperseChildren);
               
                int nSegmentEyeGenes = eyeGenes.size();
		this.segmentEyeGenes = new SegmentEyeGene[nSegmentEyeGenes];
		eyeGenes.toArray(this.segmentEyeGenes);
                this.eyeMirror = eyeMirror;
                this.eyeSymmetry = eyeSymmetry;              
	}
    
	public NeuralGeneticCode(NeuralGeneticCode parentCode) 
        {
            int i,j;
		int addedGene = -1;
		int removedGene = -1;
		int nGenes;
		boolean randomLength;
		boolean randomTheta;
		boolean randomColor;
		boolean randomBack;
		
		if (Utils.randomMutation())
			randomMirror();
		else
			mirror = parentCode.getMirror();
		if (Utils.randomMutation()) {
			// change symmetry
			if (Utils.random.nextInt(10) < 2)
				randomSymmetry();
			else
				symmetry = Utils.between(symmetry+Utils.randomSign(), 1, 8);
			nGenes = parentCode.getNGenes();
			if (nGenes * symmetry > MAX_SEGMENTS) {
				symmetry = parentCode.getSymmetry();
			}
		} else {
			// keep symmetry
			symmetry = parentCode.getSymmetry();
			if (Utils.randomMutation()) {
			// change number of segments
				if (Utils.random.nextBoolean()) {
				// increase segments
					if (parentCode.getNGenes() * parentCode.getSymmetry() >= MAX_SEGMENTS)
						nGenes = parentCode.getNGenes();
					else {
						nGenes = parentCode.getNGenes() + 1;
						addedGene = Utils.random.nextInt(nGenes);
					}
				} else {
				// decrease segments
					if (parentCode.getNGenes() * parentCode.getSymmetry() <= MIN_SEGMENTS)
						nGenes = parentCode.getNGenes();
					else {
						nGenes = parentCode.getNGenes() - 1;
						removedGene = Utils.random.nextInt(parentCode.getNGenes());
					}
				}
			} else {
			// keep number of segments
				nGenes = parentCode.getNGenes();
			}
		}
		// Create genes
		genes = new Gene[nGenes];
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
			if (Utils.randomMutation())
				randomLength = true;
			if (Utils.randomMutation())
				randomTheta = true;
			if (Utils.randomMutation())
				randomColor = true;
			if (Utils.randomMutation())
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

		if (Utils.randomMutation())
			randomDisperseChildren();
		else
			disperseChildren = parentCode.getDisperseChildren();
		calculateReproduceEnergy();
		maxAge = Utils.getMAX_AGE();
                
                reproduceEyeSegmentsWithMutations(parentCode);
                
                if (Utils.randomMutation()) {
                    if (brainGene != null)
                    brainGene.randomize();
                    else {
                        brainGene = new BrainGene();
                        brainGene.randomize();
                    }
                }
    //            adjustReproduceEnergy();
	}
        
        
        
        @Override
        protected void randomGenes() {
		int nSegments = MIN_SEGMENTS + Utils.random.nextInt(MAX_SEGMENTS-MIN_SEGMENTS+1); // 4 - 64
		if (nSegments % symmetry != 0)
		    nSegments += (symmetry - (nSegments % symmetry));
		int nGenes = nSegments / symmetry;
		genes = new NeuralGene[nGenes];
		for (int i=0; i<nGenes; i++) {
			genes[i] = new NeuralGene();
			genes[i].randomize();
		}
	}
       
        protected void reproduceEyeSegmentsWithMutations(NeuralGeneticCode parentCode) 
        {
                int i,j;
		int addedGene = -1;
		int removedGene = -1;
		int nEyeGenes;
		boolean randomLength;
		boolean randomTheta;
		boolean randomColor;
		boolean randomBack;
            //Create new segments from parent genetic code.
		if (Utils.randomMutation())
			randomSegmentEyeMirror();
		else
			eyeMirror = parentCode.getEyeMirror();
		if (Utils.randomMutation()) {
			// change symmetry
			if (Utils.random.nextInt(10) < 2)
				randomSegmentEyeSymmetry();
			else
				eyeSymmetry = Utils.between(eyeSymmetry+Utils.randomSign(), 1, 8);
			nEyeGenes = parentCode.getNSegmentEyeGenes();
			if (nEyeGenes * eyeSymmetry > MAX_SEGMENTS) {
				eyeSymmetry = parentCode.getEyeSymmetry();
			}
		} else {
			// keep symmetry
			eyeSymmetry = parentCode.getEyeSymmetry();
			if (Utils.randomMutation()) {
			// change number of segments
				if (Utils.random.nextBoolean()) {
				// increase segments
					if (parentCode.getNSegmentEyeGenes() * parentCode.getEyeSymmetry() >= MAX_SEGMENTS)
						nEyeGenes = parentCode.getNSegmentEyeGenes();
					else {
						nEyeGenes = parentCode.getNSegmentEyeGenes() + 1;
						addedGene = Utils.random.nextInt(nEyeGenes);
					}
				} else {
				// decrease segments
					if (parentCode.getNSegmentEyeGenes() * parentCode.getEyeSymmetry() <= MIN_SEGMENTS)
						nEyeGenes = parentCode.getNSegmentEyeGenes();
					else {
						nEyeGenes = parentCode.getNSegmentEyeGenes() - 1;
						removedGene = Utils.random.nextInt(parentCode.getNSegmentEyeGenes());
					}
				}
			} else {
			// keep number of segments
				nEyeGenes = parentCode.getNSegmentEyeGenes();
			}
		}
		// Create genes
		segmentEyeGenes = new SegmentEyeGene[nEyeGenes];
		for (i=0,j=0; i<nEyeGenes; i++,j++) {
			if (removedGene == j) {
				i--;
				continue;
			}
			if (addedGene == i) {
				segmentEyeGenes[i] = new SegmentEyeGene();
				segmentEyeGenes[i].randomize();
				j--;
				continue;
			}
			randomLength = randomTheta =   false;
			if (Utils.randomMutation())
				randomLength = true;
			if (Utils.randomMutation())
				randomTheta = true;
			if (randomLength || randomTheta) {
				segmentEyeGenes[i] = new SegmentEyeGene();
				if (randomLength)
					segmentEyeGenes[i].randomizeLength();
				else
					segmentEyeGenes[i].setLength(parentCode.getSegmentEyeGene(j).getLength());
				if (randomTheta)
					segmentEyeGenes[i].randomizeTheta();
				else
					segmentEyeGenes[i].setTheta(parentCode.getSegmentEyeGene(j).getTheta());
				segmentEyeGenes[i].setPigment(parentCode.getSegmentEyeGene(j).getPigment());
			} else
				segmentEyeGenes[i] = parentCode.getSegmentEyeGene(j);
		}
        }
        
        /**
	 * Calculates the energy required to reproduce this genetic code.
	 * This energy is 40 plus 3 for each segment.
	 */
	protected void adjustReproduceEnergy() {
		reproduceEnergy += segmentEyeGenes.length * eyeSymmetry;
	}
        
        @Override
	public Object clone() {
              
                NeuralGeneticCode newCode = (NeuralGeneticCode) super.clone();
                
                newCode.setEyeMirror(this.eyeMirror);
                
                newCode.setEyeSymmetry(this.eyeSymmetry);
                
                newCode.segmentEyeGenes = new SegmentEyeGene[segmentEyeGenes.length];
		for (int i=0; i<segmentEyeGenes.length; i++) {
                    newCode.segmentEyeGenes[i] = (SegmentEyeGene) segmentEyeGenes[i].clone();
                }
		return newCode;
	}
        
        
        private void randomEyeGenes()
        {
            int nSegmentEyes = MIN_SEGMENTS + Utils.random.nextInt(MAX_SEGMENTS-MIN_SEGMENTS+1); // 4 - 64
		if (nSegmentEyes % eyeSymmetry != 0)
		    nSegmentEyes += (eyeSymmetry - (nSegmentEyes % eyeSymmetry));
		int nSegmentEyeGenes = nSegmentEyes / eyeSymmetry;
		segmentEyeGenes = new SegmentEyeGene[nSegmentEyeGenes];
		for (int i=0; i<nSegmentEyeGenes; i++) {
			segmentEyeGenes[i] = new SegmentEyeGene();
			segmentEyeGenes[i].randomize();
		}
        }
                 
        public SegmentEye[] synthesizeEyes(AliveAgent agent) 
        {
                SegmentEye[] eyes = new SegmentEye[getNSegmentEyeGenes() * getEyeSymmetry()];
		SegmentFactory factory = SegmentFactory.getInstance();
		
		for (int i = 0; i < eyes.length; i++)
			eyes[i] = (SegmentEye) factory.createSegment(agent, Pigment.EYE);
		return eyes;
        }
     
        public Brain synthesizeBrain(AliveAgent agent)
        {
            Brain brain;
            BrainFactory factory = BrainFactory.getInstance();
         
            brain = factory.createBrain(agent, new BrainGene().getBrainType());
            return brain;
        }

//public void randomEyes()
//    {
//       eyes.add(new SegmentEye());
//    }

   
    
    
    @Override
    public void draw(Graphics g, int width, int height) {
                        drawEyes(g, width, height);

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
		
		//g2.translate(width/2, height/2);
		if (maxX-minX > width)
			scale = (double)width/(double)(maxX-minX);
		if (maxY-minY > height)
			scale = Math.min(scale, (double)height/(double)(maxY-minY));
		//g2.scale(scale, scale);
		
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
	}
    
    public void drawEyes(Graphics g, int width, int height) 
    {
            int[][] x0 = new int[eyeSymmetry][segmentEyeGenes.length];
            int[][] y0= new int[eyeSymmetry][segmentEyeGenes.length];
            int[][] x1 = new int[eyeSymmetry][segmentEyeGenes.length];
            int[][] y1 = new int[eyeSymmetry][segmentEyeGenes.length];
            int maxX = 0;
            int minX = 0;
            int maxY = 0;
            int minY = 0;
            double scale = 1.0;
            Vector2D v = new Vector2D();
            Graphics2D g2 = (Graphics2D) g;

		for (int i=0; i<eyeSymmetry; i++) {
			for (int j=0; j<segmentEyeGenes.length; j++) {
				v.setModulus(segmentEyeGenes[j].getLength());
				if (j==0) {
					x0[i][j]=y0[i][j]=0;
					if (!eyeMirror || i%2==0)
						v.setTheta(segmentEyeGenes[j].getTheta()+i*2*Math.PI/eyeSymmetry);
					else {
						v.setTheta(segmentEyeGenes[j].getTheta()+(i-1)*2*Math.PI/eyeSymmetry);
						v.invertX();
					}
				} else {
					x0[i][j] = x1[i][j-1];
					y0[i][j] = y1[i][j-1];
					if (!eyeMirror || i%2==0)
						v.addDegree(segmentEyeGenes[j].getTheta());
					else
						v.addDegree(-segmentEyeGenes[j].getTheta());
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
		
		for (int i=0; i<eyeSymmetry; i++) {
			for (int j=0; j<segmentEyeGenes.length; j++) {
				x0[i][j]+=(-minX-maxX)/2;
				x1[i][j]+=(-minX-maxX)/2;
				y0[i][j]+=(-minY-maxY)/2;
				y1[i][j]+=(-minY-maxY)/2;
				g2.setColor(segmentEyeGenes[j].getColor());
				g2.drawLine(x0[i][j],y0[i][j],x1[i][j],y1[i][j]);
			}
		}
    }
}
    
    
    
    

