package sing.program;

import sing.model.ParticleS;

public class Calibrate extends Program<Calibrate>
{
    public ParticleS reference = new ParticleS();
    public Interpolation mode = Interpolation.POW;
    private int frame;

    @Override
    public void iterate()
    {
	frame++;
	int c = frame % 2 == 0 ? 1 : 0;
	reference.color.set(c, c, c);
	show(reference, mode);
    }
}
