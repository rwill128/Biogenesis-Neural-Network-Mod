package organisms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import biogenesis.Utils;

import segments.Segment;
import world.World;

public abstract class SegmentBasedOrganism extends BaseOrganism {
	private static final long serialVersionUID = 1L;
	private Segment[] segments;
	private int numSegmentsPerAppendage;
	/**
	 * Last frame angle, used to avoid calculating point rotations when the angle doesn't
	 * change between two consecutive frames.
	 */
	private double lastTheta = -1;
	/**
	 * Angular speed. Organism angle variation at every frame.
	 */
	private double dtheta = 0d;
	/**
	 * Speed. Variation applied to organism coordinates at every frame.
	 */
	private double dx = 0d;
	/**
	 * Speed. Variation applied to organism coordinates at every frame.
	 */
	private double dy = 0d;
	/**
	 * Moment of inertia of this organism, used to calculate the effect of collisions.
	 */
	private double inertia = 0;
	
	public SegmentBasedOrganism(SegmentBasedOrganism parent) {
		this(parent.getWorld(), parent.createChildGeneticCode(), parent);
	}
	
	public SegmentBasedOrganism(World world, GeneticCode geneticCode, SegmentBasedOrganism parent) {
		super(world, geneticCode, parent);
		// initialize body
		segments = geneticCode.synthesize(this);
		numSegmentsPerAppendage = segments.length / geneticCode.getSymmetry();
	}
	
	public Segment[] getSegments() {
		return segments;
	}
	@Override
	public void revive() {
		super.revive();
		for (int i = 0; i < segments.length; i++)
			segments[i].setAlive(true);
	}
	public void segmentsFrameEffects() {
		if (isAlive()) {
			for (Segment s : segments)
				s.frameEffects();
		}
	}
	/**
	 * Applies the effects produced by two touching segments.
	 * 
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment. 
	 * @param oseg  Index of the other organism's segment.
	 * or this method is called by this same method in the other organism. 
	 */
	public void touchEffects(Segment seg, Segment oseg) {
		AliveAgent otherAgent = oseg.getThisAgent();
		if ((getParentId() == otherAgent.getId() || getId() == otherAgent.getParentId()) 
				&& otherAgent.isAlive())
			return;
		seg.touchEffects(oseg);
	}
	/**
	 * Kills the organism. Sets its segments to brown and tells the world
	 * about the event.
	 * 
	 * @param killingOrganism  The organism that has killed this organism,
	 * or null if it has died of natural causes.
	 */
	@Override
	public void die(Agent killingOrganism) {
		super.die(killingOrganism);
		for (Segment s : segments)
			s.setAlive(false);
	}
	/**
	 * Draws this organism to a graphics context.
	 * The organism is drawn at its position in the world.
	 * 
	 * @param g  The graphics context to draw to.
	 */
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		boolean showColor = getColor()==null;
		
		for (Segment s : segments)
			s.draw(g, showColor);
	}
	
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
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setBackground(Color.BLACK);
		g.clearRect(0,0,width,height);
		for (Segment s : getSegments())
			s.draw(g, -x + getPosX(), -y + getPosY(), true);
		return image;
	}

	/**
	 * Calculates the position of all organism points in the world, depending on
	 * its rotation. It also calculates the bounding rectangle of the organism.
	 * This method must be called from outside this class only when doing
	 * manual drawing.  
	 * 
	 * @param force  To avoid calculations, segments position are only calculated
	 * if the organism's rotation has changed in the last frame. If it is necessary
	 * to calculate them even when the rotation hasn't changed, assign true to this
	 * parameter.
	 */
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
		left = Math.round(left);
		right = Math.round(right);
		top = Math.round(top);
		bottom = Math.round(bottom);
		setBounds((int)left, (int)top, (int)(right-left+1)+1, (int)(bottom-top+1)+1);
	}

	public void initUpdate() {
		super.initUpdate();
		lastTheta = getTheta();
	}

	
	protected double pushX(Point2D p) {
		return 0;
	}
	protected double pushY(Point2D p) {
		return 0;
	}

	@Override
	public double getDtheta() {
		return dtheta;
	}

	@Override
	public double getDx() {
		return dx;
	}

	@Override
	public double getDy() {
		return dy;
	}

	@Override
	public double getInertia() {
		return inertia;
	}

	@Override
	public void setDtheta(double dtheta) {
		this.dtheta = dtheta;
	}

	@Override
	public void setDx(double dx) {
		this.dx = dx;
	}

	@Override
	public void setDy(double dy) {
		this.dy = dy;
	}

	public void setInertia(double _I) {
		this.inertia = _I;
	}

	/**
	 * Calculates the resulting speeds after a collision between two organisms, following
	 * physical rules.
	 * 
	 * @param org  The other organism in the collision.
	 * @param p  Intersection point between the organisms.
	 * @param l  Line that has collided. Of the two lines, this is the one that collided
	 * on the center, not on the vertex.
	 */
	public void touchMove(SegmentBasedOrganism org, Point2D p, Line2D l) {
		double orgdCenterX = org.getPosX();
		double orgdCenterY = org.getPosY();
		double orgdx = org.getDx();
		double orgdy = org.getDy();
		double orgdtheta = org.getDtheta();
		double orgmass = org.getMass();
		double orgI = org.getInertia();
		// Distance vector between centers of mass and p
		double rapx = p.getX() - getPosX();
		double rapy = p.getY() - getPosY();
		double rbpx = p.getX() - orgdCenterX;
		double rbpy = p.getY() - orgdCenterY;
		// Speeds of point p in the body A and B, before collision.
		double vap1x = dx - dtheta * rapy + pushX(p);
		double vap1y = dy + dtheta * rapx + pushY(p);
		double vbp1x = orgdx - orgdtheta * rbpy;
		double vbp1y = orgdy + orgdtheta * rbpx;
		// Relative speeds between the two collision points.
		double vab1x = vap1x - vbp1x;
		double vab1y = vap1y - vbp1y;
		// Normal vector to the impact line
		//First: perpendicular vector to the line
		double nx = l.getY1() - l.getY2();
		double ny = l.getX2() - l.getX1();
		//Second: normalize, modulus 1
		double modn = Math.sqrt(nx * nx + ny * ny);
		nx /= modn;
		ny /= modn;
		/*Third: of the two possible normal vectors we need the one that points to the
		 * outside; we choose the one that its final point is the nearest to the center
		 * of the other line.
		 */
		if ((p.getX()+nx-orgdCenterX)*(p.getX()+nx-orgdCenterX)+(p.getY()+ny-orgdCenterY)*(p.getY()+ny-orgdCenterY) <
			(p.getX()-nx-orgdCenterX)*(p.getX()-nx-orgdCenterX)+(p.getY()-ny-orgdCenterY)*(p.getY()-ny-orgdCenterY)) {
			nx = -nx;
			ny = -ny;
		}
		// This is the j in the parallel axis theorem
		double j = (-(1+Utils.getELASTICITY()) * (vab1x * nx + vab1y * ny)) / 
			(1/getMass() + 1/orgmass + Math.pow(rapx * ny - rapy * nx, 2) / inertia +
					Math.pow(rbpx * ny - rbpy * nx, 2) / orgI);
		// Final speed
		double maxSpeed = Utils.getMAX_VEL();
		double maxRot = Utils.getMAX_ROT();
		dx = Utils.between(dx + j*nx/getMass(), -maxSpeed, maxSpeed);
		dy = Utils.between(dy + j*ny/getMass(), -maxSpeed, maxSpeed);
		org.setDx(Utils.between(orgdx - j*nx/orgmass, -maxSpeed, maxSpeed));
		org.setDy(Utils.between(orgdy - j*ny/orgmass, -maxSpeed, maxSpeed));
		dtheta = Utils.between(dtheta + j * (rapx * ny - rapy * nx) / inertia, -maxRot, maxRot);
		org.setDtheta(Utils.between(orgdtheta - j * (rbpx * ny - rbpy * ny) /
				orgI, -maxRot, maxRot));
	}

	/**
	 * Finds if two organism are touching and if so applies the effects of the
	 * collision.
	 * 
	 * @param org  The organism to check for collisions.
	 * @return  true if the two organisms are touching, false otherwise.
	 */
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

	public void rubbingFrameEffects() {
		double rubbing = Utils.getRUBBING();
		dx *= rubbing;
		if (Math.abs(dx) < Utils.tol) dx=0;
		dy *= rubbing;
		if (Math.abs(dy) < Utils.tol) dy = 0;
		dtheta *= rubbing;
		if (Math.abs(dtheta) < Utils.tol) dtheta = 0;
	}
	
	
	
}
