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
	particle.color.r = sin1(channelIndex * Math.PI / 5);
	particle.color.g = sin1(channelIndex * Math.PI / 5 + Math.PI / 2);
	particle.color.b = 0;

	particle.positionS.azimuth = channelIndex * Math.PI / (Config.BANDS_NUM / 2) + (channelIndex * 0.00001 * millis());
	particle.positionS.inclination = Math.PI / 2;
	
	show(particle);
    }
}
