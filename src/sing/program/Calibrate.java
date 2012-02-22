package sing.program;

import sing.Config;
import sing.util.Interpolation;
import sing.util.Particle;
import sing.util.SPoint;


public class Calibrate extends Program
{
    public boolean enabled;
    public Particle reference = new Particle();
    public Interpolation mode = Interpolation.POW;

    @Override
    public void iterate()
    {
	if (!enabled)
	    return;

	reference.color.set(1, 1, 1);
	show(reference, mode);
    }
}
