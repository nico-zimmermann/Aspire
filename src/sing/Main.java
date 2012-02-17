package sing;

import krister.Ess.AudioInput;
import krister.Ess.Ess;
import krister.Ess.FFT;
import processing.core.PApplet;
import controlP5.ControlP5;
import controlP5.ControlWindow;

public class Main extends PApplet
{
    static final int BUFFER_SIZE = 128;

    PortHandler port;
    View view;
    FFT fft;

    private Lights lights;

    public void setup()
    {
	frameRate(60);
	smooth();
	size(600, 400);
	
	port = new PortHandler(this);
	port.createPort();

	fft = new FFT(BUFFER_SIZE);
	fft.averages(32);
	
	lights = new Lights(this);
	
	view = new View(this);
	view.init();
    }

    public void exit()
    {
	port.exit();
    }

    public void draw()
    {
	port.run();
	lights.iterate();
    }

    public void handleCommand(int commandId, int[] data)
    {
	fill(0xff002200);
	stroke(0xff006600);
	rect(0, 0, 128, 255);
	stroke(0xff66ff66);

	float[] srcArray = new float[BUFFER_SIZE];
	for (int i = 0; i < data.length - 1; i++)
	{
	    int value = data[i];
	    line(i, data[i], i + 1, data[i + 1]);
	    srcArray[i] = ((float) value - 128) / 255;
	}

	fft.getSpectrum(srcArray, 0);
	float level = constrain(fft.getLevel(srcArray, 0, srcArray.length) * 2.0f - 0.01f, 0, 1);
	port.setLED(9, 1 + Math.round(level * 254));

	stroke(0xff006600);
	fill(0, 0x33, 0);
	rect(128, 0, 10, 255);
	fill(255, 0, 0);
	rect(128, 0, 10, 255.0f * level);
	noFill();

	stroke(255, 0, 0);
	for (int i = 0; i < BUFFER_SIZE / 2 - 1; i++)
	{
	    line(i * 2, fft.spectrum[i] * 3000, (i + 1) * 2, fft.spectrum[i + 1] * 3000);
	}
	
	lights.setSpectrum(fft.spectrum);
	
	stroke(0, 0xff * (lights.low + 0.2f), 0);
	fill(0, 0xff * lights.low, 0);
	ellipse(40, 290, 40, 40);

	stroke(0, 0xff * (lights.high + 0.2f), 0);
	fill(0, 0xff * lights.high, 0);
	ellipse(40 + 50, 290, 40, 40);
    }

    public void setDamping(float value)
    {
	fft.damp(value);
    }

    public void setEQ(boolean enabled)
    {
	println("setEQ: " + enabled);
	fft.equalizer(enabled);

    }

    public void setSmoothing(boolean enabled)
    {
	fft.smooth = enabled;
    }
}
