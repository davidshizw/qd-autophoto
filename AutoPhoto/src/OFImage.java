import java.awt.*;
import java.awt.image.*;

public class OFImage extends BufferedImage{
	public OFImage(BufferedImage image)
	{
		super(image.getColorModel(),image.copyData(null),
				image.isAlphaPremultiplied(),null);
	}
	
	public OFImage(int width, int height)
	{
		super(width,height,TYPE_INT_RGB);
	}
	
	public void setPixel(int x, int y, Color col)
	{
		int pixel = col.getRGB();
		setRGB(x,y,pixel);
	}
	
	public Color getPixel(int x, int y)
	{
		int pixel = getRGB(x,y);
		return new Color(pixel);
	}
}
