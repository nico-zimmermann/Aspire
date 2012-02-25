package sing.model;

import javax.vecmath.Vector3d;

public class LED
{
    public SPoint positionS = new SPoint();
    public Vector3d positionV = new Vector3d();

    public void precalculate()
    {
	positionV = positionS.getPosition();	
    }
}
