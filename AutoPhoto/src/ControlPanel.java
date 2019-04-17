import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;


public class ControlPanel {
	private static JFrame frame = new JFrame();
	private static JPanel waitPanel = new JPanel();
	private static JPanel workPanel = new JPanel();
	private static JPanel welcomePanel = new JPanel();
	private static JLabel status = new JLabel("输入学号以查找相应学生");
	private static JLabel studentInfo = new JLabel(" ");
	
	private static ArrayList<Student> currentStudentList = null;	
	private static Homeroom currentHomeroom = null;
	private static Student currentStudent = null;
	private static StatusList[] statusListArr = null;
	
	private static Datapath datapath;
	private static final Object object = new Object();
	
	private JMenuItem newItem;
	private JMenuItem loadItem;
	private JMenu homeroomMenu;
	private JMenu statusMenu;
	
	public ControlPanel() 
	{
		initialize();
		makeMenuBar();
		frame.add(welcomePanel);
		frame.setSize(250,250);
		frame.setResizable(false);
		frame.setTitle("Control Panel");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				quit();
			}
		});
		JPanel temp = (JPanel) frame.getContentPane();
		temp.setBorder(new EmptyBorder(10,15,10,15));
		frame.setVisible(true);
	}
	
	public void initialize()
	{
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		workPanel.setLayout(gridBagLayout);
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(status,gridBagConstraints);
		workPanel.add(status);
		
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(studentInfo,gridBagConstraints);
		workPanel.add(studentInfo);
		
		JTextField textField = new JTextField(9);
			textField.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e)
			    {
					if ((e.getKeyChar() >= e.VK_0 && e.getKeyChar() <= e.VK_9) 
						|| e.getKeyChar() == e.VK_ENTER || e.getKeyChar() == e.VK_TAB
						|| e.getKeyChar() == e.VK_BACK_SPACE || e.getKeyChar() == e.VK_DELETE 
						|| e.getKeyChar() == e.VK_LEFT || e.getKeyChar() == e.VK_RIGHT 
						|| e.getKeyChar() == e.VK_ESCAPE) 
						return;   
					e.consume();
			    }
			});
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(textField, gridBagConstraints);
		workPanel.add(textField);
		
		JButton btn1 = new JButton("查询");
			btn1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					if(currentHomeroom == null) {
						status.setText("请先选择行政班以继续操作");
						return;
					}
					if(textField.getText().equals("")) {
						return;
					} else {
						int ID = Integer.parseInt(textField.getText());
						for(Student s: currentStudentList) {
							if(s.getID() == ID) {
								setStatus(s);
								return;
							}
						}
						studentInfo.setText("数据库中查无此人");
					}
					
				}
			});
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(btn1, gridBagConstraints);
		workPanel.add(btn1);
		
		JButton btn2 = new JButton("下一位");
			btn2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					if(currentHomeroom == null) {
						status.setText("请先选择行政班以继续操作");
						return;
					}
					if(currentStudent == null || currentHomeroom != currentStudent.getHomeroom()) {
						boolean stop = false;
						int index = 0;
						do {
							if(currentStudentList.get(index).getStatus() == false) {
								stop = true;
								currentStudent = currentStudentList.get(index);
								setStatus(currentStudent);
								return;
							} else {
								index++;
								if(index == currentStudentList.size()) {
									stop = true;
								}
							}
						} while(!stop);
						status.setText("恭喜！");
						studentInfo.setText("该行政班已完成拍摄工作。");
					} else{
						int index = 0;
						for(Student s: currentStudentList) {
							if(s == currentStudent) {
								if(s != currentStudentList.get(currentStudentList.size()-1)) {
									currentStudent = currentStudentList.get(index+1);
									if(!currentStudent.getStatus()) {
										setStatus(currentStudent);
									} else {
										actionPerformed(e);
									}
								} else {
									currentStudent = null;
									actionPerformed(e);
								}
								return;
							} else {
								index++;
							}
						}
					}
				}
			});
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(btn2, gridBagConstraints);
		workPanel.add(btn2);
		
		JButton btn3 = new JButton("确认");
			btn3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					if(currentStudent != null) {
						frame.remove(workPanel);
						frame.add(waitPanel);
						frame.validate();
						frame.repaint();
						photoProcessing();
						Thread t = new Thread(new Runnable(){
							public void run()
							{
								synchronized(object) {
									try {
										object.wait();
										frame.remove(waitPanel);
										frame.add(workPanel);
										frame.validate();
										frame.repaint();
										setStatus(null);
									} catch(InterruptedException e) {
										e.printStackTrace();
									}
								}
							}
						});
						t.start();
					}
				}
			});
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(btn3, gridBagConstraints);
		workPanel.add(btn3);
		
		waitPanel.setLayout(new GridLayout(4,1));
		waitPanel.add(new JLabel("请开始拍摄学生照片",JLabel.CENTER));
		waitPanel.add(new JLabel("PLEASE TAKE THE PHOTO ID",JLabel.CENTER));
		waitPanel.add(new JLabel("请勿关闭此窗口",JLabel.CENTER));
		waitPanel.add(new JLabel("DO NOT CLOSE THIS WINDOW",JLabel.CENTER));
		
		welcomePanel.setLayout(new BorderLayout(0,10));
		welcomePanel.add(new JLabel("欢迎使用自动拍照辅助软件",JLabel.CENTER),BorderLayout.NORTH);
		JTextArea area = new JTextArea("使用前请做好以下准备工作：\n");
		area.append("1. 照片比例应为3：2\n");
		area.append("2. 学生名单已保存成csv格式的文件\n");
		area.append("3. 请按照软件提示操作\n");
		area.append("若您已经做好上述准备工作，您可以:");
		area.setEditable(false);
		JPanel flow = new JPanel();
		JButton newBtn = new JButton("New");
			newBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					newProject();
				}
			});
			flow.add(newBtn);
		JButton loadBtn = new JButton("Load");
			loadBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					loadProject();
				}
			});
			flow.add(loadBtn);
		welcomePanel.add(flow, BorderLayout.SOUTH);
		welcomePanel.add(area, BorderLayout.CENTER);
	}
	
	public void newProject()
	{
		datapath = new Datapath();
		if(datapath.getStudentDB().getBackup().size() != 0) {
			frame.remove(welcomePanel);
			frame.add(workPanel);
			loadItem.setEnabled(false);
			newItem.setEnabled(false);
			homeroomMenu.setEnabled(true);
			statusMenu.setEnabled(true);
			initStatusListArr(datapath.getStudentDB());
			frame.validate();
			frame.repaint();
		}
	}
	
	public void loadProject()
	{
		try {
			JFileChooser fchooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory().getPath());
			fchooser.setDialogTitle("请选择需要读取的文件");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("txt file","txt");
			fchooser.setFileFilter(filter);
			int returnVal = fchooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				String path = fchooser.getSelectedFile().getPath();
				datapath = new Datapath(path);
				frame.remove(welcomePanel);
				frame.add(workPanel);
				loadItem.setEnabled(false);
				newItem.setEnabled(false);
				homeroomMenu.setEnabled(true);
				statusMenu.setEnabled(true);
				initStatusListArr(datapath.getStudentDB());
				frame.validate();
				frame.repaint();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void photoProcessing()
	{
		new ImageViewer(this);
	}
	
	public void startThread() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				synchronized(object) {
					object.notify();
				}
			}
		});
		t.start();
	}
	
	public void setStatus(Student student)
	{
		if(student != null) {
			if(student.getStatus()){
				status.setText("输入学号以查找相应学生");
				studentInfo.setText(student.getCName() + "已完成拍摄工作");
			} else {
				status.setText("请核对学生信息：");
				studentInfo.setText(student.getID() + "   " + student.getCName() + "   " + student.getHomeroom());
				currentStudent = student;
			}
		} else {
			status.setText("输入学号以查找相应学生");
			studentInfo.setText(" ");
			currentStudent = null;
		}
	}
	
	public void makeMenuBar()
	{
		JMenuBar menubar = new JMenuBar();
		frame.setJMenuBar(menubar);
		
		JMenu fileMenu = new JMenu("Project");
			newItem = new JMenuItem("New Project");
				newItem.addActionListener(e -> newProject());
			fileMenu.add(newItem);
		
			loadItem = new JMenuItem("Load Project");
				loadItem.addActionListener(e -> loadProject());
			fileMenu.add(loadItem);
			
			JMenuItem saveItem = new JMenuItem("Save");
				saveItem.addActionListener(e -> datapath.save());
				
			JMenuItem exitItem = new JMenuItem("Exit");
				exitItem.addActionListener(e -> quit());
			fileMenu.add(exitItem);
		menubar.add(fileMenu);
		
		JMenu controlMenu = new JMenu("Control");
		controlMenu.addSeparator();
			homeroomMenu = new JMenu("Change Homeroom");
			homeroomMenu.setEnabled(false);
				JMenuItem aItem = new JMenuItem("A");
					aItem.addActionListener(e -> changeHomeroomTo(Homeroom.A));
					homeroomMenu.add(aItem);
				JMenuItem bItem = new JMenuItem("B");
					bItem.addActionListener(e -> changeHomeroomTo(Homeroom.B));
					homeroomMenu.add(bItem);
				JMenuItem cItem = new JMenuItem("C");
					cItem.addActionListener(e -> changeHomeroomTo(Homeroom.C));
					homeroomMenu.add(cItem);
				JMenuItem dItem = new JMenuItem("D");
					dItem.addActionListener(e -> changeHomeroomTo(Homeroom.D));
					homeroomMenu.add(dItem);
				JMenuItem eItem = new JMenuItem("E");
					eItem.addActionListener(e -> changeHomeroomTo(Homeroom.E));
					homeroomMenu.add(eItem);
				JMenuItem fItem = new JMenuItem("F");
					fItem.addActionListener(e -> changeHomeroomTo(Homeroom.F));
					homeroomMenu.add(fItem);
			controlMenu.add(homeroomMenu);
			
			statusMenu = new JMenu("Show Status List");
			statusMenu.setEnabled(false);
				JMenuItem aList = new JMenuItem("A");
					aList.addActionListener(e -> showStatusListOf(Homeroom.A));
					statusMenu.add(aList);
				JMenuItem bList = new JMenuItem("B");
					bList.addActionListener(e -> showStatusListOf(Homeroom.B));
					statusMenu.add(bList);
				JMenuItem cList = new JMenuItem("C");
					cList.addActionListener(e -> showStatusListOf(Homeroom.C));
					statusMenu.add(cList);
				JMenuItem dList = new JMenuItem("D");
					dList.addActionListener(e -> showStatusListOf(Homeroom.D));
					statusMenu.add(dList);
				JMenuItem eList = new JMenuItem("E");
					eList.addActionListener(e -> showStatusListOf(Homeroom.E));
					statusMenu.add(eList);
				JMenuItem fList = new JMenuItem("F");
					fList.addActionListener(e -> showStatusListOf(Homeroom.F));
					statusMenu.add(fList);
			controlMenu.add(statusMenu);
				
			
		menubar.add(controlMenu);

	}
	
	public StatusList getStatusList(Homeroom homeroom)
	{
		if(homeroom == Homeroom.A) {
			return statusListArr[0];
		} else if(homeroom == Homeroom.B) {
			return statusListArr[1];
		} else if(homeroom == Homeroom.C) {
			return statusListArr[2];
		} else if(homeroom == Homeroom.D) {
			return statusListArr[3];
		} else if(homeroom == Homeroom.E) {
			return statusListArr[4];
		} else{
			return statusListArr[5];
		}
	}
	
	public void initStatusListArr(StudentDB studentDB)
	{
		statusListArr = new StatusList[6];
		statusListArr[0] = new StatusList(studentDB.getStudentList(Homeroom.A),Homeroom.A);
		statusListArr[1] = new StatusList(studentDB.getStudentList(Homeroom.B),Homeroom.B);
		statusListArr[2] = new StatusList(studentDB.getStudentList(Homeroom.C),Homeroom.C);
		statusListArr[3] = new StatusList(studentDB.getStudentList(Homeroom.D),Homeroom.D);
		statusListArr[4] = new StatusList(studentDB.getStudentList(Homeroom.E),Homeroom.E);
		statusListArr[5] = new StatusList(studentDB.getStudentList(Homeroom.F),Homeroom.F);
	}
	
	public void showStatusListOf(Homeroom homeroom)
	{
		getStatusList(homeroom).setVisible(true);
	}
	
	public void changeHomeroomTo(Homeroom homeroom)
	{
		frame.setTitle("ControlPanel - " + homeroom);
		currentHomeroom = homeroom;
		currentStudentList = datapath.getStudentDB().getStudentList(homeroom);
	}
	
	public void quit()
	{
		int confirm = JOptionPane.showOptionDialog(null, "单击确定以退出，您当前的拍摄进度将会被自动保存。", 
				"退出确认", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(confirm == 0) {
			if(datapath != null) {
				datapath.save();
			}
			System.exit(0);
		}
	}
	
	public Student getCurrentStudent()
	{
		return currentStudent;
	}
	
	public Datapath getDatapath()
	{
		return datapath;
	}
	
	public static void main(String[] args)
	{
		new ControlPanel();
	}
}
