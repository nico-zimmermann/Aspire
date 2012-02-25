package sing.model;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

public class SPoint
{
    public double inclination;
    public double azimuth;

    public Vector3d getPosition()
    {
	Vector3d result = new Vector3d();
	result.x = Math.sin(inclination) * Math.cos(azimuth);
	result.y = Math.sin(inclination) * Math.sin(azimuth);
	result.z = Math.cos(inclination);
	return result;
    }
}
