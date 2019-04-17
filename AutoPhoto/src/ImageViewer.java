import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;


public class ImageViewer 
{
	private JFrame frame;
	private ImagePanel imagePanel1;
	private ImagePanel imagePanel2;
	private JLabel filenameLabel1;
	private JLabel filenameLabel2;
	private JLabel statusLabel;
	private JButton importButton;
	private JButton deleteButton;
	private JButton giveupButton;
	private JButton finishButton;
	private JButton autoScanButton;
	
	private OFImage currentImage1;
	private OFImage currentImage2;
	private ControlPanel parent;
	private Student currentStudent;
	private File[] imageArr;
	private int deletePhoto;
	private int photoNum;
	
	private boolean flag;
	private boolean autoScan = false; 
	
	public ImageViewer(ControlPanel parent)
	{
		this.parent = parent;
		currentStudent = parent.getCurrentStudent();
		imageArr = new File[2];
		currentImage1 = null;
		currentImage2 = null;
		deletePhoto = 0;
		photoNum = 0;
		makeFrame();
	}
	
	private void showFilename(String filename1, String filename2)
	{
		if(filename1 != null) {
			if(filename1.equals("error")) {
				filenameLabel1.setText("目标文件夹中发现大于两张未命名照片，请检查");
				filenameLabel2.setText(" ");
				return;
			}
		}
		if(filename1 == null) {
			filenameLabel1.setText("File 1: No File Displayed");
		} else if(!filename1.equals("no change")){
			filenameLabel1.setText("File 1: " + filename1);
		}
		if(filename2 == null) {
			filenameLabel2.setText("File 2: No File Displayed");
		} else if(!filename2.equals("no change")){
			filenameLabel2.setText("File 2: " + filename2);
		}
	}
	
	public void makeFrame()
	{
		frame = new JFrame("Photo Processing System - " + currentStudent.getCName());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				parent.startThread();
				frame.dispose();
			}
		});
		JPanel contentPane = (JPanel) frame.getContentPane();
		contentPane.setBorder(new EmptyBorder(12,12,12,12));
		
		contentPane.setLayout(new BorderLayout(6,6));
		
		imagePanel1 = new ImagePanel();
		imagePanel1.setBorder(new EtchedBorder());
		contentPane.add(imagePanel1, BorderLayout.CENTER);
		
		imagePanel2 = new ImagePanel();
		imagePanel2.setBorder(new EtchedBorder());
		contentPane.add(imagePanel2, BorderLayout.EAST);
		
		JPanel filenames = new JPanel();
		filenames.setLayout(new GridLayout(2,0));
		filenameLabel1 = new JLabel();
		filenameLabel2 = new JLabel();
		filenames.add(filenameLabel1);
		filenames.add(filenameLabel2);
		contentPane.add(filenames, BorderLayout.NORTH);
		
		statusLabel = new JLabel("You are working on the Photo ID of " + currentStudent.getEName() + " " + currentStudent.getCName());
		contentPane.add(statusLabel, BorderLayout.SOUTH);
		
		JPanel toolbar = new JPanel();
		toolbar.setLayout(new GridLayout(0,1,0,10));
		
		importButton = new JButton("导入照片");
			importButton.addActionListener(e -> importPhoto());
		toolbar.add(importButton);
		
		deleteButton = new JButton("删除照片");
			deleteButton.addActionListener(e -> deletePhoto());
		toolbar.add(deleteButton);
		
		finishButton = new JButton("完成拍摄");
			finishButton.addActionListener(e -> finishPhoto());
		toolbar.add(finishButton);
		
		giveupButton = new JButton("放弃拍摄");
			giveupButton.addActionListener(new ActionListener() 
				{
					public void actionPerformed(ActionEvent e)
					{
						parent.startThread();
						frame.dispose();
					}
				});
		toolbar.add(giveupButton);
		
		autoScanButton = new JButton("自动扫描");
			autoScanButton.setBackground(Color.RED);
			autoScanButton.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e)
				{
					if(autoScanButton.getBackground().equals(Color.RED)) {
						importButton.setEnabled(false);
						autoScanButton.setBackground(Color.GREEN);
						autoScan = true;
						autoScan();
					} else {
						importButton.setEnabled(true);
						autoScanButton.setBackground(Color.RED);
						autoScan = false;
					}
				}
			});
		toolbar.add(autoScanButton);
		
		JPanel flow = new JPanel();
		flow.add(toolbar);
		
		contentPane.add(flow,BorderLayout.WEST);
		
		showFilename(null,null);
		deleteButton.setEnabled(false);
		finishButton.setEnabled(false);
		frame.pack();
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
		frame.setVisible(true);
	}
	
	public OFImage cropPhoto(OFImage image)
	{
		int factor = image.getWidth()/540;
		OFImage output = new OFImage(540,360);
		for(int y = 0; y < 360; y++) {
			for(int x = 0; x < 540; x++) {
				output.setPixel(x, y, image.getPixel(x*factor, y*factor));
			}
		}
		return output;
	}

	public void finishPhoto()
	{
		File f1 = imageArr[0];
		File f2 = imageArr[1];
		File s1 = new File(parent.getDatapath().getPhotoPath(currentStudent.getHomeroom()) + File.separatorChar + currentStudent.getFilename("NM"));
		File s2 = new File(parent.getDatapath().getPhotoPath(currentStudent.getHomeroom()) + File.separatorChar + currentStudent.getFilename("HY"));
		String[] options = new String[] {"左头像/右欢乐","左欢乐/右头像"};
		int response = JOptionPane.showOptionDialog(null, "请选择重命名规则", "重命名确认", 
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if(response == 0) {
			if(f1.renameTo(s1) && f2.renameTo(s2)) {
				JOptionPane.showMessageDialog(null, "重命名成功，单击确认以截取学生头像照片。");
				new AutoCrop(currentImage2,currentStudent,parent.getDatapath().getPhotoPath(currentStudent.getHomeroom()),this);
				
				
			} else {
				JOptionPane.showMessageDialog(null, "重命名发生未知错误，请联系开发者。", "错误",JOptionPane.ERROR_MESSAGE);  
			}
		} else if(response == 1){
			if(f1.renameTo(s2) && f2.renameTo(s1)) {
				JOptionPane.showMessageDialog(null, "重命名成功，单击确认以截取学生头像照片。");
				new AutoCrop(currentImage1,currentStudent,parent.getDatapath().getPhotoPath(currentStudent.getHomeroom()),this);
			} else {
				JOptionPane.showMessageDialog(null, "重命名发生未知错误，请联系开发者。", "错误",JOptionPane.ERROR_MESSAGE);  
			}
		}
	}
	
	public void afterCrop()
	{
		currentStudent.setStatus(true);
		parent.getStatusList(currentStudent.getHomeroom()).update(currentStudent);
		parent.startThread();
		frame.dispose();
	}
	
	public void autoScan()
	{
		Thread t = new Thread(new Runnable() {
			public void run() {
				flag = false;
				while(!flag) {
					try {
						Thread.sleep(1000);
						importPhoto();
					} catch(Exception e){
						
					}
				}
			}
		});
		t.start();		
	}
	
	public void deletePhoto()
	{
		if(photoNum == 1) {
			if(imagePanel1.getPanelImage() == null) {
				imagePanel2.clearImage();
				File f = imageArr[1];
				if(f.delete()) {
					statusLabel.setText("当前状态：删除成功(01)");
				}
				imageArr[1] = null;
			} else {
				imagePanel1.clearImage();
				File f = imageArr[0];
				if(f.delete()) {
					statusLabel.setText("当前状态：删除成功(02)");
				}
				imageArr[0] = null;
			}
			showFilename(null,null);
			photoNum = 0;
			deletePhoto = 0;
			frame.pack();
			frame.repaint();
		} else{
			String[] options = new String[] {"删除左侧照片","删除右侧照片","删除所有照片"};
			int response = JOptionPane.showOptionDialog(null, "请选择需要删除的照片", "删除确认", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
			if(response == 0) {
				if(currentImage1 == imagePanel1.getPanelImage()) {
					currentImage1 = null;
				} else {
					currentImage2 = null;
				}
				imagePanel1.clearImage();
				photoNum = 1;
				deletePhoto = 1;
				finishButton.setEnabled(false);
				showFilename(null,"no change");
				File f = imageArr[0];
				if(f.delete()) {
					statusLabel.setText("当前状态：删除成功(03)");
				}
				imageArr[0] = null;
			} else if(response == 1) {
				if(currentImage1 == imagePanel2.getPanelImage()) {
					currentImage1 = null;
				} else {
					currentImage2 = null;
				}
				imagePanel2.clearImage();
				photoNum = 1;
				deletePhoto = 2;
				finishButton.setEnabled(false);
				showFilename("no change",null);
				File f = imageArr[1];
				if(f.delete()) {
					statusLabel.setText("当前状态：删除成功(04)");
				}
				imageArr[1] = null;
				
			} else if(response == 2) {
				currentImage1 = null;
				currentImage2 = null;
				imagePanel1.clearImage();
				imagePanel2.clearImage();
				photoNum = 0;
				deletePhoto = 0;
				deleteButton.setEnabled(false);
				finishButton.setEnabled(false);
				showFilename(null,null);
				File f1 = imageArr[1];
				File f2 = imageArr[0];
				if(f1.delete() && f2.delete()) {
					statusLabel.setText("当前状态：删除成功(05)");
				}
				imageArr[0] = null;
				imageArr[1] = null;
			}
		}
		autoScanButton.setEnabled(true);
		if(autoScan) {
			autoScanButton.setBackground(Color.GREEN);
			autoScan();
		} else {
			importButton.setEnabled(true);
		}
	}
	
	public void importPhoto()
	{
		String path = parent.getDatapath().getPhotoPath(currentStudent.getHomeroom());
		File dir = new File(path);
		File[] fileList = dir.listFiles();
		int index = 0;
		for(File f: fileList) {
			if(f.getName().contains("IMG")) {
				if(index > 1 || (deletePhoto != 0 && index > 1)) {
					showFilename("error", null);
					return;
				} else if(deletePhoto == 0) {
					imageArr[index] = f;
					index++;
				} else if(deletePhoto == 1) {
					if(!imageArr[1].getPath().equals(f.getPath())) {
						imageArr[0] = f;
						index++;
					}
				} else {
					if(!imageArr[0].getPath().equals(f.getPath())) {
						imageArr[1] = f;
						index++;
					}
				}
			}
		}
		
		if((imageArr[0] == null && imageArr[1] == null) || (deletePhoto != 0 && index == 0)) {
			return;
		} else if(imageArr[1] == null || deletePhoto == 1) {
			currentImage1 = cropPhoto(ImageFileManager.loadImage(imageArr[0]));
			imagePanel1.setImage(currentImage1);
			
			deleteButton.setEnabled(true);
			if(deletePhoto == 1) {
				showFilename(imageArr[0].getPath(),imageArr[1].getPath());
				finishButton.setEnabled(true);
				deletePhoto = 0;
				photoNum = 2;
			} else {
				showFilename(imageArr[0].getPath(),null);
				photoNum = 1;
			}
		} else if(deletePhoto == 2){
			currentImage2 = cropPhoto(ImageFileManager.loadImage(imageArr[1]));
			imagePanel2.setImage(currentImage2);
			deleteButton.setEnabled(true);
			showFilename(imageArr[0].getPath(),imageArr[1].getPath());
			finishButton.setEnabled(true);
			deletePhoto = 0;
			photoNum = 2;
		} else {
			currentImage1 = cropPhoto(ImageFileManager.loadImage(imageArr[0]));
			imagePanel1.setImage(currentImage1);
			currentImage2 = cropPhoto(ImageFileManager.loadImage(imageArr[1]));
			imagePanel2.setImage(currentImage2);
			showFilename(imageArr[0].getPath(),imageArr[1].getPath());
			deleteButton.setEnabled(true);
			finishButton.setEnabled(true);
			importButton.setEnabled(false);
			photoNum = 2;
			flag = true;
			if(autoScan) {
				autoScanButton.setBackground(Color.YELLOW);
				autoScanButton.setEnabled(false);
			}
			statusLabel.setText("当前状态：导入成功");
		}
		statusLabel.setText("当前状态：导入成功");
		frame.pack();
		frame.repaint();
	}
}
