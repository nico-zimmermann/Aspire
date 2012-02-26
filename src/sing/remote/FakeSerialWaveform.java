package sing.remote;

import processing.serial.Serial;
import sing.Config;
import sing.remote.FakeSerial.FakeSerialPort;

public class FakeSerialWaveform extends FakeSerial
{
    public FakeSerialWaveform(Serial port)
    {
	super(port);
    }

    public void loop()
    {
	sendWaveform();
    }

    private void sendWaveform()
    {
	long ms = System.currentTimeMillis();
	double noiseValue = getAmplitude(ms * 0.01) * 00 + 10;
	for (int i = 0; i < Config.WAVEFORM_SIZE; i++)
	{
	    double value = 128
		    + Math.sin(i * 0.2) * getAmplitude(ms * 0.005) * 10
		    + Math.sin(i * 0.5) * getAmplitude(ms * 0.01 + 1) * 10
		    + Math.sin(i * 0.9) * 10
		    + Math.sin(i * 1.9) * 5
		    + Math.sin(i * 2.9) * 2
		    - 9
		    + (Math.random() * 0.5) * noiseValue;
	    input.write((int) value);
	}
	delay(40);
    }

    private double getAmplitude(double d)
    {
	return (Math.sin(d) + 1) * 0.5;
    }
}
