package sing.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import sing.Config;
import sing.Main;
import sing.program.BandParticles;
import sing.program.Calibrate;
import sing.program.Flash;
import sing.program.Flash2;
import sing.program.Program;
import sing.program.SingleSmoothParticle;
import sing.program.TwoOppositeParticles;
import sing.program.VLines;
import sing.util.Angle;

public class Programms
{
    public LED[] leds = new LED[Config.LEDS];
    public RGB[] rgb = new RGB[Config.LEDS];
    public List<Program> programs = new ArrayList<Program>();
    public Collection<Particle> particles1 = Collections.synchronizedCollection(new ArrayList<Particle>());
    public Collection<Particle> particles2 = Collections.synchronizedCollection(new ArrayList<Particle>());
    public int millis;
    public Main main;

    public double globalAlpha;
    public double globalPow;

    public SingleSmoothParticle singleSmoothParticle;
    public Calibrate calibrate;
    public TwoOppositeParticles twoOppositeParticles;
    public VLines vlines;
    public Flash flash;
    public Analyzer analyzer;
    public BandParticles bands;
    public Flash2 flash2;
    public boolean showBands;
    public boolean inDraw;
    public int frame;

    public Programms()
    {
	for (int i = 0; i < Config.LEDS; i++)
	{
	    leds[i] = new LED();
	    rgb[i] = new RGB();
	}
    }

    public void initPrograms()
    {
	addProgram( (calibrate = new Calibrate()).disable() );
	addProgram( (twoOppositeParticles = new TwoOppositeParticles()) );
	addProgram( (singleSmoothParticle = new SingleSmoothParticle()) );
	addProgram( (vlines = new VLines()).disable() );
	addProgram( (flash = new Flash()).disable() );
	addProgram( (flash2 = new Flash2()) );
	addProgram( (bands = new BandParticles()).disable() );

	expandTo(programs, 50);
    }

    private void expandTo(List<Program> programs2, int newSize)
    {
	ArrayList<Program> newPrograms = new ArrayList<Program>();

	int i = 0;
	while(i < newSize)
	{
	    for (int k = 0; k < 5; k++)
	    {
		Program randomProgram = programs2.get((i % programs2.size()));
		newPrograms.add(randomProgram);
		i++;
	    }
	}
	
	programs.clear();
	programs.addAll(newPrograms);
    }

    public void calibrateLED(int index, double azimuth, double inclination)
    {
	leds[index].positionS.azimuth = Angle.degToRad(azimuth);
	leds[index].positionS.inclination = Angle.degToRad(inclination);
	leds[index].precalculate();
    }

    public void iterate()
    {
	// globalAlpha = 0.2 + analyzer.levelSpring * 0.7;

	clearRGB();

	frame++;
	for (int i = 0; i < programs.size(); i++)
	{
	    Program program = programs.get(i);
	    program.channelIndex = i;
	    program.alpha = 1;
	    if (program.enabled)
		program.iterate();
	}

	particles2.clear();
	particles2.addAll(particles1);
	particles1.clear();
    }

    public void addProgram(Program program)
    {
	program.model = this;
	program.main = main;
	program.spectrum = analyzer;
	program.init();
	
	
	if (!program.disabled)
	    programs.add(program);
    }

    public void clearRGB()
    {
	for (int i = 0; i < Config.LEDS; i++)
	{
	    rgb[i].r = 0;
	    rgb[i].g = 0;
	    rgb[i].b = 0;
	}
    }

    public void setRGB(int index, double r, double g, double b)
    {
	if (index < 0 || index >= rgb.length)
	    return;

	rgb[index].r += r;
	rgb[index].g += g;
	rgb[index].b += b;
    }

    public double millis()
    {
	return millis;
    }
}