package sing.program;

import javax.vecmath.Matrix3d;

import sing.model.ParticleE;
import sing.model.ParticleS;

public class VLines extends Program<VLines>
{
    private double move = 0;

    @Override
    public void iterate()
    {
	double step = 0.1;

	double c = 0.01;

	double r = 5;
	double radius = 0.5;
	double space = 0.2;
	double speed = (model.analyzer.levelSpring - 0.2) * 0.3;
	if (speed < 0.02)
	    speed = 0.02;
	double xlimit = (r + 2) * space * 2;

	move -= speed;
	if (move < -xlimit)
	    move = xlimit;

	if (move > xlimit)
	    move = -xlimit;

	Matrix3d m = new Matrix3d();
	m.setIdentity();
	m.rotY(millis() / 1500.0 + 2);
	m.rotZ(millis() / 2000.0);

	for (double line = -r; line <= r; line++)
	{
	    ParticleE particle = new ParticleE();
	    particle.color.set(
		    c * sin1(millis() / 5000 + line * 0.2 + Math.PI * 0.2) * 0.6,
		    c * sin1(millis() / 6000 + line * 0.3 + Math.PI) * 0.3 * 0.3,
		    c * sin1(millis() / 7000 + line * 0.1 + Math.PI * -0.4) * 0.4
		    );
	    for (double y = -1; y <= 1; y += step)
		for (double z = -1; z <= 1; z += step)
		{
		    particle.radius = radius;
		    particle.position.set(move + space * line, y, z);
		    m.transform(particle.position);
		    show(particle);
		}
	}
    }
}
