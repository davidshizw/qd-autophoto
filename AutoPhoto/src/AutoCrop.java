import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class AutoCrop 
{
	private JFrame frame;
	private JPanel background;
	private JPanel dragBox;
	private OFImage image;
	private Student student;
	private String path;
	private ImageViewer parent;
	
	public AutoCrop(OFImage img, Student s, String path, ImageViewer parent)
	{
		frame = new JFrame();
		this.parent = parent;
		image = img;
		student = s;
		this.path = path + File.separatorChar + s.getFilename("PR");
		background = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon ii = new ImageIcon(image);
                g.drawImage(ii.getImage(), 0, 0, getWidth(), getHeight(), ii.getImageObserver());
            }
        };
        background.setLayout(new BorderLayout());
        background.setPreferredSize(new Dimension(540,360));
        addDragBox();
        frame.add(background);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}
	
	public void addDragBox() 
    {  
        JPanel container = new JPanel();
        container.setLayout(null);
        dragBox = new JPanel();  
		dragBox.setOpaque(false);
		dragBox.setBorder(BorderFactory.createLineBorder(Color.RED,2));
		dragBox.setBounds(230, 70, 100, 100);  
		container.add(dragBox);
		container.setOpaque(false);
		background.add(container,BorderLayout.CENTER); 
		JButton btn = new JButton("确定裁剪");
		background.add(btn,BorderLayout.SOUTH);
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					saveImage(cropImage());
					JOptionPane.showMessageDialog(null, "头像截取成功，"+student.getCName()+"同学的拍摄工作已完成。");
					parent.afterCrop();
					frame.dispose();
				}
			});
		Listener m = new Listener();  
		dragBox.addMouseListener(m);  
		dragBox.addMouseMotionListener(m);  
	}  
	
	public BufferedImage cropImage() 
	{
		Rectangle r = dragBox.getBounds();
		BufferedImage buffered = new BufferedImage(540, 360, BufferedImage.TYPE_INT_RGB);
        buffered.getGraphics().drawImage(image, 0, 0 , null);
        BufferedImage cropImage = buffered.getSubimage((int)r.getX()-8,(int)r.getY()-2,100,100);
        return cropImage;
	}
	
	public void saveImage(BufferedImage output)
    {
        try{
        	File f = new File(path);
        	ImageIO.write(output,"JPG",f);
        } catch(Exception e) {
        	
        }
    }
	
	class Listener extends MouseAdapter{  
		int newX,newY,oldX,oldY;  
		int startX,startY;  
  
		@Override  
		public void mousePressed(MouseEvent e) 
		{  
			Component cp = (Component)e.getSource();  
			startX = cp.getX();  
			startY = cp.getY();  
			oldX = e.getXOnScreen();  
			oldY = e.getYOnScreen();  
		}  	
  
  
		@Override  
		public void mouseDragged(MouseEvent e) 
		{  
			Component cp = (Component)e.getSource();  
			newX = e.getXOnScreen(); 
			newY = e.getYOnScreen();  
			cp.setBounds(startX+(newX - oldX), startY+(newY - oldY), cp.getWidth(), cp.getHeight()); 
		}  
	}  
}  

