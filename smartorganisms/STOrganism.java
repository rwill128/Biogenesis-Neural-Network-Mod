/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smartorganisms;

import agents.Agent;
import brains.Brain;
import eyes.Eye;
import eyes.SegmentEye;
import geneticcodes.GeneticCode;
import geneticcodes.NeuralGeneticCode;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.List;
import organisms.Organism;
import organisms.Pigment;
import organisms.SegmentBasedOrganism;
import segments.Segment;
import world.World;

/**
 *
 * @author Rick Williams
 */
public class STOrganism extends Organism implements SeeingAgent, ThinkingAgent
{
    
    Brain brain;
    
    SegmentEye[] eyes;
    
    Segment[] legs;
    
   
    public STOrganism(SegmentBasedOrganism parent) 
    {
	super(parent);
    }
    
    public STOrganism(Organism parent) 
    {
	super(parent);
    }
    
    public STOrganism(STOrganism parent) 
    {
	super(parent);
    }
	
    public STOrganism(World world, GeneticCode geneticCode) 
    {
	super(world, geneticCode);
    }
    
    public STOrganism(World world) 
    {
	this(world, new NeuralGeneticCode());
    }
    
    public STOrganism(World world, NeuralGeneticCode nGeneticCode) 
    {
	super(world, nGeneticCode);
 //       initLegs();
        eyes = nGeneticCode.synthesizeEyes(this);
        brain = nGeneticCode.synthesizeBrain(this);
    }
    
    @Override
        public void draw(Graphics g) {
		super.draw(g);
                boolean showColor = getColor()==null;
			
                for (Segment s : segments)
                        s.draw(g, showColor);

  
                for (SegmentEye s : eyes)
                    s.draw(g, showColor);
	}
    
    
   @Override
	public boolean update() {
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
	public boolean contact(Agent agent) {
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

    private void initializeEyesAndBrain()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int getNumEyes()
    {
        return eyes.length;
    }
    
    public Eye getEye(int i) 
    {
        return eyes[i];
    }
    
    public int getNumLegs()
    {
        return legs.length;
    }
    
    public Segment[] getLegs() 
    {
        return legs;
    }

    private int countLegs()
    {
        int numLegs = 0;
        for (int i = 0; i < this.getSegments().length; i++)
        {
            if (this.getSegment(i).getColor() == Pigment.BCYAN.getColor()) {
                numLegs++;
            }
       }
        return numLegs;
    }

    private void initLegs()
    {
         legs = new Segment[countLegs()];
         
         int legCounter = 0;
         
         for (int i = 0; i < this.getSegments().length; i++)
        {
            if (this.getSegment(i).getColor() == Pigment.BCYAN.getColor()) {
                legs[legCounter] = this.getSegment(i);
                legCounter++;
            }
       }
         
      
    }
}
