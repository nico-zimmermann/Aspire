package sing.model;

public class RGB
{
    public double r;
    public double g;
    public double b;
    public double alpha;

    public RGB()
    {
	set(0, 0, 0);
    }

    public RGB(double r, double g, double b)
    {
	set(r, g, b);
    }

    public RGB(int r, int g, int b)
    {
	set(r, g, b);
    }

    public void set(double r, double g, double b)
    {
	this.r = r;
	this.g = g;
	this.b = b;
    }

    public void scale(double v)
    {
	this.r = r * v;
	this.g = g * v;
	this.b = b * v;
    }

    public RGB cloneRGB()
    {
	RGB clone = new RGB();
	clone.set(r, g, b);
	return clone;
    }
}
