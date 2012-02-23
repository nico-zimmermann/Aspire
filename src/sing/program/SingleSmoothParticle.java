package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.model.Particle;
import sing.model.ParticleS;
import sing.util.Angle;

public class SingleSmoothParticle extends Program
{
    private ParticleS particle;

    public SingleSmoothParticle()
    {
	particle = new ParticleS();
    }

    @Override
    public void iterate()
    {
	particle.rnd1 = 1;
	particle.radius = 0.8;
	particle.color.r = sin1(millis() / 1000.0);
	particle.color.g = sin1(millis() / 1300.0 + Math.PI) * 0.1;
	particle.color.b = sin1(millis() / 1000.0 + Math.PI * 1.4) * 0.6;

	particle.position.azimuth += 0.05 + (Math.sin(millis() / (3000.0 - particle.rnd1 * 1000) + particle.rnd1) * 0.5 + 0.5) * 0.02;
	particle.position.inclination = Angle.degToRad((Math.sin(millis() / (2000.0 - particle.rnd1 * 500) + particle.rnd1 * 3) * 0.5 + 0.5) * (180.0 - 40.0) + 20.0);

	show(particle);
    }
}
