package sing.model;

public class Color
{
    public double r;
    public double g;
    public double b;

    public double h;
    public double l;
    public double s;

    public double alpha;
    private boolean hls;

    public Color()
    {
    }

    public void useHLS()
    {
	hls = true;
    }

    public Color set(double r, double g, double b)
    {
	this.r = r;
	this.g = g;
	this.b = b;
	return this;
    }

    public void scale(double v)
    {
	this.r = r * v;
	this.g = g * v;
	this.b = b * v;
    }

    public Color cloneRGB()
    {
	Color clone = new Color();
	clone.set(r, g, b);
	return clone;
    }

    public void updateRGB()
    {
	if (hls)
	{
	    int rgb = java.awt.Color.HSBtoRGB((float) h, (float) s, (float) l);
	    java.awt.Color c = new java.awt.Color(rgb);
	    r = c.getRed() / 256.0;
	    g = c.getGreen() / 256.0;
	    b = c.getBlue() / 256.0;
	}
    }
}
