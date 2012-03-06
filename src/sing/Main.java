package sing;

import java.io.File;
import java.io.IOException;

import krister.Ess.AudioInput;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;

import processing.core.PApplet;
import processing.serial.Serial;
import sing.model.Analyzer;
import sing.model.Band;
import sing.remote.BandWriter;
import sing.remote.PortWaveform;
import sing.ui.View;

public class Main extends PApplet
{
    public Analyzer analyzer;

    PortWaveform portWaveform;
    View view;

    private int lastLoopStart;

    private int lastDrawDuration;

    private int soundStart;

    private BandWriter bandWriter;

    public void setup()
    {
	frameRate(20);
	smooth();
	size(512 + 70, 600);
	background(0);
	textFont(createFont("Monaco", 10));

	System.out.println("WAVEFORM_SIZE: " + Config.WAVEFORM_SIZE);
	System.out.println("SAMPLE_SIZE: " + Config.SAMPLE_SIZE);

	analyzer = new Analyzer(this);

	println(Serial.list());

	portWaveform = new PortWaveform(this, "/dev/tty.usbmodemfd121");
	portWaveform.createPort();

	view = new View(this);
	view.init();

	view.loadSettings();
	
	bandWriter = new BandWriter();
	bandWriter.analyzer = analyzer;
	bandWriter.prepare();
    }

    public void exit()
    {
	view.saveSettings();
	portWaveform.exit();
	bandWriter.close();
    }

    public void draw()
    {
	frameRate(30);
	background(0);

	analyzer.doAnalysis();
	analyzer.iterate();
	bandWriter.writeBands();
	measureStats();
	background(0);
	drawAudio();
	drawStats();
	drawBands();
    }

    public void handleWaveform()
    {
	if (Config.USE_SERIAL_AUDIO)
	    analyzer.setSerialInput(portWaveform.waveform);
    }

    private void measureStats()
    {
	lastDrawDuration = millis() - lastLoopStart;
	lastLoopStart = millis();
    }

    private void drawStats()
    {
	try
	{
	    fill(Config.COLOR_BRIGHT);
	    textAlign(LEFT);
	    textSize(10);
	    text("SOUND: " + portWaveform.lastLoopDuration + "ms", 12, 21);
	    text("DRAW : " + lastDrawDuration + "ms " + (int) (1000.0 / lastDrawDuration) + "/s", 12, 41);

	    stroke(Config.COLOR_MEDIUM);
	    fill(Config.COLOR_DARK);
	    rect(115, 2 + 20, 149, 8);
	    rect(115, 2 + 30, 149, 8);
	    fill(Config.COLOR_BRIGHT);
	    rect(115, 2 + 30, 149 * map(1000 / lastDrawDuration, 0, 60, 0, 1), 8);
	    noFill();
	} catch (Exception e)
	{
	}
    }

    public double map(double y, double i, double j, double d, double e)
    {
	return map((float) y, (float) i, (float) j, (float) d, (float) e);
    }

    private boolean lowPerformance()
    {
	return millis() - lastLoopStart > 50;
    }

    private void fill(double r, double g, double b, double a)
    {
	fill((float) r * 255, (float) g * 255, (float) b * 255, (float) a * 255);
    }

    private void fill(double r, double g, double b)
    {
	fill((float) r * 255, (float) g * 255, (float) b * 255);
    }

    private void ellipse(double x, double y, double width, double height)
    {
	ellipse((float) x, (float) y, (float) width, (float) height);
    }

    private void stroke(double r, double g, double b)
    {
	stroke((float) r, (float) g, (float) b, 255);
    }

    public void point(double x, double y)
    {
	point((float) x, (float) y);
    }

    public void drawAudio()
    {
	// clear
	fill(Config.COLOR_DARK);
	stroke(Config.COLOR_MEDIUM);
	rect(10, 10, 255, 255);
	rect(255 + 20, 10, 255, 255);

	// waveform
	stroke(Config.COLOR_BRIGHT);
	int scale = analyzer.waveform.length / 256;
	for (int i = 0; i < 255; i++)
	{
	    line(i + 10, analyzer.waveform[i * scale] * 128 + 128 + 10, i + 1 + 10, analyzer.waveform[(i + 1) * scale] * 128 + 128 + 10);
	}

	// level
	stroke(Config.COLOR_MEDIUM);
	fill(Config.COLOR_DARK);
	rect(255 * 2 + 30, 0 + 10, 30, 255);
	fill(Config.COLOR_BRIGHT);
	rect(255 * 2 + 30, 255 + 10, 10, -255.0f * (float) analyzer.level);
	rect(255 * 2 + 30 + 10, 255 + 10, 10, -255.0f * (float) analyzer.levelSmooth);
	rect(255 * 2 + 30 + 20, 255 + 10, 10, -255.0f * (float) analyzer.levelSpring);
	noFill();

	stroke(Config.COLOR_BRIGHT);
	double cutY = 255 - analyzer.cutoff * 255 + 10;
	line(256 + 20, (float) cutY, 255 * 2 + 20, (float) cutY);
	// fft
	scale = analyzer.fft.spectrum.length / 256;
	for (int i = 0; i < 256; i++)
	{
	    stroke(Config.COLOR_MEDIUM);
	    line(i + 256 + 20, (float) (255 - analyzer.fft.spectrum[i * scale] * analyzer.fftScale * 255 + 10), i + 256 + 20, 255 + 10);
	    stroke(Config.COLOR_BRIGHT);
	    line(i + 256 + 20, (float) (255 - analyzer.spectrum[i * scale] * 255 + 10), i + 256 + 20, 255 + 10);
	}
    }

    public void drawBands()
    {
	// bands
	int index = 0;
	for (Band band : analyzer.bands)
	{
	    float v = (float) (255.0f * (band.value * 2 + 0.05));
	    stroke(Config.COLOR_BRIGHT, v);

	    float size = 6.35f;
	    float xOffset = size / 2 + index * size + 10;

	    float x = (float) (band.position * Config.WAVEFORM_SIZE) * 1 + 255 + 20;
	    float bsize = (float) (band.size * Config.WAVEFORM_SIZE);

	    noFill();
	    stroke(0xffff0000);
	    // line(x, 10, x, 255 + 10);
	    stroke(0xff00ff00);
	    // line(x + bsize, 10, x + bsize, 255 + 10);

	    float value = (float) band.value;
	    stroke(Config.COLOR_MEDIUM, 255 * (value + 0.2f));
	    fill(0);
	    ellipse(xOffset, 297 + 150, size, size + value * 180.0);
	    fill(Config.COLOR_BRIGHT, 255 * value);
	    ellipse(xOffset, 297 + 150, size, size + value * 180.0);

	    int scale = 1;
	    if (index % scale == 0)
	    {
		fill(Config.COLOR_BRIGHT);
		textSize(8);
		textAlign(CENTER);
		text("" + (int) (band.index), xOffset, 280 + (index % 2) * 10);
	    }
	    index++;
	}
    }

    public void audioInputData(AudioInput theInput)
    {
	portWaveform.lastLoopDuration = millis() - soundStart;
	soundStart = millis();
	analyzer.audioInput();
    }
}
