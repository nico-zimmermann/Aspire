package sing;

public class Config
{
    public static final int LEDS = 10;
    public static final int SAMPLING_RATE = 44100;
    public static final int WAVEFORM_SIZE = (int) Math.pow(2, 12);
    public static final int SAMPLE_SIZE = (int) Math.pow(2, 8);
    public static final int SPECTRUM_SIZE = WAVEFORM_SIZE / 2;
    public static final int BANDS_NUM = 75;

    public static final int COLOR_BRIGHT = 0xff20c2ff;
    public static final int COLOR_MEDIUM = 0xff00497c;
    public static final int COLOR_DARK = 0xff003652;

    public static final boolean USE_SERIAL_AUDIO = false;
    public static final boolean USE_WAVEFROM_FAKE = true;

    public static final boolean USE_LIGHTS_FAKE = true;
    public static final String SERIAL_CONFIG = "../default.ser";
}
