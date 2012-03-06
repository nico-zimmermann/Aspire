package sing.remote;

import processing.serial.Serial;
import sing.Config;
import sing.Main;

public class PortWaveform extends Thread
{
    private static final int ON = 1;
    private static final int LOW = 2;
    private static final int HIGH = 3;
    private static final int VERBOSE = LOW;

    private Serial port;
    private Main main;

    public int lastLoopDuration;
    public int[] waveform = new int[Config.WAVEFORM_SIZE];

    int spectrumIndex = 0;
    String portName;

    public PortWaveform(Main main, String portName)
    {
	this.main = main;
	this.portName = portName;
    }

    public void createPort()
    {
	try
	{
	    port = new Serial(main, portName, 115200);
	    if (Config.USE_WAVEFROM_FAKE)
		new FakeSerialWaveform(port);
	    start();
	} catch (Exception e)
	{
	    System.err.println(e.getMessage());
	    System.exit(1);
	}
    }

    public void exit()
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
	readWaveform();
	main.handleWaveform();
    }

    private void readWaveform()
    {
	boolean waiting = true;
	while (waiting)
	{
	    info("readWaveform: " + port.available(), HIGH);
	    if (port.available() >= Config.WAVEFORM_SIZE)
	    {
		for (int i = 0; i < Config.WAVEFORM_SIZE; i++)
		{
		    waveform[i] = readByte();
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