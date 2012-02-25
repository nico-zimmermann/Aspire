package sing.program;

import java.util.ArrayList;
import java.util.List;

import sing.Config;
import sing.model.ParticleS;
import sing.model.RGB;

public class BandParticles extends Program<BandParticles>
{
    public List<RGB> rgbs = new ArrayList<RGB>();

    public BandParticles()
    {
	for (int i = 0; i < Config.BANDS_NUM; i++)
	{
	    RGB rgb = new RGB();
	    rgb.r = Math.random();
	    rgb.g = Math.random();
	    rgb.b = Math.random();
	    rgbs.add(rgb);
	}
    }

    @Override
    public void iterate()
    {
	ParticleS particle = new ParticleS();
	for (int i = 0; i < Config.BANDS_NUM; i += 1)
	{
	    //particle.positionS.inclination = Math.PI / 2 + (i - 25) * 0.05;
	    particle.positionS.inclination = Math.PI / 2 ;
	    particle.positionS.azimuth =
		    main.map(i, 0, spectrum.bands.size(), 0, Math.PI * 2)
			    + spectrum.levelSpringIncrement * 0.00;
	    particle.color = getColor(i);
	    
	    double angle = i * 0.01;
	    particle.color.r = main.map(angle, -1, 0, 0, 1);
	    particle.color.g = 0;
	    particle.color.b = 0;
	    particle.color.scale(2);
	    particle.radius = spectrum.bands.get(i).valueSmooth * 1.0 + 0.3;

	    show(particle);
	}
    }

    private RGB getColor(int i)
    {
	return rgbs.get(i).cloneRGB();
    }

}
