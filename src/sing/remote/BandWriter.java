package sing.remote;

import java.io.File;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;

import sing.model.Analyzer;
import sing.model.Band;

public class BandWriter
{
    public Analyzer analyzer;

    private JsonGenerator g;

    public void prepare()
    {
	try
	{
	    JsonFactory f = new JsonFactory();
	    g = f.createJsonGenerator(new File("../bands.json"), JsonEncoding.UTF8);

	    g.writeStartObject();
	    g.writeObjectFieldStart("meta");
	    g.writeStringField("song", "SomeSongName");
	    g.writeEndObject();
	    g.writeArrayFieldStart("bands");

	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void writeBands()
    {
	try
	{
	    g.writeRaw("\n");
	    g.writeStartArray();
	    for (Band band : analyzer.bands)
	    {
		g.writeNumber(Math.round(band.value * 1000.0) / 1000.0);
	    }
	    g.writeEndArray();

	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public void close()
    {
	System.out.println("close");
	try
	{
	    g.writeRaw("\n");
	    g.writeEndArray();
	    g.writeEndObject();
	    g.close();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
