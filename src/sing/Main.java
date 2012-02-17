package sing;

import krister.Ess.AudioInput;
import krister.Ess.Ess;
import krister.Ess.FFT;
import processing.core.PApplet;
import controlP5.ControlP5;
import controlP5.ControlWindow;

public class Main extends PApplet
{
    private static final long serialVersionUID = 1L;

    public PortHandler port;

    ControlP5 cp5;

    int bufferSize = 128;
    int numAverages = 32;
    FFT myFFT;
    float[] srcArray = new float[bufferSize];
    AudioInput myInput;

    public void setup()
    {
	port = new PortHandler(this);
	frameRate(60);
	smooth();
	size(600, 400);
	cp5 = new ControlP5(this);

	ControlWindow cw = cp5.addControlWindow("win", 700, 0, 400, 250);
	cp5.begin(cw, 10, 10);
	cp5.addButton("b3").setCaptionLabel("save default");
	cp5.addButton("b4").setCaptionLabel("load default").setColorBackground(color(0, 100, 50)).linebreak();
	cp5.addSlider("slider1", 50, 100).linebreak();
	cp5.addSlider("slider2").linebreak();
	cp5.addSlider("slider3").linebreak();
	cp5.addSlider("slider4").linebreak();
	cp5.addSlider("hello", 0, 100).linebreak();
	cp5.addToggle("toggleC");
	cp5.end();

	port.createPort();

	Ess.start(this);
	myInput = new AudioInput(bufferSize / 2);

	myFFT = new FFT(bufferSize);
	myFFT.equalizer(true);
	myFFT.smooth = true;
	float minLimit = .005f;
	float maxLimit = .05f;
	myFFT.limits(0.005f, 0.05f);
	myFFT.damp(0.5f);
	myFFT.averages(numAverages);
	float limitDiff = maxLimit - minLimit;
	myInput.start();

	// cp5.loadProperties(("default.ser"));
    }

    public void exit()
    {
	port.stop();
    }

    public void draw()
    {
	port.run();
    }

    void b3(float v)
    {
	cp5.saveProperties("default.ser", "default");
    }

    void b4(float v)
    {
	cp5.loadProperties(("default.ser"));
    }

    public void audioInputData(AudioInput theInput)
    {
	myFFT.getSpectrum(myInput);
    }

    public void handleCommand(int commandId, byte[] bytes)
    {
	fill(0);
	noStroke();
	rect(0, 0, 128, 255);
	stroke(0xff);
	for (int i = 0; i < bytes.length; i++)
	{
	    int value = bytes[i];
	    srcArray[i] = (value - 128) / 255;
	    point(i, value);
	}

	myFFT.getSpectrum(srcArray, 0);
	stroke(255, 0, 0);
	for (int i = 0; i < bufferSize / 2; i++)
	{
	    point(i, myFFT.spectrum[i] * 200);
	}
    }
}
