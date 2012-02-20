package sing;

public class Lights
{
    public float low;
    public float low0;
    public float lowV;
    public float high;
    public float high0;
    public float highV;
    private final Main main;

    public Lights(Main main)
    {
	this.main = main;
    }

    public void setSpectrum(float[] spectrum)
    {
	float newLow = 0f;
	float newHigh = 0f;
	for (int i = 0; i < spectrum.length; i++)
	{
	    float lowCoeff = main.constrain(main.map(i, 0, 10, 20, 0), 0, 20);
	    float highCoeff = main.constrain(main.map(i, 8, spectrum.length, 1, 5), 0, 5);

	    float v = spectrum[i];
	    v -= 0.00;
	    if (v < 0)
		v = 0;
	    newLow += v * lowCoeff;
	    newHigh += v * highCoeff;
	}
	
	lowV += (main.constrain(newLow, 0, 1) - low0) * 0.25f;
	highV += (main.constrain(newHigh, 0, 1) - high0) * 0.25f;
    }

    public void iterate()
    {
	lowV *= 0.9;
	highV *= 0.9;
	
	low0 = lowV * 0.9f;
	high0 = highV * 0.9f;
	
	low = (float)Math.pow(low0, 3f);
	high = (float)Math.pow(high0, 3f);
    }

}
