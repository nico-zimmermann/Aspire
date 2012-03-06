package sing.ui;

import javax.media.jai.Interpolation;

import sing.Config;
import sing.Main;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ControlWindow;
import controlP5.Toggle;

public class View
{
    private static final String SHAPES = "SHAPES";
    private static final String SOUND = "SOUND";
    private static final String CALIBRATE = "CALIBRATE";

    private static int COMPONENT_ID = 0;

    final Main main;
    ControlP5 cp5;

    public View(Main main)
    {
	this.main = main;
    }

    public void init()
    {
	cp5 = new ControlP5(main);

	ControlWindow cw = cp5.addControlWindow("win", 0, 665, 582, 300);
	cw.frameRate(2);
	cp5.begin(cw);

	cw.getTab("default").setHeight(20);
	cw.addTab(SOUND).setHeight(20);

	createDefaultTab();
	createSoundTab();

	cp5.end();
    }

    public void loadSettings()
    {
	cp5.loadProperties(Config.SERIAL_CONFIG);
    }

    public void saveSettings()
    {
	cp5.saveProperties(Config.SERIAL_CONFIG);
    }

    private void createDefaultTab()
    {
	cp5.addButton("b3")
		.setCaptionLabel("save default")
		.setColorBackground(0xff663333)
		.setPosition(10, 30)
		.setSize(100, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			saveSettings();
		    }
		});

	cp5.addButton("b4")
		.setCaptionLabel("load default")
		.setColorBackground(0xff336633)
		.setPosition(120, 30)
		.setSize(100, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			loadSettings();
		    }
		});
    }

    private String getNextName()
    {
	COMPONENT_ID++;
	return "C_" + COMPONENT_ID;
    }

    private void createSoundTab()
    {
	cp5.addSlider("FFT SCALE")
		.moveTo(SOUND)
		.setMin(0)
		.setMax(100)
		.setValue(20)
		.setHeight(20)
		.setPosition(10, 30)
		.setSize(200, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.fftScale = theEvent.getValue();
		    }
		})
		.update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("AUTO CUT")
		.moveTo(SOUND)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(280, 30)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.autoCutoff = theEvent.getValue() == 1 ? true : false;
		    }
		})
		.update();

	cp5.addSlider(getNextName())
		.setCaptionLabel("CUT")
		.moveTo(SOUND)
		.setMin(0)
		.setMax(1)
		.setValue(0.1f)
		.setHeight(20)
		.setPosition(340, 30)
		.setSize(200, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.cutoff = theEvent.getValue();
		    }
		})
		.update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("CUT SMOOTH")
		.moveTo(SOUND)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(280, 70)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.autoCutoffSmooth = theEvent.getValue() == 1 ? true : false;
		    }
		})
		.update();

	cp5.addSlider(getNextName())
		.setCaptionLabel("SMOOTH")
		.moveTo(SOUND)
		.setMin(1)
		.setMax(50)
		.setValue(10)
		.setHeight(20)
		.setPosition(340, 70)
		.setSize(200, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.autoCutoffSmoothValue = theEvent.getValue();
		    }
		})
		.update();
	cp5.addToggle(getNextName())
		.setCaptionLabel("CUT POW")
		.moveTo(SOUND)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(280, 110)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.autoCutoffModePow = theEvent.getValue() == 1 ? true : false;
		    }
		})
		.update();

	cp5.addSlider(getNextName())
		.setCaptionLabel("POW")
		.moveTo(SOUND)
		.setMin(0.1f)
		.setMax(3)
		.setValue(1.5f)
		.setHeight(20)
		.setPosition(340, 110)
		.setSize(200, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.autoCutoffModePowExponent = theEvent.getValue();
		    }
		})
		.update();

	cp5.addSlider(getNextName())
		.setCaptionLabel("INPUT OFFSET")
		.moveTo(SOUND)
		.setMin(-20)
		.setMax(20)
		.setValue(10)
		.setHeight(20)
		.setPosition(340, 150)
		.setSize(200, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.inputOffset = theEvent.getValue();
		    }
		})
		.update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("FFT EQ")
		.moveTo(SOUND)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(10, 90)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.equalizer = theEvent.getValue() == 1 ? true : false;
		    }
		})
		.update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("FFT SMOOTH")
		.moveTo(SOUND)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(50, 90)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.smoothing = theEvent.getValue() == 1 ? true : false;
		    }
		})
		.update();
    }

    public class ToggleBlock
    {
	private boolean isFree = true;

	public boolean isFree()
	{
	    return isFree;
	}

	public void enable()
	{
	    isFree = false;
	}

	public void disable()
	{
	    isFree = true;
	}
    }
}