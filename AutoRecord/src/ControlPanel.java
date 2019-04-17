import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class ControlPanel {
	private static JFrame frame = new JFrame();
	private static JLabel status = new JLabel("״̬��׼������");
	private static JLabel studentInfo = new JLabel(" ");
	private static JPanel workPanel = new JPanel();
	private static JPanel initPanel = new JPanel();
	private static Datapath datapath = null;
	private static Student currentStudent = null;
	private static ArrayList<Student> order = new ArrayList<Student>();
	
	public ControlPanel()
	{
		init();
		JPanel panel = (JPanel) frame.getContentPane();
		panel.setBorder(new EmptyBorder(10,15,10,15));
		frame.setSize(250,250);
		frame.add(initPanel);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e)
			{
				int confirm = JOptionPane.showOptionDialog(null, "����ȷ�����˳�������ǰ�������¼���ᱻ�Զ����档", 
						"�˳�ȷ��", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if(confirm == 0) {
					if(datapath != null) {
						datapath.save(order);
					}
					frame.dispose();
				}
			}
		});
		frame.setVisible(true);
	}
	
	public void init()
	{
		JPanel temp = new JPanel();
		initPanel.setLayout(new BorderLayout(0,10));
		JButton btn1 = new JButton("New");
			btn1.addActionListener(e -> newProject());
			temp.add(btn1);
		JButton btn2 = new JButton("Load");
			btn2.addActionListener(e -> loadProject());
			temp.add(btn2);
		initPanel.add(temp, BorderLayout.CENTER);
		initPanel.add(new JLabel("״̬���ȴ���ʼ��"), BorderLayout.SOUTH);
		
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
		
		JTextField textField1 = new JTextField(2);
		textField1.addFocusListener(new JTextFieldHintListener("�༶", textField1));
			textField1.addKeyListener(new KeyAdapter() {
				public void keyTyped(KeyEvent e)
			    {
					if (e.getKeyChar() == e.VK_A || e.getKeyChar() == e.VK_B
							|| e.getKeyChar() == e.VK_ENTER || e.getKeyChar() == e.VK_TAB
							|| e.getKeyChar() == e.VK_BACK_SPACE || e.getKeyChar() == e.VK_DELETE 
							|| e.getKeyChar() == e.VK_LEFT || e.getKeyChar() == e.VK_RIGHT 
							|| e.getKeyChar() == e.VK_ESCAPE || e.getKeyChar() == e.VK_C
							|| e.getKeyChar() == e.VK_D || e.getKeyChar() == e.VK_E
							|| e.getKeyChar() == e.VK_F)
					{
						return;   
					}
					e.consume();
			    }
			});
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(textField1, gridBagConstraints);
		workPanel.add(textField1);
		
		JTextField textField2 = new JTextField(2);
		textField2.addFocusListener(new JTextFieldHintListener("ѧ��", textField2));
		textField2.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e)
		    {
				if ((e.getKeyChar() >= e.VK_0 && e.getKeyChar() <= e.VK_9) 
						|| e.getKeyChar() == e.VK_ENTER || e.getKeyChar() == e.VK_TAB
						|| e.getKeyChar() == e.VK_BACK_SPACE || e.getKeyChar() == e.VK_DELETE 
						|| e.getKeyChar() == e.VK_LEFT || e.getKeyChar() == e.VK_RIGHT 
						|| e.getKeyChar() == e.VK_ESCAPE) 
				{
					return;   
				}
				e.consume();
		    }
		});
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(textField2, gridBagConstraints);
		workPanel.add(textField2);
		
		JButton btn6 = new JButton("��ѯ");
			btn6.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					studentInfo.setText(" ");
					if(textField1.getText().length() > 1 || textField1.getText().length() == 0) {
						status.setText("��Ч�༶�����������롣");
						return;
					} else if(textField2.getText().length() == 0){
						status.setText("��Чѧ�ţ����������롣");
						return;
					} else {
						ArrayList<Student> studentList = datapath.getStudentDB().getStudentList(datapath.getStudentDB().getHomeroom(textField1.getText()));
						if(Integer.parseInt(textField2.getText())==0 || Integer.parseInt(textField2.getText()) > studentList.size()) {
							status.setText("��Чѧ�ţ����������롣");
							return;
						}
						for(Student s: studentList) {
							if(s.getID() == Integer.parseInt(textField2.getText())) {
								studentInfo.setText(s.getCName());
								currentStudent = s;
								status.setText("��ѯ�ɹ�");
							}
						}
					}
				}
			});
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(btn6, gridBagConstraints);
		workPanel.add(btn6);
		
		JButton btn3 = new JButton("ȷ��");
			btn3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					if(studentInfo.getText().equals(" ")) {
						return;
					} else {
						order.add(currentStudent);
						status.setText("״̬��׼������");
						studentInfo.setText(" ");
					}
				}
			});
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 1;
		gridBagConstraints.gridheight = 1;
		gridBagLayout.setConstraints(btn3, gridBagConstraints);
		workPanel.add(btn3);
	}
	
	public void newProject()
	{
		datapath = new Datapath();
		if(datapath.getStudentDB().getBackup().size() != 0) {
			status.setText("�½���Ŀ�ɹ�");
			frame.remove(initPanel);
			frame.add(workPanel);
			frame.revalidate();
			frame.repaint();
		}
	}
	
	public void loadProject()
	{
		try {
			JFileChooser fchooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory().getPath());
			fchooser.setDialogTitle("��ѡ�񻺴��ļ�����ѧ��������");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("txt file","txt");
			fchooser.setFileFilter(filter);
			int returnVal = fchooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				String path = fchooser.getSelectedFile().getPath();
				datapath = new Datapath(path);
				status.setText("������Ŀ�ɹ�");
				frame.remove(initPanel);
				frame.add(workPanel);
				frame.revalidate();
				frame.repaint();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		ControlPanel c = new ControlPanel();
	}
}