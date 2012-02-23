package sing.model;

import java.util.ArrayList;
import java.util.List;

import sing.Config;
import sing.Main;
import sing.program.Calibrate;
import sing.program.Flash;
import sing.program.Program;
import sing.program.TwoOppositeParticles;
import sing.program.SingleSmoothParticle;
import sing.program.VLines;
import sing.util.Angle;

public class Model
{
    public SPoint[] positions = new SPoint[Config.LEDS];
    public RGB[] rgb = new RGB[Config.LEDS];
    public List<Program> programs = new ArrayList<Program>();
    public int millis;
    public Main main;

    public double globalAlpha;
    public double globalPow;

    public SingleSmoothParticle singleSmoothParticle;
    public Calibrate calibrate;
    public TwoOppositeParticles twoOppositeParticles;
    public VLines vlines;
    public Flash flash;
    public Spectrum spectrum;

    public Model()
    {
	for (int i = 0; i < Config.LEDS; i++)
	{
	    positions[i] = new SPoint();
	    rgb[i] = new RGB();
	}
    }

    public void initPrograms()
    {
	addProgram(calibrate = new Calibrate());
	addProgram(twoOppositeParticles = new TwoOppositeParticles());
	addProgram(singleSmoothParticle = new SingleSmoothParticle());
	addProgram(vlines = new VLines());
	addProgram(flash = new Flash());
    }

    public void calibrateLED(int index, double azimuth, double inclination)
    {
	positions[index].setAzimuth(Angle.degToRad(azimuth));
	positions[index].setInclination(Angle.degToRad(inclination));
    }

    public void iterate()
    {
	clearRGB();

	for (Program program : programs)
	{
	    if (program.enabled && program.alpha < 1)
		program.alpha += 0.03;

	    if (!program.enabled && program.alpha > 0)
		program.alpha *= 0.88;

	    if (program.alpha < 0.01)
		program.alpha = 0;
	    if (program.alpha > 1)
		program.alpha = 1;

	    if (program.alpha != 0)
		program.iterate();
	}
    }

    public void addProgram(Program program)
    {
	program.model = this;
	program.main = main;
	program.spectrum = spectrum;
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
