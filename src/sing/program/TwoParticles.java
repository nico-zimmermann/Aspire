package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.Config;
import sing.util.Angle;
import sing.util.Particle;
import sing.util.SPoint;

public class TwoParticles extends Program
{
    public List<Particle> particles = new ArrayList<Particle>();
    public boolean enabled;

    public TwoParticles()
    {
	Particle particle;

	particle = new Particle();
	particles.add(particle);

	particle = new Particle();
	particles.add(particle);
    }

    @Override
    public void iterate()
    {
	if (!enabled)
	    return;

	Particle particle1 = particles.get(0);
	particle1.radius = 0.8;
	particle1.color.r = 1;
	particle1.color.g = 0.15;
	particle1.color.b = 0;

	Particle particle2 = particles.get(1);
	particle2.radius = 0.8;
	particle2.color.r = 0;
	particle2.color.g = 0.5;
	particle2.color.b = 0.15;

	for (Particle particle : particles)
	{
	    particle.position.azimuth += (Math.sin(millis() / (4000.0 - particle.rnd1 * 1000) + particle.rnd1) * 0.5 + 0.5) * 0.1 + (Math.sin(millis() / 3000.0 + 1 + particle.rnd1 * 3) * 0.5 + 0.5) * 0.05;
	    particle.position.inclination = Angle.degToRad((Math.sin(millis() / (2000.0 - particle.rnd1 * 500) + particle.rnd1 * 3) * 0.5 + 0.5) * (180.0 - 40.0) + 20.0);

	    show(particle);
	}
    }
}
