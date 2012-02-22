package sing.ui;

import sing.Config;
import sing.Main;
import sing.Model;
import sing.util.Interpolation;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ControlWindow;
import controlP5.Slider;
import controlP5.Textlabel;
import controlP5.Toggle;

public class View
{
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

    private static final String SHAPES = "SHAPES";
    private static final String SOUND = "SOUND";
    private static final String LED = "LED";
    private static final String CALIBRATE = "CALIBRATE";

    private static int COMPONENT_ID = 0;

    public Textlabel fpsLabel;

    final Main main;
    ControlP5 cp5;
    private final Model model;

    public View(Main main, Model model)
    {
	this.main = main;
	this.model = model;
    }

    public void init()
    {
	cp5 = new ControlP5(main);

	ControlWindow cw = cp5.addControlWindow("win", 700, 0, 400, 600);
	cp5.begin(cw);

	cw.getTab("default").setHeight(20);
	cw.addTab(CALIBRATE).setHeight(20);
	cw.addTab(LED).setHeight(20);
	cw.addTab(SOUND).setHeight(20);
	cw.addTab(SHAPES).setHeight(20);

	createDefaultTab();
	createCalibrateTab();
	createLEDTab();
	createSoundTab();
	createShapesTab();

	cp5.end();
    }

    private void createShapesTab()
    {
	cp5.addToggle(getNextName())
	.setCaptionLabel("Two Particles")
	.moveTo(SHAPES)
	.setValue(0)
	.setSize(30, 20)
	.setPosition(10, 30)
	.addListener(new ControlListener() {
	    public void controlEvent(ControlEvent theEvent)
	    {
		model.twoParticles.enabled = theEvent.getValue() == 1 ? true : false;
	    }
	})
	.update();
	cp5.addToggle(getNextName())
	.setCaptionLabel("Two Opposite Particles")
	.moveTo(SHAPES)
	.setValue(0)
	.setSize(30, 20)
	.setPosition(10, 70)
	.addListener(new ControlListener() {
	    public void controlEvent(ControlEvent theEvent)
	    {
		model.twoOppositeParticles.enabled = theEvent.getValue() == 1 ? true : false;
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
			model.calibrate.reference.position.setAzimuth(theEvent.getValue() * Math.PI / 180.0);
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
			model.calibrate.reference.position.setInclination(theEvent.getValue() * Math.PI / 180.0);
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
	cp5.addSlider("damp")
		.moveTo(SOUND)
		.setMin(0)
		.setMax(1)
		.setValue(1.0f)
		.setHeight(20)
		.setPosition(10, 60)
		.setSize(100, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.setDamping(theEvent.getValue());
		    }
		})
		.update();

	cp5.addToggle("EQ")
		.moveTo(SOUND)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(10, 90)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.setEQ(theEvent.getValue() == 1 ? true : false);
		    }
		})
		.update();

	cp5.addToggle("SMOOTH")
		.moveTo(SOUND)
		.setValue(1)
		.setSize(30, 20)
		.setPosition(50, 90)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.setSmoothing(theEvent.getValue() == 1 ? true : false);
		    }
		})
		.update();
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
			cp5.saveProperties("default.ser", "default");
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
			cp5.loadProperties("default.ser");
		    }
		});

	fpsLabel = cp5.addTextlabel("FPS")
		.setPosition(10, 10)
		.setText("HUHU!");
    }

    private void createLEDTab()
    {
	cp5.addToggle("USE")
		.moveTo(LED)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(10, 30)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
		    }
		})
		.update();

	for (int index = 0; index < Config.LEDS; index++)
	    createColorSlider(index);
    }

    private void createColorSlider(int index)
    {
	createChannelSlider(index, 0);
	createChannelSlider(index, 1);
	createChannelSlider(index, 2);

	cp5.addToggle("LED " + index)
		.moveTo(LED)
		.setValue(0)
		.setSize(30, 20)
		.setPosition(200, 70 + index * 50)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
		    }
		})
		.update();

    }

    private void createChannelSlider(int index, int channel)
    {
	Slider slider = cp5.addSlider("LED " + index + " - CH " + channel)
		.moveTo(LED)
		.setMin(0)
		.setMax(1)
		.setValue(0.0f)
		.setHeight(20)
		.setPosition(10, 70 + channel * 12 + index * 50)
		.setSize(100, 10)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			//
		    }
		});
	switch (channel)
	{
	    case 0:
		slider.setColorBackground(0xff993333);
		slider.setColorForeground(0xffff6666);
		slider.setColorActive(0xffff9999);
		break;
	    case 1:
		slider.setColorBackground(0xff339933);
		slider.setColorForeground(0xff66cc66);
		slider.setColorActive(0xff99cc99);
		break;
	}
    }
}
