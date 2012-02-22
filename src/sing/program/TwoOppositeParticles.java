package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.Config;
import sing.util.Angle;
import sing.util.Particle;
import sing.util.SPoint;

public class TwoOppositeParticles extends Program
{
    public List<Particle> particles = new ArrayList<Particle>();
    public boolean enabled;

    public TwoOppositeParticles()
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
	particle1.color.r = 2.0;
	particle1.color.g = -1;
	particle1.color.b = -1;
	particle1.radius = 0.6;

	particle1.position.azimuth -= (Math.sin(millis() / 4000.0) * 0.5 + 0.5) * 0.1 + (Math.sin(millis() / 3000.0) * 0.5 + 0.5) * 0.05;
	particle1.position.inclination = Angle.degToRad((Math.sin(-millis() / 2000.0 + particle1.rnd1 * 3) * 0.5 + 0.5) * (180.0 - 40.0) + 20.0);

	show(particle1);

	Particle particle2 = particles.get(1);
	particle2.color.r = -1;
	particle2.color.g = -1;
	particle2.color.b = 2.0;
	particle2.radius = 0.6;

	particle2.position.azimuth = particle1.position.azimuth - Angle.degToRad(180);
	particle2.position.inclination = Angle.degToRad(180) - particle1.position.inclination;

	show(particle2);
    }
}
