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
	particles.add(particle);

	particle = new ParticleS();
	particles.add(particle);
    }

    @Override
    public void iterate()
    {
	
	double scale = 0;
	switch (channelIndex)
	{
	    case 3:
	    case 4:
	    case 12:
	    case 18:
	    case 22:
	    case 25:
		scale = 0;
		break;
		
	    default:
		scale = 1;
	}
	
	scale *= model.analyzer.levelSpring;
	
	ParticleS particle1 = particles.get(0);
	particle1.color.r = sin1(channelIndex * 0.1);
	particle1.color.g = sin1(channelIndex * 0.1 + Math.PI / 2);
	particle1.color.b = 0;
	particle1.radius = 0.6;
	
	particle1.positionS.azimuth = -model.analyzer.levelSpringIncrement * 0.5 - model.frame * 0.03 * 1;
	particle1.positionS.inclination = Angle.degToRad(40 + channelIndex * 0.5) ;


	ParticleS particle2 = particles.get(1);
	particle2.color.r = spectrum.lowBands() * 0.5;
	particle2.color.g = 0;
	particle2.color.b = spectrum.highBands();
	particle2.color.r = 1 - particle1.color.r;
	particle2.color.g = 1 - particle1.color.g;
	particle2.color.b = 1 - particle1.color.b;
	particle2.radius = particle1.radius;

	particle2.positionS.azimuth = particle1.positionS.azimuth - Angle.degToRad(180);
	particle2.positionS.inclination = Angle.degToRad(180) - particle1.positionS.inclination;

	particle1.color.scale(0.5 * scale);
	show(particle1);
	particle2.color.scale(0.5 * scale);
	show(particle2);
    }
}
