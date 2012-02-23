package sing;

import java.util.ArrayList;

import processing.serial.Serial;
import sing.model.RGB;

public class PortSpectrum extends Thread
{
    private static final int ON = 1;
    private static final int LOW = 2;
    private static final int HIGH = 3;
    private static final int VERBOSE = LOW;

    private Serial port;
    private Main main;
    
    public int lastLoopDuration;
    public int[] spectrum = new int[Config.SPECTRUM_SIZE];
    
    int spectrumIndex = 0;
    String portName;

    public PortSpectrum(Main main, String portName)
    {
	this.main = main;
	this.portName = portName;
    }

    void createPort()
    {
	try
	{
	    port = new Serial(main, portName, 115200);
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

	readSpectrum();

	lastLoopDuration = main.millis() - start;
    }

    private void readSpectrum()
    {
	boolean waiting = true;
	while (waiting)
	{
	    info("readSpectrum: " + port.available(), HIGH);
	    if (port.available() >= Config.SPECTRUM_SIZE)
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
    
    private int readByte()
    {
	int v = port.read();
	info("< " + v, HIGH);
	return v;
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
}