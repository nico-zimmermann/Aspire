package sing;

import java.util.ArrayList;

import processing.serial.Serial;

public class PortHandler2 extends Thread
{
    private static final int OFF = 1;
    private static final int LOW = 2;
    private static final int HIGH = 3;
    private static final int VERBOSE = LOW;

    public int lastLoopDuration;
    
    private Serial port;
    private Main main;

    public PortHandler2(Main main)
    {
	this.main = main;
    }

    void createPort()
    {
	try
	{
	    main.println(Serial.list());
	    port = new Serial(main, Serial.list()[0], 115200);
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

	//sendOk();
	//waitOk();
	sendRGB();
	waitOk();

	lastLoopDuration = main.millis() - start;
    }

    private void sendRGB()
    {
	for (int i = 0; i < 10; i++)
	{
	    float r = main.sin(main.millis() / 200.0f + i * 1.0f) * 0.5f + 0.5f;
	    float g = main.sin(main.millis() / 800.0f + i * 1.0f) * 0.5f + 0.5f;
	    float b = main.sin(main.millis() / 300.0f + i * 1.0f) * 0.5f + 0.5f;
	    setRGB(r, g, b);
	}
    }

    private void waitOk()
    {
	boolean waiting = true;
	while (waiting)
	{
	    if (port.available() == 1)
	    {
		if (readByte() != 0)
		    error("was expecting zero!");
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

    public void setRGB(float r, float g, float b)
    {
	info("setRGB", HIGH);
	writeByte((int) (r * 255.0));
	writeByte((int) (g * 255.0));
	writeByte((int) (b * 255.0));
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