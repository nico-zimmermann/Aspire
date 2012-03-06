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
    public boolean equalizer;
    public boolean smoothing;

    public Analyzer(Main main)
    {
	this.main = main;

	Ess.start(main);

	myInput = new AudioInput(Config.SAMPLE_SIZE);
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
	for (double i = 0; i < Config.BANDS_NUM; i++)
	{
	    Band band = new Band();
	    band.analyzer = this;
	    band.index = i;
	    bands.add(band);
	}
    }

    synchronized public void audioInput()
    {
	// shift left
	for (int i = Config.SAMPLE_SIZE; i < Config.WAVEFORM_SIZE; i++)
	{
	    waveform[i - Config.SAMPLE_SIZE] = waveform[i];
	}

	// append right
	for (int i = 0; i < Config.SAMPLE_SIZE; i++)
	{
	    waveform[i + Config.WAVEFORM_SIZE - Config.SAMPLE_SIZE] = myInput.buffer[i];
	}

	// doAnalysis();
    }

    synchronized public void setSerialInput(int[] newWaveform)
    {
	for (int i = 0; i < Config.WAVEFORM_SIZE; i++)
	{
	    int value = newWaveform[i];
	    waveform[i] = (value + inputOffset) / 256 - 0.5f;
	    waveform[i] = waveform[i] * 2;
	}

	doAnalysis();
    }

    synchronized public void doAnalysis()
    {
	fft.noLimits();
	fft.envelope(0.0f);
	fft.damp(1.0f);
	fft.equalizer(equalizer);
	fft.smooth = smoothing;
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
	newLevel = main.constrain(fft.getLevel(waveform, Config.WAVEFORM_SIZE - Config.SAMPLE_SIZE, Config.SAMPLE_SIZE) * 1.0f - 0.03f, 0, 1);

    }

    public void iterate()
    {
	iterateBands();
	iterateLevel();
    }

    public void iterateBands()
    {
	double scale = (float) Config.SAMPLING_RATE / Config.WAVEFORM_SIZE;
	int fromTone = 0;
	int toTone = fromTone + 1;
	for (Band band : bands)
	{
	    band.position = 0;
	    band.size = 0;

	    while (band.size * spectrum.length < 1)
	    {
		double frequency0 = 440 * Math.pow(2, (fromTone - 0.5 - 69) / 12.0);
		double frequency1 = 440 * Math.pow(2, (toTone + 0.5 - 69) / 12.0);
		double v0 = (frequency0 / scale) / spectrum.length;
		double v1 = (frequency1 / scale) / spectrum.length;
		band.position = v0;
		band.size = (v1 - v0);
		toTone++;
	    }
	    band.compute(spectrum);
	    fromTone = toTone;
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
