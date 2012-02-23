package sing.program;

import sing.model.ParticleS;

public class Calibrate extends Program
{
    public ParticleS reference = new ParticleS();
    public Interpolation mode = Interpolation.POW;

    @Override
    public void iterate()
    {
	reference.color.set(1, 1, 1);
	show(reference, mode);
    }
}
