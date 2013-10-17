package smartorganisms;

import agents.Agent;
import auxiliar.Vector2D;
import biogenesis.Utils;
import brains.Brain;
import brains.StandardNNBrain;
import eyes.SegmentEye;
import genes.SegmentEyeGene;
import geneticcodes.NeuralGeneticCode;
import intentionalmover.BCyanSegment;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import organisms.Organism;
import organisms.SegmentBasedOrganism;
import segments.Segment;
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
    
    private Rectangle eyeBox = new Rectangle();
    private Rectangle lastEyeFrame = new Rectangle();
    protected static transient Vector2D sightV = new Vector2D();
    
    public int getNumEyes() { return segmentEyes.length; }
    public Rectangle getEyeBox() { return eyeBox; }
    
    public SegmentEye getEyeSegment(int i) { return segmentEyes[i]; }
    public SegmentEye[] getEyeSegments() { return segmentEyes; }
    public int getNumLegs() { return numLegs; }    
    
    public List<Segment> getLegs() { return legs; }
        
    public STOrganism(World world) 
    {
	this(world, new NeuralGeneticCode());
    }
    
    public STOrganism(SegmentBasedOrganism parent) {
		super(parent);
                segmentEyes = ((NeuralGeneticCode) geneticCode).synthesizeEyes(this);
                eyeSymmetry = ((NeuralGeneticCode) geneticCode).eyeSymmetry;
        eyeMirror = ((NeuralGeneticCode) geneticCode).getEyeMirror();
                numSegmentEyesPerEyeAppendage = segmentEyes.length / ((NeuralGeneticCode) geneticCode).getEyeSymmetry();
                         updateEyes();

                brain = ((NeuralGeneticCode) geneticCode).synthesizeBrain(this);
                 initLegs();
	}
    
    public STOrganism(World world, NeuralGeneticCode nGeneticCode) 
    {
	super(world, nGeneticCode);
        
        segmentEyes = ((NeuralGeneticCode) geneticCode).synthesizeEyes(this);
        eyeSymmetry = ((NeuralGeneticCode) geneticCode).eyeSymmetry;
        eyeMirror = ((NeuralGeneticCode) geneticCode).getEyeMirror();
        numSegmentEyesPerEyeAppendage = segmentEyes.length / eyeSymmetry;
        updateEyes();
        

        brain = nGeneticCode.synthesizeBrain(this);
        initLegs();
    }
    
    @Override
    public void segmentsFrameEffects() {
		if (isAlive()) {
			for (Segment s : segments)
				s.frameEffects();
                        for (Segment s : segmentEyes)
				s.frameEffects();
		}
	}
    
    @Override
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setBackground(Color.BLACK);
		g.clearRect(0,0,width,height);
		for (Segment s : getSegments())
			s.draw(g, -x + getPosX(), -y + getPosY(), true);
                for (Segment s : getEyeSegments())
			s.draw(g, -x + getPosX(), -y + getPosY(), true);
		return image;
	}
    
    @Override
	public boolean reproduce() {
		boolean hasReproduced = false;
		Organism newOrg;
		double reproduceEnergy = getInfectedGeneticCode() != null ? getInfectedGeneticCode().getReproduceEnergy() : 
				getGeneticCode().getReproduceEnergy();
		
		for (int i=0; i < nChildren; i++) {
			newOrg = new STOrganism(this);
			newOrg.setEnergy(Math.min(reproduceEnergy / (nChildren+1), getEnergy()));
			
			if (getEnergy() >= newOrg.getEnergy()+Utils.getYELLOW_ENERGY_CONSUMPTION() &&
					newOrg.placeNear(this)) {
				// It can be created
				increaseChildren();
				setEnergy(getEnergy() - newOrg.getEnergy());
				if (i!=0)
					useEnergy(Utils.getYELLOW_ENERGY_CONSUMPTION());
				getWorld().addAgent(newOrg,this);
				setInfectedGeneticCode(null);
			}
			hasReproduced = true;
			
		}
		return hasReproduced;
	}
    
    @Override
    public boolean placeNear(SegmentBasedOrganism parent) {
		int nPos = Utils.random.nextInt(8);
		// Try to put it in any possible position, starting from a randomly chosen one.
		for (int nSide = 0; nSide < 8; nSide++) {
			// Calculate candidate position
			setPosition(parent.getPosX() + (parent.width / 2 + width / 2+ 1) * Utils.side[nPos][0],
					parent.getPosY() + (parent.height / 2 + height / 2 + 1) * Utils.side[nPos][1]);
			// Check this position is inside the world.
			if (getWorld().isInsideWorld(this) == World.INSIDE_WORLD) {
				// Check that it doesn't overlap with other organisms.
				if (getWorld().fastCheckHit(this) == null) {
					if (parent.getGeneticCode().getDisperseChildren()) {
						setDx(Utils.side[nPos][0]);
						setDy(Utils.side[nPos][1]);
					} else {
						setDx(parent.getDx());
						setDy(parent.getDy());
					}
					return true;
				}
			}
			nPos = (nPos + 1) % 8;
		}
		// It can't be placed.
		return false;
	}
    
    @Override
    public void initUpdate() 
    {
		moved = false;
		lastFrame.setBounds(this);
	}
    
    public void initLegs() 
    {                
		for (Segment s : getSegments())
                {
			if (s instanceof BCyanSegment) {
                                numLegs++;
                                legs.add(s);
                        }
                }
	}
    
    @Override
	public NeuralGeneticCode getGeneticCode() {
		return (NeuralGeneticCode) geneticCode;
	}
       
    protected void updateEyes() 
    {
	int i,j,segment=0;
	segmentEyes = getEyeSegments();
	SegmentEyeGene gene;
	int sequence = segmentEyes.length / eyeSymmetry;
	for (i=0; i<eyeSymmetry; i++) {
            for (j=0; j<sequence; j++,segment++) {
            // Here, we take the vector that forms the segment, scale it depending on
            // the relative size of the organism and rotate it depending on the
            // symmetry and mirroring.
            gene = ((NeuralGeneticCode) geneticCode).getSegmentEyeGene(j);
            sightV.setModulus(gene.getLength()/Utils.scale[growthRatio-1]);
            if (j==0) {
                segmentEyes[segment].setStartingPoint(0, 0);
		if (!eyeMirror || i%2==0) {
                    sightV.setTheta(gene.getTheta()+i*2*Math.PI/eyeSymmetry);
                } else {
                    sightV.setTheta(gene.getTheta()+(i-1)*2*Math.PI/eyeSymmetry);
                    sightV.invertX();
		}
            } else {
                segmentEyes[segment].setStartingPoint(segmentEyes[segment-1].getEndingPoint());
		if (!eyeMirror || i%2==0)
                    sightV.addDegree(gene.getTheta());
                else
                    sightV.addDegree(-gene.getTheta());
            }
            // Apply the vector to the starting point to get the ending point.
             segmentEyes[segment].setEndingPoint((int) Math.round(sightV.getX() +
            segmentEyes[segment].getStartingPoint().x),
            (int) Math.round(sightV.getY() + segmentEyes[segment].getStartingPoint().y));
            }
        }
        recalculateEyeSize();
    }
    protected void recalculateEyeSize() {
		double newMass = 0;
		double inertia = 0;
		for (Segment s : getEyeSegments()) {
			// calculate points distance of the origin and modulus
			s.updateMass();
			newMass += s.getMass();
			// add the effect of this segment, following the parallel axis theorem
			inertia += s.getInertia();
		}
		//setInertia(inertia);
		//setMass(newMass);
	}
    @Override
    public boolean spin() {
		if (growthRatio < 16) {
			growthRatio++;
			setTheta(getTheta() + (Math.PI / 6));
			updateBody();
                        updateEyes();
			calculateBounds(true);
                        
			return true;
		}
		return false;
	}
    
    
    @Override
    public void grow() {
		double energy = getEnergy();
		if (growthRatio > 1 && (getAge() & 0x07) == 0x07 && isAlive() && energy >= getMass()/10) {
			growthRatio--;
			double m = getMass();
			double I = getInertia();
			updateBody();
                        updateEyes();
			// Cynetic energy is constant. If mass changes, speed must also change.
			m = Math.sqrt(m/getMass());
			setDx(getDx() * m);
			setDy(getDy() * m);
			setDtheta(getDtheta() * Math.sqrt(I/getInertia()));
			hasGrown = 1;
		} else {
			if (growthRatio < 15 && energy < getMass()/12) {
				growthRatio++;
				double m = getMass();
				double I = getInertia();
				updateBody();
                                updateEyes();
				// Cynetic energy is constant. If mass changes, speed must also change.
				m = Math.sqrt(m/getMass());
				setDx(getDx() * m);
				setDy(getDy() * m);
				setDtheta(getDtheta() * Math.sqrt(I/getInertia()));
				hasGrown = -1;
			} else
				hasGrown = 0;
		}
	}
    
    /**
     *
     */
    
    @Override
	public void revive() {
		super.revive();
                for (int i = 0; i < segmentEyes.length; i++)
			segmentEyes[i].setAlive(true);
	}
    
        protected NeuralGeneticCode createChildGeneticCode() {
		NeuralGeneticCode inheritGeneticCode;
		
		// Create the inherited genetic code
		if (infectedGeneticCode != null)
			inheritGeneticCode = (NeuralGeneticCode) infectedGeneticCode;
		else
			inheritGeneticCode = (NeuralGeneticCode) geneticCode;
		return new NeuralGeneticCode(inheritGeneticCode);
	}    /**
	 * Kills the organism. Sets its segments to brown and tells the world
	 * about the event.
	 * 
	 * @param killingOrganism  The organism that has killed this organism,
	 * or null if it has died of natural causes.
	 */
	@Override
	public void die(Agent killingOrganism) {
		super.die(killingOrganism);
		for (Segment s : segmentEyes)
			s.setAlive(false);
	}
    
    @Override
    public void draw(Graphics g) 
    {
            super.draw(g);
		boolean showColor = getColor()==null;
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
                if (brain == null) {
                    brain = new StandardNNBrain(this);
                }
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
   
	public boolean eyeContact(Agent agent) {
            boolean seenSomething = false;
		if (agent instanceof SegmentBasedOrganism) {
			SegmentBasedOrganism org = (SegmentBasedOrganism) agent;
			List<SegmentEye> possibleSeeingEyes = findPossibleSeeingEyes(org);
			List<Segment> otherContactingSegments = org.findPossibleContactingSegments(this.eyeBox);
			
                        int seenSegments = 0;
                        
			for (Segment s : possibleSeeingEyes) {
				for (Segment sorg : otherContactingSegments) {
					if (s.intersectsLine(sorg)) {
                                            seenSomething = true;
                                            seenSegments++;
                                            s.touchEffects(sorg);
                                            // Find up to 5 collisions
                                            if (seenSegments >= 5)
						return true;
					}
				}
			}
		}
		return seenSomething;
	}


        
        public List<SegmentEye> findPossibleSeeingEyes(Rectangle otherAgentRectangle) {
		List<SegmentEye> seeingEyes = new ArrayList<SegmentEye>();
		// Check collisions for all segments
		for (SegmentEye s : segmentEyes) {
			// Consider only segments with modulus greater than 1
			// First check if the line intersects the bounding box of the other organism
			if (s.getMass() >= 1 && otherAgentRectangle.intersectsLine(s)) {
				seeingEyes.add(s);
			}
		}
		return seeingEyes;
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
				if (Utils.isEFFICIENCY_MODE()) {
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
			left = Utils.min(left, s.getX1(), s.getX2());
			right = Utils.max(right, s.getX1(), s.getX2());
			top = Utils.min(top, s.getY1(), s.getY2());
			bottom = Utils.max(bottom, s.getY1(), s.getY2());
		}
                
                double eyeLeft=java.lang.Double.MAX_VALUE, eyeRight=java.lang.Double.MIN_VALUE, 
		eyeTop=java.lang.Double.MAX_VALUE, eyeBottom=java.lang.Double.MIN_VALUE;
                double lastEyeCalc=0;
                int eyeSegmentNum=0;

                for (SegmentEye s : getEyeSegments()) {
			/* Save calculation: if rotation hasn't changed and it is not forced,
			 * don't calculate points again.
			 */
			if (lastTheta != getTheta() || force)
				if (Utils.isEFFICIENCY_MODE()) {
					/* Save results internal to recalculate for reuse in the next segment
					 */
					if (eyeSegmentNum % numSegmentEyesPerEyeAppendage == 0)
						lastEyeCalc = s.recalculate(getTheta(), getPosX(), getPosY(), 0.0);
					else
						lastEyeCalc = s.recalculate(getTheta(), getPosX(), getPosY(), lastEyeCalc);
					eyeSegmentNum++;
				} else {
					s.recalculate(getTheta(), getPosX(), getPosY());
				}
			else
				s.setCenter(getPosX(), getPosY());
			// Finds the rectangle that comprises the organism
			eyeLeft = Utils.min(eyeLeft, s.getX1(), s.getX2());
			eyeRight = Utils.max(eyeRight, s.getX1(), s.getX2());
			eyeTop = Utils.min(eyeTop, s.getY1(), s.getY2());
			eyeBottom = Utils.max(eyeBottom, s.getY1(), s.getY2());
		}
                
		left = Math.round(left);
		right = Math.round(right);
		top = Math.round(top);
		bottom = Math.round(bottom);
		setBounds((int)left, (int)top, (int)(right-left+1)+1, (int)(bottom-top+1)+1);
                
                eyeLeft = Math.round(eyeLeft);
		eyeRight = Math.round(eyeRight);
		eyeTop = Math.round(eyeTop);
		eyeBottom = Math.round(eyeBottom);
		eyeBox.setBounds((int)eyeLeft, (int)eyeTop, (int)(eyeRight-eyeLeft+1)+1, (int)(eyeBottom-eyeTop+1)+1);
	}
   
   
   
    @Override
    public boolean move() {
		// Movement
		boolean moved = false;
		double dx=getDx(), dy=getDy(), dtheta=getDtheta();
		offset(dx, dy, dtheta);
		calculateBounds(hasGrown!=0);
		
		if (hasGrown!=0 || dx!=0 || dy!=0 || dtheta!=0) {
			moved = true;
			if (collisionDetection()) {
				// If there is a collision, undo movement.
				moved = false;
				offset(-dx, -dy, -dtheta);
				if (hasGrown!=0) {
					growthRatio+=hasGrown;
					updateBody();
                                        updateEyes();
				}
				calculateBounds(hasGrown!=0);
			}
                        if (seesSomething()) {
                            //Where braii messages should acctually, instead of in the damn legs and eyes. Or in the seesSomething method.
                        }
		}
		return moved;
	}
    
    public boolean seesSomething() {
		// Collision detection with other organisms.
		if (getWorld().checkSees(this) != null)
			return true;
		return false;
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
