package sing;

import java.util.ArrayList;
import java.util.List;

import sing.program.Calibrate;
import sing.program.Program;
import sing.program.TwoOppositeParticles;
import sing.program.TwoParticles;
import sing.util.Angle;
import sing.util.RGB;
import sing.util.SPoint;

public class Model
{
    public SPoint[] positions = new SPoint[Config.LEDS];
    public RGB[] rgb = new RGB[Config.LEDS];
    public List<Program> programs = new ArrayList<Program>();
    public int millis;
    
    public TwoParticles twoParticles;
    public Calibrate calibrate;
    public Main main;
    public TwoOppositeParticles twoOppositeParticles;

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
	addProgram(twoOppositeParticles = new TwoOppositeParticles());
	addProgram(twoParticles = new TwoParticles());
	addProgram(calibrate = new Calibrate());
    }

    public void calib(int index, double azimuth, double inclination)
    {
	positions[index].setAzimuth(Angle.degToRad(azimuth));
	positions[index].setInclination(Angle.degToRad(inclination));
    }

    public void iterate()
    {
	clearRGB();
	
	for(Program program : programs)
	{
	    program.iterate();
	}
    }

    public void addProgram(Program program)
    {
	program.model = this;
	program.main = main;
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
	rgb[index].r += r;
	rgb[index].g += g;
	rgb[index].b += b;
    }

    public double millis()
    {
	return millis;
    }
}
