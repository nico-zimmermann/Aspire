package sing.ui;

import sing.Config;
import sing.Main;
import sing.model.Programms;
import sing.program.Interpolation;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ControlWindow;
import controlP5.Slider;
import controlP5.Textlabel;
import controlP5.Toggle;

public class View
{
    private static final String SHAPES = "SHAPES";
    private static final String SOUND = "SOUND";
    private static final String CALIBRATE = "CALIBRATE";

    private static int COMPONENT_ID = 0;

    final Main main;
    ControlP5 cp5;
    private final Programms model;

    public View(Main main, Programms model)
    {
	this.main = main;
	this.model = model;
    }

    public void init()
    {
	cp5 = new ControlP5(main);

	ControlWindow cw = cp5.addControlWindow("win", 0, 665, 582, 300);
	cw.frameRate(2);
	cp5.begin(cw);

	cw.getTab("default").setHeight(20);
	cw.addTab(SOUND).setHeight(20);
	cw.addTab(CALIBRATE).setHeight(20);
	cw.addTab(SHAPES).setHeight(20);

	createDefaultTab();
	createSoundTab();
	createCalibrateTab();
	createShapesTab();

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

	cp5.addSlider(getNextName())
		.setCaptionLabel("GLOBAL ALPHA")
		.setMin(0.01f)
		.setMax(5.0f)
		.setValue(1.0f)
		.setHeight(20)
		.setPosition(10, 60)
		.setSize(300, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.globalAlpha = theEvent.getValue();
		    }
		})
		.update();

	cp5.addSlider(getNextName())
		.setCaptionLabel("GLOBAL POW")
		.setMin(0.01f)
		.setMax(20.0f)
		.setValue(1.0f)
		.setHeight(20)
		.setPosition(10, 90)
		.setSize(300, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.globalPow = theEvent.getValue();
		    }
		})
		.update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("HIDE VIEW")
		.setValue(1)
		.setSize(40, 20)
		.setPosition(10, 130)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.hideView = theEvent.getValue() == 1 ? true : false;
		    }
		}).update();
    }

    private void createShapesTab()
    {
	cp5.addToggle(getNextName())
		.setCaptionLabel("SINGLE SMOOTH")
		.moveTo(SHAPES)
		.setValue(1)
		.setSize(30, 20)
		.setPosition(10, 30)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.singleSmoothParticle.enabled = theEvent.getValue() == 1 ? true : false;
		    }
		}).update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("Two Opposite Particles")
		.moveTo(SHAPES)
		.setValue(1)
		.setSize(30, 20)
		.setPosition(10, 70)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.twoOppositeParticles.enabled = theEvent.getValue() == 1 ? true : false;
		    }
		}).update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("VLines")
		.moveTo(SHAPES)
		.setValue(1)
		.setSize(30, 20)
		.setPosition(10, 110)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.vlines.enabled = theEvent.getValue() == 1 ? true : false;
		    }
		}).update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("Flash")
		.moveTo(SHAPES)
		.setValue(1)
		.setSize(30, 20)
		.setPosition(10, 150)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.flash.enabled = theEvent.getValue() == 1 ? true : false;
		    }
		}).update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("Bands")
		.moveTo(SHAPES)
		.setValue(1)
		.setSize(30, 20)
		.setPosition(10, 190)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.bands.enabled = theEvent.getValue() == 1 ? true : false;
		    }
		}).update();

	cp5.addToggle(getNextName())
		.setCaptionLabel("Flash2")
		.moveTo(SHAPES)
		.setValue(1)
		.setSize(30, 20)
		.setPosition(10, 230)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.flash2.enabled = theEvent.getValue() == 1 ? true : false;
		    }
		})
		.update();

    }

    private void createCalibrateTab()
    {
	cp5.addToggle(getNextName())
		.setCaptionLabel("ENABLED")
		.moveTo(CALIBRATE)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(10, 30)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.calibrate.enabled = theEvent.getValue() == 1 ? true : false;
		    }
		})
		.update();

	final Toggle pow = cp5.addToggle(getNextName());
	final Toggle linear = cp5.addToggle(getNextName());
	final ToggleBlock powBlock = new ToggleBlock();
	final ToggleBlock linearBlock = new ToggleBlock();

	pow.setCaptionLabel("SQRT")
		.moveTo(CALIBRATE)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(60, 30)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			if (powBlock.isFree())
			{
			    linearBlock.enable();
			    model.calibrate.mode = Interpolation.POW;
			    linear.setValue(false);
			    linearBlock.disable();
			}
		    }
		});

	linear.setCaptionLabel("LINEAR")
		.moveTo(CALIBRATE)
		.setValue(1)
		.setSize(30, 20)
		.setPosition(95, 30)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			if (linearBlock.isFree())
			{
			    powBlock.enable();
			    model.calibrate.mode = Interpolation.LINEAR;
			    pow.setValue(false);
			    powBlock.disable();
			}
		    }
		});

	cp5.addSlider(getNextName())
		.setCaptionLabel("AZIMUTH")
		.moveTo(CALIBRATE)
		.setMin(-180)
		.setMax(180)
		.setValue(0.0f)
		.setHeight(20)
		.setPosition(10, 70)
		.setSize(300, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.calibrate.reference.positionS.azimuth = theEvent.getValue() * Math.PI / 180.0;
		    }
		})
		.update();

	cp5.addSlider(getNextName())
		.setCaptionLabel("INCLINATION")
		.moveTo(CALIBRATE)
		.setMin(0)
		.setMax(180)
		.setValue(90.0f)
		.setHeight(20)
		.setPosition(10, 100)
		.setSize(300, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.calibrate.reference.positionS.inclination = theEvent.getValue() * Math.PI / 180.0;
		    }
		})
		.update();

	cp5.addSlider(getNextName())
		.setCaptionLabel("RADIUS")
		.moveTo(CALIBRATE)
		.setMin(0.0f)
		.setMax(2.0f)
		.setValue(0.6f)
		.setHeight(20)
		.setPosition(10, 130)
		.setSize(300, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.calibrate.reference.radius = theEvent.getValue();
		    }
		})
		.update();
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

	cp5.addSlider(getNextName())
		.setCaptionLabel("FFT DAMP")
		.moveTo(SOUND)
		.setMin(0)
		.setMax(1)
		.setValue(1.0f)
		.setHeight(20)
		.setPosition(10, 60)
		.setSize(200, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			// main.setDamping(theEvent.getValue());
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
			// main.setEQ(theEvent.getValue() == 1 ? true : false);
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
			// main.setSmoothing(theEvent.getValue() == 1 ? true :
			// false);
		    }
		})
		.update();

	cp5.addToggle(getNextName())
		.moveTo(SOUND)
		.setCaptionLabel("show bands")
		.setPosition(10, 130)
		.setSize(50, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			model.showBands = theEvent.getValue() == 1 ? true : false;
		    }
		});

	cp5.addSlider("offset")
		.moveTo(SOUND)
		.setMin(-0.5f)
		.setMax(0.5f)
		.setValue(-0.05f)
		.setHeight(20)
		.setPosition(10, 170)
		.setSize(200, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.offset = theEvent.getValue();
		    }
		})
		.update();

	cp5.addSlider("mul")
		.moveTo(SOUND)
		.setMin(0)
		.setMax(10)
		.setValue(1.00f)
		.setHeight(20)
		.setPosition(10, 210)
		.setSize(200, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.analyzer.mul = theEvent.getValue();
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