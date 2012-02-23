package sing.model;

import javax.vecmath.Vector3d;

import sing.model.Particle;
import sing.model.SPoint;

public class ParticleE extends Particle
{
    public Vector3d position = new Vector3d();

    @Override
    public double getDistance(SPoint position)
    {
	return position.getDistance(this.position);
    }

}
