package sing.model;

import javax.vecmath.Vector3d;


public abstract class Particle
{
    public RGB color = new RGB();
    public double radius = 0.8;
    public Vector3d position = new Vector3d();

    public double rnd1 = Math.random();
    public double rnd2 = Math.random();
    public double rnd3 = Math.random();
    
    abstract public Particle creatViewClone();
    
    abstract public void precalculate();
    
    public void setFrom(Particle particle)
    {
	radius = particle.radius;
	color.r = particle.color.r;
	color.g = particle.color.g;
	color.b = particle.color.b;
	position.x = particle.position.x;
	position.y = particle.position.y;
	position.z = particle.position.z;
    }
}
