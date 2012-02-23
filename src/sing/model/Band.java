package sing.model;

public class Band
{
    public double position;
    public double size;
    public double value;
    public double valueSmooth = 0;

    public void compute(float[] spectrum)
    {
	value = 0;
	for (int i = 0; i < spectrum.length; i++)
	{
	    double weight = exp2(i, spectrum.length);
	    //value += Math.pow(spectrum[i] * weight * 50, 2) * 0.2;
	    value += spectrum[i] * weight * 15;
	}
	
	valueSmooth += (value - valueSmooth) * 0.1;
    }

    public double exp2(double t, double length)
    {
	double t2 = (t - length * position) / (length * size * 0.5);
	return Math.exp(-(t2 * t2));
    }
}
