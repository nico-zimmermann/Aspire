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
	double noiseValue = getAmplitude(ms * 0.01) * 00 + 0;
	for (int i = 0; i < Config.WAVEFORM_SIZE; i++)
	{
	    double value = 128
		    + Math.sin(i * 0.01) * getAmplitude(ms * 0.005) * 0
		    + Math.sin(i * 0.5) * getAmplitude(ms * 0.01 + 1) * 0
		    + Math.sin(i * Math.PI / Config.WAVEFORM_SIZE * 2 * 254) * 0
		    + Math.sin(i * Math.PI / (Config.WAVEFORM_SIZE / 2.0) * Math.pow(2, (0 * 12) / 12.0)) * 5
		    + Math.sin(i * Math.PI / (Config.WAVEFORM_SIZE / 2.0) * Math.pow(2, (1 * 12) / 12.0)) * 5
		    + Math.sin(i * Math.PI / (Config.WAVEFORM_SIZE / 2.0) * Math.pow(2, (2 * 12) / 12.0)) * 5
		    + Math.sin(i * Math.PI / (Config.WAVEFORM_SIZE / 2.0) * Math.pow(2, (3 * 12) / 12.0)) * 5
		    + Math.sin(i * Math.PI / (Config.WAVEFORM_SIZE / 2.0) * Math.pow(2, (4 * 12) / 12.0)) * 5
		    + Math.sin(i * Math.PI / (Config.WAVEFORM_SIZE / 2.0) * Math.pow(2, (5 * 12) / 12.0)) * 5
		    + Math.sin(i * Math.PI / (Config.WAVEFORM_SIZE / 2.0) * Math.pow(2, (6 * 12 + 0) / 12.0)) * 5
		    + Math.sin(i * Math.PI / (Config.WAVEFORM_SIZE / 2.0) * Math.pow(2, (7 * 12 - 1) / 12.0)) * 5
		    + Math.sin(i * 1.9) * 0
		    + Math.sin(i * 2.9) * 0
		    + (Math.random() * 0.5) * noiseValue;
	    
	    int result = (int) Math.round(value);
	    if (result > 255)
		result = 255;
	    if (result < 0)
		result = 0;
	    input.write(result);
	}
	delay(40);
    }

    private double getAmplitude(double d)
    {
	return (Math.sin(d) + 1) * 0.5;
    }
}
