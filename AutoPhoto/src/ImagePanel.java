import java.awt.*;
import javax.swing.*;

public class ImagePanel extends JComponent{

	private int width;
	private int height;
	private OFImage panelImage;
	
	public ImagePanel()
	{
		width = 540;
		height = 360;
		panelImage = null;
	}
	
	public void setImage(OFImage image)
	{
		if(image != null) {
			width = image.getWidth();
			height = image.getHeight();
			panelImage = image;
			repaint();
		}
	}
	
	public OFImage getPanelImage()
	{
		return panelImage;
	}
	
	public void clearImage()
	{
		Graphics imageGraphics = panelImage.getGraphics();
		imageGraphics.setColor(Color.LIGHT_GRAY);
		imageGraphics.fillRect(0, 0, width, height);
		panelImage = null;
		repaint();
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(width, height);
	}
	
	public void paintComponent(Graphics g)
	{
		Dimension size = getSize();
		g.clearRect(0, 0, size.width, size.height);
		if(panelImage != null) {
			g.drawImage(panelImage, 0, 0, null);
		}
	}
}
