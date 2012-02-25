package sing.model;

import javax.vecmath.Vector3d;

public class ParticleS extends Particle
{
    public SPoint positionS = new SPoint();

    @Override
    public Particle creatViewClone()
    {
	ParticleS clone = new ParticleS();
	clone.setFrom(this);
	clone.positionS.azimuth = positionS.azimuth;
	clone.positionS.inclination = positionS.inclination;
	return clone;
    }

    @Override
    public void precalculate()
    {
	Vector3d pos = positionS.getPosition();
	position.x = pos.x;
	position.y = pos.y;
	position.z = pos.z;
    }

}
