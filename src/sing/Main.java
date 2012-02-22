package sing;

import krister.Ess.AudioInput;
import krister.Ess.Ess;
import krister.Ess.FFT;
import processing.core.PApplet;
import sing.ui.View;
import controlP5.ControlP5;
import controlP5.ControlWindow;

public class Main extends PApplet
{
    static final int BUFFER_SIZE = 128;

    Port port;
    View view;
    FFT fft;
    Model model;

    private Lights lights;

    public void setup()
    {
	frameRate(30);
	smooth();
	size(600, 400);

	model = new Model();
	model.main = this;
	model.initPrograms();
	model.calib(0, 45, 90 + 22);
	model.calib(1, -90 / 4 * 3, 90);
	model.calib(2, 45 + 10, 90 - 45);
	model.calib(3, 0, 90);
	model.calib(4, 180 - 10, 90 - 45 - 20);
	model.calib(5, 180 - 45 + 10, 90);
	model.calib(6, -180 + 22, 90 - 10);
	model.calib(7, 90, 90 - 15);
	model.calib(8, - 40, 90 - 45);
	model.calib(9, -180 + 45 + 20, 45 + 10);


	port = new Port(this);
	port.createPort();

	fft = new FFT(BUFFER_SIZE);
	fft.averages(32);

	lights = new Lights(this);

	view = new View(this, model);
	view.init();
    }

    public void exit()
    {
	port.exit();
    }

    public void draw()
    {
	background(0);
	view.fpsLabel.setText("CRYSTAL FPS: " + port.lastLoopDuration);
	
	lights.iterate();
	
	model.millis = millis();
	model.iterate();
	
	port.setRGBs(model.rgb);
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
	// port.setLED(3, 1 + Math.round(level * 254));
	// port.setLED(3, 0);

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

	// port.setLED(9, 1 + Math.round((lights.low * level + 0.2f) * 254));
	// port.setLED(10, 1);
	// port.setLED(11, 1 + Math.round((lights.high * level + 0.2f) * 254));
    }
}
