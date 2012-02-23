package sing.program;

import sing.Config;
import sing.Main;
import sing.model.Model;
import sing.model.Particle;
import sing.model.SPoint;
import sing.model.Spectrum;

public abstract class Program
{
    public Model model;
    public Main main;

    public boolean enabled = false;
    public double alpha = 0;
    public Spectrum spectrum;

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
		    energy = particle.radius - particle.getDistance(position);
		    break;
		case POW:
		    double near = 1 - particle.getDistance(position);
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
	model.setRGB(index,
		Math.pow(r * alpha * model.globalAlpha, model.globalPow),
		Math.pow(g * alpha * model.globalAlpha, model.globalPow),
		Math.pow(b * alpha * model.globalAlpha, model.globalPow)
		);
    }

    public double sin(double x)
    {
	return Math.sin(x);
    }

    public double sin1(double x)
    {
	return Math.sin(x) * 0.5 + 0.5;
    }

    public int millis()
    {
	return model.millis;
    }
}
