package sing.model;

import javax.vecmath.Vector3d;

import quicktime.streaming.NewPresentationParams;

import sing.model.Particle;
import sing.model.SPoint;

public class ParticleE extends Particle
{
    @Override
    public Particle creatViewClone()
    {
	ParticleE clone = new ParticleE();
	clone.setFrom(this);
	clone.position.x = position.x;
	clone.position.y = position.y;
	clone.position.z = position.z;
	return clone;
    }

    @Override
    public void precalculate()
    {
    }
}
