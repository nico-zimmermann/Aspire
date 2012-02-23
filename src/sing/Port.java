package sing;

import java.util.ArrayList;

import processing.serial.Serial;
import sing.model.RGB;

public class Port extends Thread
{
    private static final int ON = 1;
    private static final int LOW = 2;
    private static final int HIGH = 3;
    private static final int VERBOSE = LOW;

    public int lastLoopDuration;
    public float[] rgb = new float[Config.LEDS * 3];
    public int[] spectrum = new int[Config.SPECTRUM_SIZE];
    
    Serial port;
    Main main;
    String portName;

    public Port(Main main, String portName)
    {
	this.main = main;
	this.portName = portName;
    }

    void createPort()
    {
	try
	{
	    port = new Serial(main, portName, 115200);
	    info("Create port... available:" + port.available());
	    start();
	} catch (Exception e)
	{
	    System.err.println(e.getMessage());
	    System.exit(1);
	}
    }

    void exit()
    {
	port.stop();
    }

    public void run()
    {
	info("Wait...");
	delay(1000);
	while (true)
	{
	    loop();
	}
    }

    private void loop()
    {
	int start = main.millis();
	sendRGB();
	readOk();
	lastLoopDuration = main.millis() - start;
    }

    private void sendRGB()
    {
	for (int index = 0; index < Config.LEDS; index++)
	{
	    float r = rgb[index * 3 + 0];
	    float g = rgb[index * 3 + 1];
	    float b = rgb[index * 3 + 2];
	    setRGB(r, g, b);
	}
    }

    private void readOk()
    {
	boolean waiting = true;
	while (waiting)
	{
	    if (port.available() == 1)
	    {
		int b = 0;
		if ((b = readByte()) != 0)
		    error("was expecting zero!: " + b);
		waiting = false;
	    }
	    delay(1);
	}
    }

    private void readSpectrum()
    {
	boolean waiting = true;
	while (waiting)
	{
	    info("readSpectrum: " + port.available());
	    if (port.available() == Config.SPECTRUM_SIZE)
	    {
		for(int i = 0; i < Config.SPECTRUM_SIZE; i++)
		{
		    spectrum[i] = readByte();
		}
		waiting = false;
	    }
	    delay(1);
	}
    }
    
    private void sendOk()
    {
	info("sendOk", HIGH);
	writeByte((byte) 0);
    }

    private int readByte()
    {
	int v = port.read();
	info("< " + v, HIGH);
	return v;
    }

    private void writeByte(int b)
    {
	info("> " + b, HIGH);
	port.write(b);
    }

    private void setRGB(float r, float g, float b)
    {
	info("setRGB: " + r + " " + g + " " + b, HIGH);
	writeByte((int) (Math.min(r, 0.6) * 255.0));
	writeByte((int) (g * 0.85 * 255.0));
	writeByte((int) (b * 0.5 * 255.0));
    }

    public void setRGB2(int index, double r, double g, double b)
    {
	if (r < 0)
	    r = 0;
	if (r > 1)
	    r = 1;
	if (g < 0)
	    g = 0;
	if (g > 1)
	    g = 1;
	if (b < 0)
	    b = 0;
	if (b > 1)
	    b = 1;
	
	rgb[index * 3 + 0] = (float) r;
	rgb[index * 3 + 1] = (float) g;
	rgb[index * 3 + 2] = (float) b;
    }

    private void delay(int time)
    {
	try
	{
	    Thread.sleep(time);
	} catch (InterruptedException e)
	{
	    e.printStackTrace();
	}
    }

    private void info(String message)
    {
	System.out.println(message);
    }

    private void info(String message, int verbose)
    {
	if (verbose <= VERBOSE)
	    info(message);
    }

    private void error(String message)
    {
	System.err.println(message);
    }

    public void setRGBs(RGB[] rgbs)
    {
	for(int index = 0; index < Config.LEDS; index++)
	{
	    RGB rgb = rgbs[index];
	    setRGB2(index, rgb.r, rgb.g, rgb.b);
	}
    }
}