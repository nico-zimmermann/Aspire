package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.Config;
import sing.model.ParticleS;
import sing.model.Color;

public class BandParticles extends Program<BandParticles>
{
    @Override
    public void iterate()
    {
	ParticleS particle = new ParticleS();
	particle.color.useHLS();
	// particle.positionS.inclination = Math.PI / 2 + (i - 25) * 0.05;
	particle.positionS.inclination = Math.PI / 2;
	particle.positionS.azimuth =
		main.map(channelIndex, 0, spectrum.bands.size(), 0, Math.PI * 2)
			+ spectrum.levelSpringIncrement * 0.001;

	particle.color.h = channelIndex / 10.0;
	particle.color.l = 0.6;
	particle.color.s = 1;
	particle.radius = spectrum.bands.get(channelIndex).valueSmooth * 0.5 + 0.1;

	show(particle);
    }
}
