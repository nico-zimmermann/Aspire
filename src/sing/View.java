package sing;

import controlP5.CallbackEvent;
import controlP5.CallbackListener;
import controlP5.ControlEvent;
import controlP5.ControlListener;
import controlP5.ControlP5;
import controlP5.ControlWindow;

public class View
{
    final Main main;
    ControlP5 cp5;

    public View(Main main)
    {
	this.main = main;
    }

    public void init()
    {
	cp5 = new ControlP5(main);

	ControlWindow cw = cp5.addControlWindow("win", 700, 0, 400, 250);
	cp5.begin(cw);

	cp5.addButton("b3")
		.setCaptionLabel("save default")
		.setColorBackground(0xff663333)
		.setPosition(10, 10)
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
		.setPosition(120, 10)
		.setSize(100, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			cp5.loadProperties("default.ser");
		    }
		});

	cp5.addSlider("damp")
		.setMin(0)
		.setMax(1)
		.setValue(1.0f)
		.setHeight(20)
		.setPosition(10, 40)
		.setSize(100, 20)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.setDamping(theEvent.getValue());
		    }
		})
		.update();

	cp5.addToggle("EQ")
		.setValue(0)
		.setSize(30, 20)
		.setPosition(10, 70)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.setEQ(theEvent.getValue() == 1 ? true : false);
		    }
		})
		.update();

	cp5.addToggle("SMOOTH")
		.setValue(1)
		.setSize(30, 20)
		.setPosition(50, 70)
		.addListener(new ControlListener() {
		    public void controlEvent(ControlEvent theEvent)
		    {
			main.setSmoothing(theEvent.getValue() == 1 ? true : false);
		    }
		})
		.update();

	cp5.end();
    }
}
