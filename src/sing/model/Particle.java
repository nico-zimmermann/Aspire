package sing.model;


public abstract class Particle
{
    public RGB color = new RGB();
    public double radius = 0.8;

    public double rnd1 = Math.random();
    public double rnd2 = Math.random();
    public double rnd3 = Math.random();
    
    abstract public double getDistance(SPoint position);
}
