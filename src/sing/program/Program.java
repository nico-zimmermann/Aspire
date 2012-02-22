package sing.program;

import sing.Config;
import sing.Main;
import sing.Model;
import sing.util.Interpolation;
import sing.util.Particle;
import sing.util.SPoint;

public abstract class Program
{
    public Model model;
    public Main main;

    public abstract void iterate();

    public void show(Particle particle)
    {
	show(particle, Interpolation.LINEAR);
    }

    public void show(Particle particle, Interpolation interpolation)
    {
	for (int index = 0; index < Config.LEDS; index++)
	{
	    SPoint position = getLED(index);

	    double energy = 0;

	    switch (interpolation)
	    {
		case LINEAR:
		    energy = particle.radius - particle.position.getDistance(position);
		    break;
		case POW:
		    double near = 1 - particle.position.getDistance(position);
		    energy = Math.pow(near, (1 - particle.radius) * 8);
		    break;
	    }

	    if (energy < 0)
		energy = 0;

	    setRGB(index, particle.color.r * energy, particle.color.g * energy, particle.color.b * energy);
	}
    }

    public SPoint getLED(int index)
    {
        return model.positions[index];
    }

    public void setRGB(int index, double r, double g, double b)
    {
	model.setRGB(index, r, g, b);
    }

    public int millis()
    {
        return model.millis;
    }
}
