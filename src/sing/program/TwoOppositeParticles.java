package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.model.Particle;
import sing.model.ParticleS;
import sing.util.Angle;

public class TwoOppositeParticles extends Program
{
    public List<ParticleS> particles = new ArrayList<ParticleS>();

    public TwoOppositeParticles()
    {
	ParticleS particle;

	particle = new ParticleS();
	particles.add(particle);

	particle = new ParticleS();
	particles.add(particle);
    }

    @Override
    public void iterate()
    {
	ParticleS particle1 = particles.get(0);
	particle1.color.r = spectrum.highBands();
	particle1.color.g = spectrum.lowBands() * 0.5;
	particle1.color.b = 0;
	particle1.radius = 0.7;

	particle1.position.azimuth -= spectrum.bands.get(1).value * 0.4;
	particle1.position.inclination = Angle.degToRad((Math.sin(-millis() / 2000.0 + particle1.rnd1 * 3) * 0.5 + 0.5) * (180.0 - 40.0) + 20.0);

	show(particle1);

	ParticleS particle2 = particles.get(1);
	particle2.color.r = spectrum.lowBands() * 0.5;
	particle2.color.g = 0;
	particle2.color.b = spectrum.highBands();
	particle2.radius = 0.7;

	particle2.position.azimuth = particle1.position.azimuth - Angle.degToRad(180);
	particle2.position.inclination = Angle.degToRad(180) - particle1.position.inclination;

	show(particle2);
    }
}
