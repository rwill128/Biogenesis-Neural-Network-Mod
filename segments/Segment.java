package segments;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import agents.AliveAgent;

import biogenesis.Utils;

public abstract class Segment extends Line2D.Double {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Color deadColor = Utils.ColorBROWN;
	/**
	 * X coordinates of the starting point of each organism's segments.
	 */
	private Point startingPoint = new Point();
	/**
	 * X coordinates of the ending point of each organism's segments.
	 */
	private Point endingPoint = new Point();
	/**
	 * Precalculated distance from the origin to the starting point of each segment.
	 * Used to calculate rotations.
	 */
	private double _m1;
	/**
	 * Precalculated distance from the origin to the ending point of each segment.
	 * Used to calculate rotations.
	 */
	private double _m2;
	/**
	 * Precalculated modulus of each segment.
	 */
	private double _m;
	/**
	 * Effective segment colors, taken from the genetic code if alive or brown if dead.
	 */
	protected final Color _segColor;
	protected boolean alive = true;
	
	protected double centerX, centerY;
	
	private static Point2D.Double intersectionPoint = new Point2D.Double();
	
	private final AliveAgent thisAgent;
	
	public AliveAgent getThisAgent() {
		return thisAgent;
	}
	
	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	public void setCenter(double centerX, double centerY) {
		x1 = x1 - this.centerX + centerX;
		y1 = y1 - this.centerY + centerY;
		x2 = x2 - this.centerX + centerX;
		y2 = y2 - this.centerY + centerY;
		this.centerX = centerX;
		this.centerY = centerY;
	}
	
	public Segment(AliveAgent thisAgent, Color color) {
		this.thisAgent = thisAgent;
		_segColor = color;
	}
	public void setStartingPoint(int x, int y) {
		startingPoint.x = x;
		startingPoint.y = y;
	}
	public void setStartingPoint(Point p) {
		startingPoint.x = p.x;
		startingPoint.y = p.y;
	}
	public void setEndingPoint(int x, int y) {
		endingPoint.x = x;
		endingPoint.y = y;
	}
	public void setEndingPoint(Point p) {
		endingPoint.x = p.x;
		endingPoint.y = p.y;
	}
	public void translate(int x, int y) {
		startingPoint.translate(x, y);
		endingPoint.translate(x, y);
	}
	public Point getStartingPoint() {
		return startingPoint;
	}
	public Point getEndingPoint() {
		return endingPoint;
	}
	public Color getColor() {
		return _segColor;
	}
	public void draw(Graphics g, boolean showColor) {
		if (showColor)
			g.setColor(alive?_segColor:deadColor);
		g.drawLine((int)Math.round(x1), (int)Math.round(y1), (int)Math.round(x2), (int)Math.round(y2));
	}
	
	public void draw(Graphics g, double centerX, double centerY, boolean showColor) {
		if (showColor)
			g.setColor(alive?_segColor:deadColor);
		g.drawLine((int)Math.round(x1 - this.centerX + centerX),
				(int)Math.round(y1 - this.centerY + centerY),
				(int)Math.round(x2 - this.centerX + centerX),
				(int)Math.round(y2 - this.centerY + centerY));
	}
	public double getMass() {
		return _m;
	}

	public void recalculate(double inclination, double centerX, double centerY) {
		recalculate(inclination, centerX, centerY, 
				Math.atan2(startingPoint.y, startingPoint.x));
	}
	public double recalculate(double inclination, double centerX, double centerY, 
			double atanStart) {
		double theta;
		double endPointAngle;
		
		theta = inclination + atanStart;
		x1 = _m1*Math.cos(theta);
		y1 = _m1*Math.sin(theta);
		endPointAngle = Math.atan2(endingPoint.y, endingPoint.x);
		theta = inclination + endPointAngle;
		x2 = _m2*Math.cos(theta);
		y2 = _m2*Math.sin(theta);
		
		x1 += centerX;
		x2 += centerX;
		y1 += centerY;
		y2 += centerY;
		
		this.centerX = centerX;
		this.centerY = centerY;
		
		return endPointAngle;
	}
	public void updateMass() {
		_m1 = startingPoint.distance(0, 0);
		_m2 = endingPoint.distance(0, 0);
		_m = startingPoint.distance(endingPoint);
	}
	public double getInertia() {
		double cx, cy;
		
		// calculate inertia moment
		// the mass center of a segment is its middle point
		cx = (startingPoint.x + endingPoint.x) / 2d;
		cy = (startingPoint.y + endingPoint.y) / 2d;
		
		return Math.pow(getMass(),3)/12d +
			getMass() * (cx*cx + cy*cy);// mass * length^2 (center is at 0,0)
	}
	
	/**
	 * Return the intersection of this line with l.
	 *  
	 * @param l  The line to which the intersection is to be calculated
	 * @return  A Point2D indicating the point of intersection or
	 * the middle point between the two segments if they are parallel.
	 * For the sake of speed, the returned point is static, so it should not be
	 * saved.
	 */
	public Point2D getIntersection(Segment l)
	{
		double d;
		double t;
		
		d = (l.x1-l.x2)*(y2-y1)+(x2-x1)*(l.y2-l.y1);
		if (d < Utils.tol) {
			// Parallel or the same straight line
			intersectionPoint.x = (x1+x2+l.x1+l.x2)/4f;
			intersectionPoint.y = (y1+y2+l.y1+l.y2)/4f;
		} else {
			t = ((l.x1-x1)*(y2-y1)+(x2-x1)*(y1-l.y1))/d;
			intersectionPoint.x = l.x1 + t*(l.x2-l.x1);
			intersectionPoint.y = l.y1 + t*(l.y2-l.y1);
		}
		return intersectionPoint;
	}
	
	public void touchEffects(Segment otherSegment) {
		touchEffectsOneWay(otherSegment);
		otherSegment.touchEffectsOneWay(this);
	}
	
	protected void touchEffectsOneWay(Segment otherSegment) {
		// Do nothing.
	}
	
	protected boolean shield(Segment otherSegment) {
		if (otherSegment.getColor() != getColor())
			thisAgent.setColor(Color.YELLOW);
		return false;
	}
	
	public void frameEffects() {
		// Do nothing
	}
	/*
	public void contact(AliveAgent otherAgent) {
		// Consider only segments with modulus greater than 1
		if (getMass() >= 1) {
			// First check if the line intersects the bounding box of the other organism
			if (otherAgent.intersectsLine(this)) {
							// Do the same for the other organism's segments.
							for (Segment sorg : otherAgent.getSegments()) {
								if (sorg.getMass() >= 1) {
									if (intersectsLine(sorg) && intersectsLine(sorg)) {
										// If we found two intersecting segments, apply effects
										getThisAgent().touchEffects(otherAgent, this, sorg, true);
										// Intersection point
										Point2D intersec= getIntersection(sorg);
										/* touchMove needs to know which is the line that collides from the middle (not
										 * from a vertex). Try to guess it by finding the vertex nearest to the
										 * intersection point.
										 *
										double dl1, dl2, dbl1, dbl2;
										dl1 = intersec.distanceSq(getP1());
										dl2 = intersec.distanceSq(getP2());
										dbl1 = intersec.distanceSq(sorg.getP1());
										dbl2 = intersec.distanceSq(sorg.getP2());
										// Use this to send the best choice to touchMove
										if (Math.min(dl1, dl2) < Math.min(dbl1, dbl2))
											touchMove(org, intersec, sorg, false);
										else
											touchMove(org, intersec, s, true);
										// Find only one collision to speed up.
										return true;
									}
								}
							}
						}
					}
	}*/
}
