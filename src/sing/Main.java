package sing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import processing.core.PApplet;
import processing.serial.Serial;
import sing.model.Analyzer;
import sing.model.Band;
import sing.model.Particle;
import sing.model.Programms;
import sing.remote.PortLights;
import sing.remote.PortWaveform;
import sing.ui.View;

public class Main extends PApplet
{
    public Analyzer analyzer;

    PortLights portLights;
    PortWaveform portWaveform;
    View view;
    Programms model;

    private int lastLoopStart;

    private int lastDrawDuration;

    public boolean inDraw;

    public boolean waitForModelDraw;

    public boolean hideView;

    public void setup()
    {
	frameRate(20);
	smooth();
	size(582, 600);
	background(0);
	textFont(createFont("Monaco", 10));

	analyzer = new Analyzer(this);

	model = new Programms();
	model.main = this;
	model.analyzer = analyzer;
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

	portLights = new PortLights(this, "/dev/tty.usbmodemfa131");
	portLights.createPort();

	portWaveform = new PortWaveform(this, "/dev/tty.usbmodemfd121");
	portWaveform.createPort();

	view = new View(this, model);
	view.init();
    }

    public void exit()
    {
	portLights.exit();
	portWaveform.exit();
    }

    public void iterate()
    {
	model.millis = millis();
	model.iterate();

	portLights.setRGBs(model.rgb);

	analyzer.iterate();
    }

    public void draw()
    {
	frameRate(hideView ? 10 : 30);
	measureStats();
	background(0);
	if (!hideView)
	{
	    drawAudio();
	    drawBands();
	    drawStats();
	    drawModel();
	}
    }

    public void handleWaveform()
    {
	analyzer.setInput(portWaveform.waveform);
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
	    text("SOUND: " + portWaveform.lastLoopDuration + "ms " + (int) (1000 / portWaveform.lastLoopDuration) + "/s", 12, 21);
	    text("LIGHT: " + portLights.lastLoopDuration + "ms " + (int) (1000 / portLights.lastLoopDuration) + "/s", 12, 31);
	    text("DRAW : " + lastDrawDuration + "ms " + (int) (1000 / lastDrawDuration) + "/s", 12, 41);

	    stroke(Config.COLOR_MEDIUM);
	    fill(Config.COLOR_DARK);
	    rect(115, 2 + 10, 149, 8);
	    rect(115, 2 + 20, 149, 8);
	    rect(115, 2 + 30, 149, 8);
	    fill(Config.COLOR_BRIGHT);
	    rect(115, 2 + 10, 149 * map(1000 / portWaveform.lastLoopDuration, 0, 60, 0, 1), 8);
	    rect(115, 2 + 20, 149 * map(1000 / portLights.lastLoopDuration, 0, 60, 0, 1), 8);
	    rect(115, 2 + 30, 149 * map(1000 / lastDrawDuration, 0, 60, 0, 1), 8);
	    noFill();

	} catch (Exception e)
	{
	}
    }

    private void drawModel()
    {
	ArrayList<Particle> particles = new ArrayList<Particle>();
	particles.addAll(model.particles2);
	int width = 255;
	int height = 255;

	int offsetX1 = 10;
	int offsetY1 = 335;

	int cx1 = offsetX1 + width / 2;
	int cy1 = offsetY1 + height / 2;

	int offsetX2 = 10 + 255 + 10;
	int offsetY2 = 335;

	int cx2 = offsetX2 + width / 2;
	int cy2 = offsetY2 + height / 2;

	fill(0);
	stroke(Config.COLOR_MEDIUM);
	rect(offsetX1, offsetY1, width, height);
	rect(offsetX2, offsetY2, width, height);

	stroke(Config.COLOR_DARK);
	ellipse(cx1, cy1, 200, 200);
	ellipse(cx2, cy2, 200, 200);

	stroke(Config.COLOR_MEDIUM);

	if (lowPerformance())
	    return;
	Collections.sort(particles, new Comparator<Particle>()
	{
	    @Override
	    public int compare(Particle p0, Particle p1)
	    {
		double z0 = p0.position.z;
		double z1 = p1.position.z;
		if (z0 < z1)
		    return -1;
		else if (z0 > z1)
		    return 1;
		else
		    return 0;
	    }

	});
	for (Particle particle : particles)
	{
	    if (lowPerformance())
		return;
	    drawParticle(particle, particle.position.x * 100 + cx1, particle.position.y * 100 + cy1, particle.radius * 100 * map(particle.position.z, -1, 1, 0.9, 1.1));
	}
	
	Collections.sort(particles, new Comparator<Particle>()
	{
	    @Override
	    public int compare(Particle p0, Particle p1)
	    {
		double z0 = p0.position.y;
		double z1 = p1.position.y;
		if (z0 < z1)
		    return -1;
		else if (z0 > z1)
		    return 1;
		else
		    return 0;
	    }

	});

	for (Particle particle : particles)
	{
	    if (lowPerformance())
		return;
	    drawParticle(particle, particle.position.x * 100 + cx2, -particle.position.z * 100 + cy2, particle.radius * 100 * map(particle.position.y, -1, 1, 0.9, 1.1));
	}
    }

    public double map(double y, double i, double j, double d, double e)
    {
	return map((float) y, (float) i, (float) j, (float) d, (float) e);
    }

    private void drawParticle(Particle particle, double x, double y, double radius)
    {
	fill(particle.color.r, particle.color.g, particle.color.b, particle.color.alpha);
	ellipse(x, y, radius + 1, radius + 1);
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
	int[] waveform = analyzer.waveform;

	// clear
	fill(Config.COLOR_DARK);
	stroke(Config.COLOR_MEDIUM);
	rect(10, 10, Config.WAVEFORM_SIZE, 255);
	rect(Config.WAVEFORM_SIZE + 20, 10, Config.WAVEFORM_SIZE, 255);

	// waveform
	stroke(Config.COLOR_BRIGHT);
	for (int i = 0; i < Config.WAVEFORM_SIZE - 1; i++)
	    line(i + 10, waveform[i] + 10, i + 1 + 10, waveform[i + 1] + 10);

	// level
	stroke(Config.COLOR_MEDIUM);
	fill(Config.COLOR_DARK);
	rect(Config.WAVEFORM_SIZE * 2 + 30, 0 + 10, 30, 255);
	fill(Config.COLOR_BRIGHT);
	rect(Config.WAVEFORM_SIZE * 2 + 30, 255 + 10, 10, -255.0f * (float) analyzer.level);
	rect(Config.WAVEFORM_SIZE * 2 + 30 + 10, 255 + 10, 10, -255.0f * (float) analyzer.levelSmooth);
	rect(Config.WAVEFORM_SIZE * 2 + 30 + 20, 255 + 10, 10, -255.0f * (float) analyzer.levelSpring);
	noFill();

		
	stroke(Config.COLOR_BRIGHT);
	double cutY = 255 - analyzer.cutoff * 255 + 10;
	line(Config.WAVEFORM_SIZE + 22, (float)cutY, Config.WAVEFORM_SIZE * 2 + 22, (float)cutY);
	// fft
	for (int i = 0; i < Config.SPECTRUM_SIZE - 1; i++)
	{
	    stroke(Config.COLOR_MEDIUM);
	    line(i * 2 + Config.WAVEFORM_SIZE + 22, (float) (255 - analyzer.fft.spectrum[i] * analyzer.fftScale * 255 + 10), i * 2 + Config.WAVEFORM_SIZE + 22, 255 + 10);
	    stroke(Config.COLOR_BRIGHT);
	    line(i * 2 + Config.WAVEFORM_SIZE + 22, (float) (255 - analyzer.spectrum[i] * 255 + 10), i * 2 + Config.WAVEFORM_SIZE + 22, 255 + 10);
	}

    }
    public void drawBands()
    {
	// bands
	int index = 0;
	for (Band band : analyzer.bands)
	{
	    float v = (float) (255.0f * (band.energy * 2 + 0.05));
	    stroke(Config.COLOR_BRIGHT, v);
	    
	    float size = 11.25f;
	    float xOffset = size / 2 + index * size + 10;
	    
	    float x = (float) (band.position * Config.WAVEFORM_SIZE) * 1 + 255 + 20; 
	    float bsize = (float) (band.size * Config.WAVEFORM_SIZE);
	    
	    if(model.showBands)
	    {
	    noFill();
	    stroke(0xffff0000);
	    line(x, 10, x, Config.WAVEFORM_SIZE + 10);
	    stroke(0xff00ff00);
	    line(x + bsize, 10, x + bsize, Config.WAVEFORM_SIZE + 10);
	    }
	    
	    float value = (float) band.value;
	    stroke(Config.COLOR_MEDIUM, 255 * (value * 2 + 0.5f));
	    fill(0);
	    ellipse(xOffset, 297, size, size);
	    fill(Config.COLOR_BRIGHT, 255 * value);
	    ellipse(xOffset, 297, size, size);
	    ellipse(xOffset, 297, size, size);
	    
	    float value2 = (float) band.energySmooth * 1.0f;
	    stroke(Config.COLOR_MEDIUM, 255 * (value2 * 2 + 0.5f));
	    fill(Config.COLOR_BRIGHT, 255 * value2);
	    ellipse(xOffset, 320, size, size);
	    
	    int scale = 1;
	    if (index % scale == 0)
	    {
		fill(Config.COLOR_BRIGHT);
		textSize(8);
		textAlign(CENTER);
		text("" + (int) (band.index), xOffset, 280);
	    }
	    index++;
	}
    }
}
