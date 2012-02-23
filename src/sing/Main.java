package sing;

import krister.Ess.FFT;
import processing.core.PApplet;
import processing.serial.Serial;
import sing.model.Band;
import sing.model.Model;
import sing.model.Spectrum;
import sing.ui.View;

public class Main extends PApplet
{
    Port port;
    PortSpectrum portSpectrum;
    View view;
    FFT fft;
    Model model;

    private Spectrum spectrum;

    public void setup()
    {
	frameRate(30);
	smooth();
	size(600, 400);

	spectrum = new Spectrum(this);

	model = new Model();
	model.main = this;
	model.spectrum = spectrum;
	model.calibrateLED(0, 45, 90 + 22);
	model.calibrateLED(1, -90 / 4 * 3, 90);
	model.calibrateLED(2, 45 + 10, 90 - 45);
	model.calibrateLED(3, 0, 90);
	model.calibrateLED(4, 180 - 10, 90 - 45 - 20);
	model.calibrateLED(5, 180 - 45 + 10, 90);
	model.calibrateLED(6, -180 + 22, 90 - 10);
	model.calibrateLED(7, 90, 90 - 15);
	model.calibrateLED(8, -40, 90 - 45);
	model.calibrateLED(9, -180 + 45 + 20, 45 + 10);
	model.initPrograms();

	println(Serial.list());

	port = new Port(this, "/dev/tty.usbmodemfa131");
	port.createPort();

	portSpectrum = new PortSpectrum(this, "/dev/tty.usbmodemfd121");
	portSpectrum.createPort();

	fft = new FFT(Config.SPECTRUM_SIZE);
	fft.averages(32);

	view = new View(this, model);
	view.init();
    }

    public void exit()
    {
	port.exit();
	portSpectrum.exit();
    }

    public void draw()
    {
	background(0);
	view.fpsLabel.setText("s:" + portSpectrum.lastLoopDuration + " c:" + port.lastLoopDuration);

	spectrum.iterate();

	model.millis = millis();
	model.iterate();
	
	model.globalAlpha = 0.2 + spectrum.levelSpring * 0.7;

	handleCommand(portSpectrum.spectrum);

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

    public void handleCommand(int[] data)
    {
	fill(0xff002200);
	stroke(0xff006600);
	rect(0, 0, 128, 255);
	
	stroke(30, 150, 30);
	float[] srcArray = new float[Config.SPECTRUM_SIZE];
	for (int i = 0; i < data.length - 1; i++)
	{
	    int value = data[i];
	    line(i, data[i], i + 1, data[i + 1]);
	}

	for (int i = 0; i < data.length; i++)
	{
	    int value = data[i];
	    srcArray[i] = ((float) value - 128.0f) / 255.0f;
	}

	fft.getSpectrum(srcArray, 0);
	float level = constrain(fft.getLevel(srcArray, 0, srcArray.length) * 2.0f - 0.01f, 0, 1);
	spectrum.setSpectrum(fft.spectrum);
	spectrum.setLevel(level);

	stroke(0xff006600);
	fill(0, 0x33, 0);
	rect(128, 0, 30, 255);
	fill(0, 255, 0);
	rect(128, 0, 10, 255.0f * (float) spectrum.level);
	rect(128 + 10, 0, 10, 255.0f * (float) spectrum.levelSmooth);
	rect(128 + 20, 0, 10, 255.0f * (float) spectrum.levelSpring);
	noFill();

	int index = 0;
	for (Band band : spectrum.bands)
	{
	    for (int i = 0; i < fft.spectrum.length - 1; i++)
	    {
		stroke(255, 0, 255);
		float v0 = (float) (band.exp2(i, fft.spectrum.length) * 128);
		float v1 = (float) (band.exp2(i + 1, fft.spectrum.length) * 128);
		line(i * 2, v0, (i + 1) * 2, v1);
	    }

	    float value = (float) band.value;
	    stroke(0, 0xff * (value + 0.2f), 0);
	    fill(0, 0xff * value, 0);
	    ellipse(40 + index * 40, 290, 40, 40);
	    index++;
	}
	
	for (int i = 0; i < fft.spectrum.length - 1; i++)
	{
	    stroke(0, 255, 0);
	    line(i * 2, fft.spectrum[i] * 3000, (i + 1) * 2, fft.spectrum[i + 1] * 3000);
	}
    }

    private double exp2(double t, double position, double size)
    {
	t = (t - fft.spectrum.length * position) / (fft.spectrum.length * size * 0.5);
	return Math.exp(-(t * t));
    }
}
