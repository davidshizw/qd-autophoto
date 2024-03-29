import java.io.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.filechooser.*;

public class Datapath {
	private static StudentDB studentDB;
	
	public Datapath(String path)
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			studentDB = new StudentDB();
			boolean stop = false;
			while(!stop) {
				String s = br.readLine();
				if(s == null) {
					stop = true;
				} else if(s.contains(",")) {
					studentDB.loadStudent(s);
				}
			}
			br.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public Datapath()
	{
		studentDB = new StudentDB();
		getStudentList();
	}

	public void getStudentList()
	{
		try {
			JFileChooser fchooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory().getPath());
			fchooser.setDialogTitle("请选择学生名单");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("csv file","csv");
			fchooser.setFileFilter(filter);
			int returnVal = fchooser.showOpenDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION){
				String path = fchooser.getSelectedFile().getPath();
				studentDB.importStudent(path);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public StudentDB getStudentDB()
	{
		return studentDB;
	}
	
	public void save(ArrayList<Student> order)
	{
		try {
			JFileChooser fchooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory().getPath());
			int returnVal = fchooser.showSaveDialog(null);
			if(returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File f = new File(fchooser.getSelectedFile().getPath());
			File f1 = new File(f.getParent()+File.separatorChar+"StudentList_DO_NOT_DELETE.txt");
			if(!f1.exists()) {
				studentDB.saveBackup(new File(f.getParent()+File.separatorChar+"StudentList_DO_NOT_DELETE.txt"));
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
			for(Student s: order) {
				writer.append(s.summary()+"\n");
			}
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}