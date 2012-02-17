package sing;

import java.util.ArrayList;

import processing.serial.Serial;

public class PortHandler
{
    final static int HANDSHAKE = 0;
    final static int IDLE = 1;
    final static int COMMAND = 2;
    final static int DATA = 3;

    public Serial port;
    public int portState;
    public int currentCommand;
    public int expectedBytes;
    public boolean needsHandShake;
    public boolean stopped;
    public int frame;
    public Main main;

    public PortHandler(Main main)
    {
	this.main = main;
	portState = HANDSHAKE;
	expectedBytes = 1;
	needsHandShake = true;
	stopped = false;
	frame = 0;
    }

    void createPort()
    {
	port = new Serial(main, Serial.list()[1], 57600 * 2);
	Main.println("port.available() " + port.available());
    }

    void stop()
    {
	port.stop();
	stopped = true;
    }

    public void run()
    {
	if (stopped)
	    return;

	frame++;

	if (port == null)
	    return;
	if (portState == HANDSHAKE && frame < 10)
	{
	    main.background(0, 255, 0);
	    while (port.available() > 0)
		port.read();
	    return;
	} else if (portState == HANDSHAKE)
	{
	    portState = IDLE;
	}

	if (portState == IDLE)
	{
	    port.write(1);
	    port.write(0);
	    port.write(0);
	    port.write(1);
	}

	port.write(2);
	port.write(9);
	port.write((byte) ((Math.sin(main.millis() / 1000.0) + 1) * 127));
	port.write(2);

	while (port.available() >= expectedBytes)
	{
	    switch (portState)
	    {
		case IDLE:
		    currentCommand = port.read();
		    // println("currentCommand:" + currentCommand);
		    portState = COMMAND;
		    expectedBytes = 2;
		    break;
		case COMMAND:
		    expectedBytes = port.read() + (port.read() << 8);
		    // println("expectedBytes: " + expectedBytes);
		    portState = DATA;
		    break;
		case DATA:
		    // println("data");

		    byte[] bytes = new byte[expectedBytes];
		    port.readBytes(bytes);
		    main.handleCommand(0, bytes);
		    portState = IDLE;
		    expectedBytes = 1;
	    }
	}
    }
}