package sing.model;

import java.util.ArrayList;

import krister.Ess.AudioInput;
import krister.Ess.Ess;
import krister.Ess.FFT;
import sing.Config;
import sing.Main;

public class Analyzer
{
    public float[] waveform = new float[Config.WAVEFORM_SIZE];
    public double[] spectrum = new double[Config.SPECTRUM_SIZE];
    public FFT fft;

    public double level;
    public double levelVelocity;
    public double levelSmooth;
    public double levelSpring;
    public double levelSpringIncrement;

    public double offset = 0;
    public double mul = 1;
    public double cutoff;
    public boolean autoCutoff;
    public double autoCutoffValue;
    public double autoCutoffSmoothValue;
    public double fftScale;

    public ArrayList<Band> bands = new ArrayList<Band>();

    Main main;
    private double newLevel;
    public boolean autoCutoffSmooth;
    public boolean autoCutoffModePow;
    public double autoCutoffModePowExponent;
    public float inputOffset;
    private AudioInput myInput;

    public Analyzer(Main main)
    {
	this.main = main;

	Ess.start(main);

	myInput = new AudioInput(Config.WAVEFORM_SIZE);
	if (!Config.USE_SERIAL_AUDIO)
	    myInput.start();

	fft = new FFT(Config.WAVEFORM_SIZE);
	fft.averages(32);

	levelSmooth = 0;
	levelSpring = 0;
	levelSpringIncrement = 0;
	levelVelocity = 0;

	createBands();
    }

    private void createBands()
    {
	double e = 0.05776;
	mul = 0.0083;
	offset = 0;
	for (double i = 0; i < Config.BANDS_NUM; i++)
	{
	    Band band = new Band();
	    band.analyzer = this;
	    band.index = i;
	    double v1 = (Math.exp(e * (band.index)) - Math.exp(0)) * mul;
	    double v2 = (Math.exp(e * (band.index + 1)) - Math.exp(0)) * mul;
	    band.position = v1 + offset;
	    band.size = (v2 - v1) * 1;
	    bands.add(band);
	    System.out.println(v1 * Config.SPECTRUM_SIZE);
	}
    }

    public void audioInput()
    {
	for (int i = 0; i < Config.WAVEFORM_SIZE; i++)
	    waveform[i] = myInput.buffer[i];

	doAnalysis();
    }

    public void setSerialInput(int[] newWaveform)
    {
	for (int i = 0; i < Config.WAVEFORM_SIZE; i++)
	{
	    int value = newWaveform[i];
	    waveform[i] = (value + inputOffset) / 256 - 0.5f;
	    waveform[i] = waveform[i] * 2;
	}

	doAnalysis();
    }

    private void doAnalysis()
    {
	fft.noLimits();
	fft.envelope(0.0f);
	fft.damp(1.0f);
	fft.equalizer(true);
	fft.smooth = false;
	fft.getSpectrum(waveform, 0);

	if (autoCutoff)
	{
	    double newCutoff = 0;

	    if (autoCutoffModePow)
	    {
		double summ = 0;
		for (int index = 0; index < fft.spectrum.length; index++)
		{
		    summ += Math.pow(fft.spectrum[index] * fftScale, autoCutoffModePowExponent);
		}
		newCutoff = Math.sqrt(summ / fft.spectrum.length);
	    }
	    else
	    {
		double summ = 0;
		for (int index = 0; index < fft.spectrum.length; index++)
		{
		    summ += fft.spectrum[index] * fftScale;
		}
		newCutoff = summ / fft.spectrum.length;
	    }

	    if (autoCutoffSmooth)
	    {
		newCutoff = cutoff + (newCutoff - cutoff) / autoCutoffSmoothValue;
	    }

	    cutoff = newCutoff;
	}

	for (int index = 0; index < fft.spectrum.length; index++)
	{
	    double value = fft.spectrum[index] * fftScale;

	    if (value < cutoff)
		value = cutoff;
	    if (value > 1)
		value = 1;

	    value = main.map(value, cutoff, 1, 0, 1);
	    spectrum[index] = value;
	}
	newLevel = main.constrain(fft.getLevel(waveform, 0, Config.WAVEFORM_SIZE) * 1.0f - 0.03f, 0, 1);

    }

    public void iterate()
    {
	iterateBands();
	iterateLevel();
    }

    public void iterateBands()
    {
	for (Band band : bands)
	    band.compute(spectrum);
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
