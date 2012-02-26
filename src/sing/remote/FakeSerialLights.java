package sing.remote;

import processing.serial.Serial;
import sing.remote.FakeSerial.FakeSerialPort;

public class FakeSerialLights extends FakeSerial
{
    public FakeSerialLights(Serial port)
    {
	super(port);
    }
    
    public void loop()
    {
	readRGB();
	sendOk();
    }

    private void sendOk()
    {
	input.write(0);
    }

    private void readRGB()
    {
	boolean waiting = true;
	while (waiting)
	{
	    if (output.available() == 30)
	    {
		output.clear();
		waiting = false;
	    }
	    delay(10);
	}
    }
    
}
