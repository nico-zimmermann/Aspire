package sing.program;

import sing.Config;
import sing.model.ParticleS;

public class SingleSmoothParticle extends Program<SingleSmoothParticle>
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
	particle.radius = 0.5;

	switch (channelIndex)
	{
	    case 3:
	    case 4:
		particle.color.r = sin1(model.frame * 0.05 + spectrum.levelSpring * 0.2);
		particle.color.g = sin1(model.frame * 0.05 + Math.PI / 2 + spectrum.levelSpring * 0.2);
		particle.color.b = 0;
		break;

	    case 5:
	    case 6:
		particle.color.r = 0;
		particle.color.g = 0;
		particle.color.b = 1;
		break;
		
	    case 8:
	    case 9:
		particle.color.r = 0;
		particle.color.g = 1;
		particle.color.b = 0;
		break;

	    case 10:
	    case 11:
		particle.color.r = 1;
		particle.color.g = 0;
		particle.color.b = 0;
		break;
		
	    case 13:
		particle.color.r = 1;
		particle.color.g = 0;
		particle.color.b = 0;
		break;
		
	    case 12:
	    case 18:
	    case 22:
	    case 25:
		particle.color.r = 0;
		particle.color.g = 0;
		particle.color.b = 0;
		break;

	    default:
		particle.color.r = sin1(channelIndex * Math.PI / 5);
		particle.color.g = sin1(channelIndex * Math.PI / 5 + Math.PI / 2);
		particle.color.b = 0;
	}

	particle.positionS.azimuth = channelIndex * Math.PI / (Config.BANDS_NUM / 2) + (channelIndex * 0.00001 * millis());
	particle.positionS.inclination = Math.PI / 2;

	show(particle);
    }
}
