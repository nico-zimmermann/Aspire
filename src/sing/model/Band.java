package sing.model;

import sing.Config;

public class Band
{
    public Analyzer analyzer;
    public double position;
    public double size;
    public double value;
    public double index;

    public void compute(double[] spectrum)
    {

	value = 0;
	int from = (int) Math.round(spectrum.length * position);
	int to = (int) Math.round(spectrum.length * (position + size));
	if (to == from)
	    to = from + 1;
	for (int i = from; i < to; i++)
	{
	    if (i > 0 && i < spectrum.length)
	    {
		double v = spectrum[i];
		value += v;
	    }
	}
	
	value /= to - from;
	
	if (value > 1)
	    value = 1;

	//System.out.println(value);
    }
}
