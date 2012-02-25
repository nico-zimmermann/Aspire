package sing.program;

import javax.vecmath.Vector3d;

import sing.Config;
import sing.Main;
import sing.model.Analyzer;
import sing.model.LED;
import sing.model.Programms;
import sing.model.Particle;
import sing.model.SPoint;

public abstract class Program<T>
{
    public Programms model;
    public Main main;

    public boolean enabled = false;
    public double alpha = 0;
    public Analyzer spectrum;
    public boolean disabled;
    public int channelIndex;

    public abstract void iterate();

    public T disable()
    {
	disabled = true;
	return (T) this;
    }

    public void show(Particle particle)
    {
	show(particle, Interpolation.LINEAR);
    }

    public void show(Particle particle, Interpolation interpolation)
    {
	particle.precalculate();
	if (disabled)
	    return;
	for (int index = 0; index < Config.LEDS; index++)
	{
	    LED led = getLED(index);

	    double energy = 0;
	    
	    double dx = led.positionV.x - particle.position.x;
	    double dy = led.positionV.y - particle.position.y;
	    double dz = led.positionV.z - particle.position.z;
	    double dist = Math.sqrt(dx * dx + dy * dy + dz * dz) / 2.0;

	    switch (interpolation)
	    {
		case LINEAR:
		    energy = particle.radius - dist;
		    break;
		case POW:
		    double near = 1 - dist;
		    energy = Math.pow(near, (1 - particle.radius) * 8);
		    break;
	    }

	    if (energy < 0)
		energy = 0;

	    setRGB(index, particle.color.r * energy, particle.color.g * energy, particle.color.b * energy);
	}
	
	Particle clone = particle.creatViewClone();
	clone.color.alpha = spectrum.bands.get(channelIndex).energySmooth;
	model.particles1.add(clone);
    }

    public LED getLED(int index)
    {
	return model.leds[index];
    }

    public void setRGB(int index, double r, double g, double b)
    {
	double energy = spectrum.bands.get(channelIndex).energySmooth;
	
	model.setRGB(index,
		//Math.pow(r * alpha * model.globalAlpha, model.globalPow),
		//Math.pow(g * alpha * model.globalAlpha, model.globalPow),
		//Math.pow(b * alpha * model.globalAlpha, model.globalPow)
		r * energy, g * energy, b * energy
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

    public void init()
    {
    }
}
