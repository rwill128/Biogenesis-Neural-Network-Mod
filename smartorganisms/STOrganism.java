package smartorganisms;

import agents.Agent;
import auxiliar.Vector2D;
import biogenesis.Utils;
import brains.Brain;
import eyes.SegmentEye;
import genes.SegmentEyeGene;
import geneticcodes.NeuralGeneticCode;
import intentionalmover.BCyanSegment;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import organisms.BaseOrganism;
import organisms.Organism;
import organisms.SegmentBasedOrganism;
import segments.Segment;
import stbiogenesis.STUtils;
import world.World;

/**
 *
 */
public class STOrganism extends Organism implements SeeingAgent, ThinkingAgent
{
    
    Brain brain;
    SegmentEye[] segmentEyes;
    private int numSegmentEyesPerEyeAppendage;
    List<Segment> legs = new ArrayList<>();
    private int numLegs;
    private int eyeSymmetry;
    private boolean eyeMirror;
    private Vector2D sightV = new Vector2D();
    
    public int getNumEyes() { return segmentEyes.length; }
    
    public SegmentEye getEyeSegment(int i) { return segmentEyes[i]; }
    public SegmentEye[] getEyeSegments() { return segmentEyes; }
    public int getNumLegs() { return numLegs; }    
    
    public List<Segment> getLegs() { return legs; }
        
    public STOrganism(World world) 
    {
	this(world, new NeuralGeneticCode());
    }
    
        
    public STOrganism(World world, NeuralGeneticCode nGeneticCode) 
    {
	super(world, nGeneticCode);
        segmentEyes = nGeneticCode.synthesizeEyes(this);
        numSegmentEyesPerEyeAppendage = segmentEyes.length / nGeneticCode.getEyeSymmetry();

        brain = nGeneticCode.synthesizeBrain(this);
        initEyesAndLegs();
    }
    
    public void initEyesAndLegs() 
    {
		eyeSymmetry = ((NeuralGeneticCode) getGeneticCode()).getEyeSymmetry();
                eyeMirror = ((NeuralGeneticCode) getGeneticCode()).getEyeMirror();
                
                updateEyes();
                
                recalculateSize();
		for (Segment s : getSegments())
                {
			if (s instanceof BCyanSegment) {
                                numLegs++;
                                legs.add(s);
                        }
                }
		nChildren = Utils.between(nChildren, 1, 8);
	}
    
    @Override
	public NeuralGeneticCode getGeneticCode() {
		return (NeuralGeneticCode) geneticCode;
	}
       
    protected void updateEyes() 
    {
	int i,j,segment=0;
	NeuralGeneticCode geneticCode = (NeuralGeneticCode) getGeneticCode();
	SegmentEye[] eyeSegments = getEyeSegments();
	SegmentEyeGene gene;
	int sequence = eyeSegments.length / eyeSymmetry;
		
	for (i=0; i<eyeSymmetry; i++) {
            for (j=0; j<sequence; j++,segment++) {
            // Here, we take the vector that forms the segment, scale it depending on
            // the relative size of the organism and rotate it depending on the
            // symmetry and mirroring.
            gene = geneticCode.getSegmentEyeGene(j);
            sightV.setModulus(gene.getLength()/STUtils.scale[growthRatio-1]);
            if (j==0) {
                eyeSegments[segment].setStartingPoint(0, 0);
		if (!eyeMirror || i%2==0) {
                    sightV.setTheta(gene.getTheta()+i*2*Math.PI/eyeSymmetry);
                } else {
                    sightV.setTheta(gene.getTheta()+(i-1)*2*Math.PI/eyeSymmetry);
                    sightV.invertX();
		}
            } else {
                eyeSegments[segment].setStartingPoint(eyeSegments[segment-1].getEndingPoint());
		if (!eyeMirror || i%2==0)
                    sightV.addDegree(gene.getTheta());
                else
                    sightV.addDegree(-gene.getTheta());
            }
            // Apply the vector to the starting point to get the ending point.
            eyeSegments[segment].setEndingPoint((int) Math.round(sightV.getX() +
            eyeSegments[segment].getStartingPoint().x),
            (int) Math.round(sightV.getY() + eyeSegments[segment].getStartingPoint().y));
            }
        }
    }
    
    /**
     *
     */
    @Override
        protected void recalculateSize() 
    {
		double newMass = 0;
		double inertia = 0;
		for (Segment s : getSegments()) {
			// calculate points distance of the origin and modulus
			s.updateMass();
			newMass += s.getMass();
			// add the effect of this segment, following the parallel axis theorem
			inertia += s.getInertia();
		}
		setInertia(inertia);
		setMass(newMass);
}
    
    @Override
    public void draw(Graphics g) 
    {
	super.draw(g);
        boolean showColor = getColor()==null;
		
        for (Segment s : segments)
            s.draw(g, showColor);
  
         for (SegmentEye s : segmentEyes)
             s.draw(g, showColor);
    }
    
    @Override
    public void draw(Graphics g, int width, int height) 
    {
       getGeneticCode().draw(g, width, height);
    }
    
    
   @Override
	public boolean update() 
   {
		initUpdate();
                //Think about self and world.
                brain.think(this, world);
		// Apply segment effects for this frame.
		segmentsFrameEffects();
		// Apply rubbing effects
		rubbingFrameEffects();
		// Check if it can grow or shrink
		grow();
		moved = move();
		if (checkReproduce())
			if (reproduce())
				timeToReproduce = 20;
		checkMaxEnergy();
		// Maintenance
		return breath();
	}
   
   @Override
	public boolean contact(Agent agent) 
   {
		if (agent instanceof SegmentBasedOrganism) {
			SegmentBasedOrganism org = (SegmentBasedOrganism) agent;
			List<Segment> contactingSegments = findPossibleContactingSegments(org);
			List<Segment> otherContactingSegments = org.findPossibleContactingSegments(this);
			
			for (Segment s : contactingSegments) {
				for (Segment sorg : otherContactingSegments) {
					if (s.intersectsLine(sorg)) {
						// If we found two intersecting segments, apply effects
						touchEffects(s, sorg);
						// Intersection point
						Point2D intersec= s.getIntersection(sorg);
						/* touchMove needs to know which is the line that collides from the middle (not
						 * from a vertex). Try to guess it by finding the vertex nearest to the
						 * intersection point.
						 */
						double dl1, dl2, dbl1, dbl2;
						dl1 = intersec.distanceSq(s.getP1());
						dl2 = intersec.distanceSq(s.getP2());
						dbl1 = intersec.distanceSq(sorg.getP1());
						dbl2 = intersec.distanceSq(sorg.getP2());
						// Use this to send the best choice to touchMove
						if (Math.min(dl1, dl2) < Math.min(dbl1, dbl2))
							org.touchMove(this, intersec, sorg);
						else
							touchMove(org, intersec, s);
						// Find only one collision to speed up.
						return true;
					}
				}
			}
		}
		return false;
	}
   
    @Override
   public List<Segment> findPossibleContactingSegments(BaseOrganism otherAgent) {
		List<Segment> contactingSegments = new ArrayList<Segment>(segments.length);
		// Check collisions for all segments
		for (Segment s : segments) {
			// Consider only segments with modulus greater than 1
			// First check if the line intersects the bounding box of the other organism
			if (s.getMass() >= 1 && otherAgent.intersectsLine(s)) {
				contactingSegments.add(s);
			}
		}
		return contactingSegments;
	}
   
   @Override
   protected void calculateBounds(boolean force) {
		double left=java.lang.Double.MAX_VALUE, right=java.lang.Double.MIN_VALUE, 
		top=java.lang.Double.MAX_VALUE, bottom=java.lang.Double.MIN_VALUE;
		double lastCalc=0;
		int segmentNum=0;
		for (Segment s : getSegments()) {
			/* Save calculation: if rotation hasn't changed and it is not forced,
			 * don't calculate points again.
			 */
			if (lastTheta != getTheta() || force)
				if (STUtils.isEFFICIENCY_MODE()) {
					/* Save results internal to recalculate for reuse in the next segment
					 */
					if (segmentNum % numSegmentsPerAppendage == 0)
						lastCalc = s.recalculate(getTheta(), getPosX(), getPosY(), 0.0);
					else
						lastCalc = s.recalculate(getTheta(), getPosX(), getPosY(), lastCalc);
					segmentNum++;
				} else {
					s.recalculate(getTheta(), getPosX(), getPosY());
				}
			else
				s.setCenter(getPosX(), getPosY());
			// Finds the rectangle that comprises the organism
			left = STUtils.min(left, s.getX1(), s.getX2());
			right = STUtils.max(right, s.getX1(), s.getX2());
			top = STUtils.min(top, s.getY1(), s.getY2());
			bottom = STUtils.max(bottom, s.getY1(), s.getY2());
		}
                
                for (Segment s : getEyeSegments()) {
			/* Save calculation: if rotation hasn't changed and it is not forced,
			 * don't calculate points again.
			 */
			if (lastTheta != getTheta() || force)
				if (STUtils.isEFFICIENCY_MODE()) {
					/* Save results internal to recalculate for reuse in the next segment
					 */
					if (segmentNum % numSegmentEyesPerEyeAppendage == 0)
						lastCalc = s.recalculate(getTheta(), getPosX(), getPosY(), 0.0);
					else
						lastCalc = s.recalculate(getTheta(), getPosX(), getPosY(), lastCalc);
					segmentNum++;
				} else {
					s.recalculate(getTheta(), getPosX(), getPosY());
				}
			else
				s.setCenter(getPosX(), getPosY());
			// Finds the rectangle that comprises the organism
			left = STUtils.min(left, s.getX1(), s.getX2());
			right = STUtils.max(right, s.getX1(), s.getX2());
			top = STUtils.min(top, s.getY1(), s.getY2());
			bottom = STUtils.max(bottom, s.getY1(), s.getY2());
		}
                
		left = Math.round(left);
		right = Math.round(right);
		top = Math.round(top);
		bottom = Math.round(bottom);
		setBounds((int)left, (int)top, (int)(right-left+1)+1, (int)(bottom-top+1)+1);
	}

    

//    private int countLegs()
//    {
//        int numLegs = 0;
//        for (int i = 0; i < this.getSegments().length; i++)
//        {
//            if (this.getSegment(i).getColor() == Pigment.BCYAN.getColor()) {
//                numLegs++;
//            }
//       }
//        return numLegs;
//    }
//
//    private void initLegs()
//    {
//         legs = new Segment[countLegs()];
//         
//         int legCounter = 0;
//         
//         for (int i = 0; i < this.getSegments().length; i++)
//        {
//            if (this.getSegment(i).getColor() == Pigment.BCYAN.getColor()) {
//                legs[legCounter] = this.getSegment(i);
//                legCounter++;
//            }
//       }
//    }
}
