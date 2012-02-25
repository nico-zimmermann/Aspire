package sing.model;

import java.util.ArrayList;

import krister.Ess.FFT;
import sing.Config;
import sing.Main;

public class Analyzer
{
    public int[] waveform = new int[Config.WAVEFORM_SIZE];
    public double[] spectrum = new double[Config.SPECTRUM_SIZE];

    public double level;
    public double levelVelocity;
    public double levelSmooth;
    public double levelSpring;
    public double levelSpringIncrement;

    public double offset = 0;
    public double mul = 1;

    public ArrayList<Band> bands = new ArrayList<Band>();

    Main main;
    private FFT fft;
    private float newLevel;

    public Analyzer(Main main)
    {
	this.main = main;

	fft = new FFT(Config.WAVEFORM_SIZE);
	fft.averages(32);

	levelSmooth = 0;
	levelSpring = 0;
	levelSpringIncrement = 0;
	levelVelocity = 0;

	for (double i = 0; i < Config.BANDS_NUM; i++)
	{
	    Band band = new Band();
	    band.analyzer = this;
	    band.index = i;
	    bands.add(band);
	}
    }

    public void setInput(int[] newWaveform)
    {
	waveform = newWaveform;
	float[] srcArray = new float[Config.WAVEFORM_SIZE];
	for (int i = 0; i < waveform.length; i++)
	{
	    int value = waveform[i];
	    srcArray[i] = ((float) value - 128.0f) / (255.0f);
	}

	fft.noLimits();
	fft.envelope(0.0f);
	fft.damp(1.0f);
	fft.equalizer(false);
	fft.smooth = false;
	fft.getSpectrum(srcArray, 0);
	double cut = 0.2;
	for (int index = 0; index < fft.spectrum.length; index++)
	{
	    double value = fft.spectrum[index] * 20;
	    if (value < cut)
		value = cut;
	    if (value > 1)
		value = 1;

	    value = main.map(value, cut, 1, 0, 1);
	    //System.out.println(index + " " + value);
	    spectrum[index] = value;
	}
	newLevel = main.constrain(fft.getLevel(srcArray, 0, srcArray.length) * 2.0f - 0.05f, 0, 1);
    }

    public void iterate()
    {
	iterateBands();
	iterateLevel();
    }

    public void iterateBands()
    {
	for (Band band : bands)
	{
	    double v1 = Math.exp(0.05776 * (band.index - 49)) * mul;
	    double v2 = Math.exp(0.05776 * (band.index - 49 + 1)) * mul;
	    band.position = v1 + offset;
	    band.size = (v2 - v1) * 1;
	    band.compute(spectrum);
	}
    }

    public void iterateLevel()
    {
	levelVelocity += (level - levelSpring) * 0.03;
	levelVelocity *= 0.95;

	levelSpring += levelVelocity;
	levelSpringIncrement += levelSpring;

	level = newLevel;
	levelSmooth += (level - levelSmooth) * 0.01;
    }

    public double highBands()
    {
	double result = 0;
	for (int index = Config.BANDS_NUM / 2; index < Config.BANDS_NUM; index++)
	{
	    result += bands.get(index).valueSmooth;
	}
	return result;
    }

    public double lowBands()
    {
	double result = 0;
	for (int index = 0; index < Config.BANDS_NUM / 2; index++)
	{
	    result += bands.get(index).valueSmooth;
	}
	return result;
    }
}
