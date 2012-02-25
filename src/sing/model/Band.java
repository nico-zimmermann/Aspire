package sing.model;

import sing.Config;

public class Band
{
    public Analyzer analyzer;
    public double position;
    public double size;
    public double value;
    public double valueSmooth = 0;
    public double valueSmooth2 = 0;
    public double index;
    public double valueSmoothSlow;
    public double energy;
    public double energySmooth;
    public float runningPosition = 0;

    public void compute(double[] spectrum)
    {
	runningPosition += 0.02;
	if(runningPosition > Config.BANDS_NUM)
	    runningPosition = 0;
	
	value = 0;
	double diffRunning = Math.abs(runningPosition - index);
	double runingRadius = 1;
	if (diffRunning < runingRadius && analyzer.levelSmooth < 0.02)
	    value = (runingRadius - diffRunning) / runingRadius * 0.5;
	
	int from = (int) Math.round(spectrum.length * position);
	int to = (int) Math.round(spectrum.length * (position + size));
	if (to == from)
	    to = from + 1;
	for (int i = from; i < to; i++)
	{
	    if (i > 0 && i < spectrum.length)
	    {
		double v = spectrum[i] * 10;
		value += v;
	    }
	}
	if (value < 0)
	    value = 0;
	if (value > 1)
	    value = 1;

	double speed = (value - valueSmooth);
	valueSmoothSlow += speed * 0.01;
	valueSmooth += speed * 0.1;
	valueSmooth2 += speed * 0.3;

	energy = value;

	double diff = (energy - energySmooth);
	if (diff < 0)
	    diff *= ((analyzer.levelSmooth + analyzer.levelSpring) * 0.5 + 0.1) * 0.3;
	else
	    diff *= ((analyzer.levelSmooth + analyzer.levelSpring) * 0.5 + 0.1) * 0.2 + 0.1;

	energySmooth += diff;
    }

    public double exp2(double t, double length)
    {
	double t2 = (t - length * position) / (length * size * 0.5);
	return Math.exp(-(t2 * t2));
    }
}
