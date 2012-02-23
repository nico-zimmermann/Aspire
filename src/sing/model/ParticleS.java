package sing.model;

public class ParticleS extends Particle
{
    public SPoint position = new SPoint();

    @Override
    public double getDistance(SPoint position)
    {
	return this.position.getDistance(position);
    }

}
