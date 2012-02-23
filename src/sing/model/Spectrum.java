package sing.model;

import java.util.ArrayList;

import sing.Config;
import sing.Main;

public class Spectrum
{
    public double level;
    public double levelVelocity;
    public double levelSmooth;
    public double levelSpring;

    public ArrayList<Band> bands = new ArrayList<Band>();
    
    Main main;
    
    public Spectrum(Main main)
    {
	this.main = main;
	levelSmooth = 0;
	levelSpring = 0;
	levelVelocity = 0;
	
	double size = 0.05;
	for(double i = 0; i < Config.BANDS_NUM; i++)
	{
	    createBand(i / Config.BANDS_NUM + size, size);
	}
    }

    private void createBand(double position, double size)
    {
	Band band = new Band();
	band.position = position;
	band.size = size;
	bands.add(band);
    }

    public void setSpectrum(float[] spectrum)
    {
	for(Band band:bands)
	{
	    band.compute(spectrum);
	}
    }

    public void iterate()
    {
	
    }

    public void setLevel(double level)
    {
	levelVelocity += (level - levelSpring) * 0.03;
	levelVelocity *= 0.95;
	
	levelSpring += levelVelocity;
	
	this.level = level;
	levelSmooth += (level - levelSmooth) * 0.01;
    }

    public double highBands()
    {
	double result = 0; 
	for(int index = Config.BANDS_NUM / 2; index < Config.BANDS_NUM; index++)
	{
	    result += bands.get(index).valueSmooth;
	}
	return result;
    }

    public double lowBands()
    {
	double result = 0; 
	for(int index = 0; index < Config.BANDS_NUM / 2; index++)
	{
	    result += bands.get(index).valueSmooth;
	}
	return result;
    }
    
}
