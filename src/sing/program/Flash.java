package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.model.Particle;
import sing.model.ParticleS;
import sing.util.Angle;

public class Flash extends Program
{
    private ParticleS particle;

    public Flash()
    {
	particle = new ParticleS();
    }

    @Override
    public void iterate()
    {
	double color = sin1(millis() / 10) * 10 + spectrum.highBands() * 1;
	int index = (int) (sin1(millis() / 100) * 10);
	if (spectrum.highBands() > 0.5)
	{
	    particle.color.set(color, color, color);
	    //setRGB(index, color, color, color);
	    show(particle);
	}
	
	particle.radius = 0.3;
	particle.position.azimuth += 0.2;
	particle.position.inclination = Angle.degToRad(sin1(millis() / 200) * 180);
    }
}
