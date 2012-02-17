package sing;

public class Lights
{
    public float low;
    public float high;
    private final Main main;

    public Lights(Main main)
    {
	this.main = main;
    }

    public void setSpectrum(float[] spectrum)
    {
	float newLow = 0f;
	float newHigh = 0f;
	for (int i = 4; i < spectrum.length; i++)
	{
	    float lowCoeff = main.constrain(main.map(i, 8, 10, 5, 0), 0, 5);
	    float highCoeff = main.constrain(main.map(i, 10, spectrum.length, 0, 5), 0, 5);

	    newLow += spectrum[i] * lowCoeff;
	    newHigh += spectrum[i] * highCoeff;
	}

	low = newLow;
	high = newHigh;
    }

    public void iterate()
    {
    }

}
