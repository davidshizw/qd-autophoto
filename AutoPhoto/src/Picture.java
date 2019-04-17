// designed for the photos of size 5472*3648

import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;

public class Picture 
{
    private static String path = "";
    private static ImageIcon icon = null;
    
    public static void setPath(String route)
    {
        path = route;
        icon = new ImageIcon(path);
    }

    public static BufferedImage cropPicture(double FACTOR, int x, int y, int w, int h) throws IOException
    {  
        try{
            BufferedImage img = ImageIO.read(new File(path));
            int scaleX = (int) (img.getWidth() * FACTOR);
            int scaleY = (int) (img.getHeight() * FACTOR);
            Image image = img.getScaledInstance(scaleX, scaleY, Image.SCALE_SMOOTH);
            BufferedImage buffered = new BufferedImage(scaleX, scaleY, BufferedImage.TYPE_INT_RGB);
            buffered.getGraphics().drawImage(image, 0, 0 , null);
            BufferedImage cropPicture = buffered.getSubimage(x,y,w,h);
            return cropPicture;
        } catch(IOException e){
            System.out.println("Invalid path: file not found");
            return null;
        }
    }
    
    public static void showPicture(double FACTOR, int x, int y, int w, int h) throws IOException
    {
          JFrame frame = new JFrame();
          BufferedImage output = cropPicture(FACTOR,x,y,w,h);
          ImageIcon finalIcon = new ImageIcon(output);
          if(finalIcon == null){
              return;
          }
          JLabel label = new JLabel(finalIcon);
          frame.add(label);
          frame.setDefaultCloseOperation
                 (JFrame.EXIT_ON_CLOSE);
          frame.pack();
          frame.setVisible(true);
    }
    
    public static void preview(String path, double FACTOR, int x, int y, int w, int h) throws IOException
    {
        setPath(path);
        showPicture(FACTOR,x,y,w,h);
        System.out.println("Successful\n");
    }
    
    public static void savePicture(String path, String file_path, double FACTOR, int x, int y, int w, int h) throws IOException
    {
        setPath(path);
        BufferedImage output = cropPicture(FACTOR,x,y,w,h);
        File f = new File(file_path);
        ImageIO.write(output,"JPG",f);
    }
}