package sing.program;

import sing.Config;
import sing.model.ParticleS;

public class SingleSmoothParticle extends Program<SingleSmoothParticle>
{
    private ParticleS particle;

    public SingleSmoothParticle()
    {
	particle = new ParticleS();
	particle.color.useHLS();
    }

    @Override
    public void iterate()
    {
	particle.rnd1 = 1;
	particle.radius = 0.7;

	particle.color.l = 0.5 * 1.0;
	particle.color.s = 1;
	particle.color.h = channelIndex / 8.0 * ((channelIndex % 2 == 1) ? 0.5 : 1.0);
	switch((channelIndex + 1) % 3)
	{
	    case 0:
		particle.color.h += model.frame * 0.000;
		break;
	    case 1:
		particle.color.h += spectrum.levelSpringIncrement * 0.005;
		break;
	    case 2:
		particle.color.l = 0;
		break;
	}
	

	particle.positionS.azimuth = channelIndex * Math.PI / (Config.BANDS_NUM / 2) + ((channelIndex + 6) * 0.000009 * millis());
	particle.positionS.inclination = Math.PI / 2;

	show(particle);
    }
}
