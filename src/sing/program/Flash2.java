package sing.program;

import java.util.ArrayList;

import sing.Config;
import sing.model.ParticleS;
import sing.util.Angle;

public class Flash2 extends Program<Flash2>
{
    @Override
    public void iterate()
    {
	ParticleS particle = new ParticleS();
	particle.radius = model.analyzer.bands.get(channelIndex).position * 0.4 + 0.2;
	particle.positionS.azimuth =  channelIndex * Math.PI / (Config.BANDS_NUM / 2);
	particle.positionS.inclination = Math.PI / 2 + sin((channelIndex - Config.BANDS_NUM / 2) * 0.03) * 1;
	double frequency = model.analyzer.bands.get(channelIndex).position * 4 + 0.1;
	double energy = sin1(model.frame * frequency) * 0.5 + 0.3;

	switch((channelIndex + 1) % 3)
	{
	    case 0:
		break;
	    case 1:
		energy = 0;
		break;
	    case 2:
		break;
	}
	double r = 1;
	double g = 1;
	double b = 1;
	particle.color.set(r * energy, g * energy, b * energy);
	show(particle);
    }
}
