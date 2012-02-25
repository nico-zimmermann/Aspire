package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.model.Particle;
import sing.model.ParticleS;
import sing.util.Angle;

public class Flash extends Program<Flash>
{
    private ParticleS particle;

    public Flash()
    {
	particle = new ParticleS();
    }

    @Override
    public void iterate()
    {
	double color = sin1(millis() / 10) * 10 + spectrum.bands.get(10).value * 1;
	int index = (int) (sin1(millis() / 100) * 10);
	if (spectrum.bands.get(10).value > 0.5)
	{
	    particle.color.set(color, color, color);
	    //setRGB(index, color, color, color);
	    show(particle);
	}
	
	particle.radius = 0.3;
	particle.positionS.azimuth += 0.2;
	particle.positionS.inclination = Angle.degToRad(sin1(millis() / 200) * 180);
    }
}
