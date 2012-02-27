package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.model.Analyzer;
import sing.model.Particle;
import sing.model.ParticleS;
import sing.util.Angle;

public class TwoOppositeParticles extends Program<TwoOppositeParticles>
{
    public List<ParticleS> particles = new ArrayList<ParticleS>();

    public TwoOppositeParticles()
    {
	ParticleS particle;

	particle = new ParticleS();
	particle.color.useHLS();
	particles.add(particle);

	particle = new ParticleS();
	particle.color.useHLS();
	particles.add(particle);
    }

    @Override
    public void iterate()
    {
	
	double scale = 1;
	switch (channelIndex)
	{
	    case 3:
	    case 4:
	    case 12:
	    case 18:
	    case 22:
	    case 25:
		scale = 0;
	}
	
	scale *= model.analyzer.levelSpring;
	if (scale < 0)
	    scale = 0;
	if (scale > 1)
	    scale = 1;
	
	ParticleS particle1 = particles.get(0);
	particle1.color.h = channelIndex / 10.0;
	particle1.color.l = 0.5 * scale;
	particle1.color.s = 1;
	particle1.radius = 0.6;
	
	particle1.positionS.azimuth = -model.analyzer.levelSpringIncrement * 0.3 - model.frame * 0.01;
	particle1.positionS.inclination = Angle.degToRad(40 + channelIndex * 0.5) ;


	ParticleS particle2 = particles.get(1);
	particle2.color.h = particle1.color.h + 0.5;
	particle2.color.l = 0.5 * scale;
	particle2.color.s = 1;
	particle2.radius = particle1.radius;

	particle2.positionS.azimuth = particle1.positionS.azimuth - Angle.degToRad(180);
	particle2.positionS.inclination = Angle.degToRad(180) - particle1.positionS.inclination;

	//particle1.color.scale(0.5 * scale);
	show(particle1);
	//particle2.color.scale(0.5 * scale);
	show(particle2);
    }
}
