import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class StatusList {
	private JFrame frame;
	private JPanel contentPane;
	private Homeroom homeroom;
	
	public StatusList(ArrayList<Student> list, Homeroom homeroom)
	{
		this.homeroom = homeroom;
		initialize(list.size());
		createStatusList(list);
		frame.pack();
	}
	
	public void setVisible(boolean isVisible)
	{
		frame.setVisible(isVisible);
	}
	
	public void initialize(int num)
	{
		frame = new JFrame();
		frame.setTitle("House " + homeroom + " Student Status");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e)
			{
				frame.setVisible(false);
			}
		});
		contentPane = (JPanel) frame.getContentPane();
		contentPane.setBorder(new EmptyBorder(12,12,12,12));
		contentPane.setLayout(new GridLayout(num+1,3,30,5));
		contentPane.add(new JLabel("Ñ§ºÅ"));
		contentPane.add(new JLabel("ÐÕÃû"));
		contentPane.add(new JLabel("×´Ì¬"));
	}
	
	public void createStatusList(ArrayList<Student> list)
	{
		for(Student s: list) {
			contentPane.add(new JLabel(Integer.toString(s.getID())));
			contentPane.add(new JLabel(s.getCName()));
			JLabel label = new JLabel();
			if(s.getStatus()) {
				label.setForeground(Color.GREEN.darker());
				label.setText("True");
			} else {
				label.setForeground(Color.RED);
				label.setText("False");
			}
			contentPane.add(label);
		}
	}
	
	public void update(Student student)
	{
		if(student.getHomeroom() == homeroom) {
			int index = 3*(student.getID())+2;
			contentPane.remove(index);
			JLabel label = new JLabel();
			if(student.getStatus()) {
				label.setForeground(Color.GREEN.darker());
				label.setText("True");
			} else {
				label.setForeground(Color.RED);
				label.setText("False");
			}
			contentPane.add(label,index);
			frame.pack();
			frame.repaint();
		}
	}
	
	// to be deleted
	public static void main(String[] args)
	{
		Student temp = new Student("Ê©ÖÙÍþ","David",1,Homeroom.A);
		ArrayList<Student> list = new ArrayList<Student>();
		list.add(temp);
		StatusList a = new StatusList(list,Homeroom.A);
		temp.setStatus(true);
		a.update(temp);
		
	}
}
