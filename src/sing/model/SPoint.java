package sing.model;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

public class SPoint
{
    public double inclination;
    public double azimuth;

    public void setAzimuth(double value)
    {
	azimuth = value;
    }

    public void setInclination(double value)
    {
	inclination = value;
    }

    public double getDistance(SPoint sPoint)
    {
	Vector3d sPointP = sPoint.getPosition();
	Vector3d thisP = this.getPosition();
	sPointP.sub(thisP);
	return sPointP.length() / 2.0;
    }

    public double getDistance(Vector3d vector)
    {
	Vector3d vPos = (Vector3d) vector.clone();
	Vector3d thisP = this.getPosition();
	vPos.sub(thisP);
	return vPos.length() / 2.0;
    }
    
    private Vector3d getPosition()
    {
	Vector3d result = new Vector3d();
	result.x = Math.sin(inclination) * Math.cos(azimuth);
	result.y = Math.sin(inclination) * Math.sin(azimuth);
	result.z = Math.cos(inclination);
	return result;
    }
}
