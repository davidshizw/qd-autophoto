import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.*;

public class Datapath {
	private static String PARENT_DIRECTORY_PATH;
	private static String[] PHOTO_PATH_ARRAY;
	private static StudentDB studentDB;
	
	public Datapath(String path)
	{
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			PARENT_DIRECTORY_PATH = br.readLine();
			PHOTO_PATH_ARRAY = new String[6];
			for(int i = 0; i < 6; i++) {
				PHOTO_PATH_ARRAY[i] = br.readLine();
			}
			studentDB = new StudentDB();
			boolean stop = false;
			while(!stop) {
				String s = br.readLine();
				if(s == null) {
					stop = true;
				} else {
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
		if(setParentDirectory()) {
			getStudentList();
		}
	}
	
	public boolean setParentDirectory()
	{
		JFileChooser fchooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory().getPath());
		fchooser.setDialogTitle("请选择目标工作文件夹");
		fchooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fchooser.showOpenDialog(fchooser);
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			PARENT_DIRECTORY_PATH = fchooser.getSelectedFile().getAbsolutePath();
			PHOTO_PATH_ARRAY = new String[] {PARENT_DIRECTORY_PATH + File.separatorChar + "House A",PARENT_DIRECTORY_PATH + File.separatorChar + "House B",
					 						 PARENT_DIRECTORY_PATH + File.separatorChar + "House C",PARENT_DIRECTORY_PATH + File.separatorChar + "House D",
					 						 PARENT_DIRECTORY_PATH + File.separatorChar + "House E",PARENT_DIRECTORY_PATH + File.separatorChar + "House F"};				for(String path: PHOTO_PATH_ARRAY) {
			File photoDir = new File(path);
				photoDir.mkdir();
			}
			return true;
		} else{
			return false;
		}
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
	
	public void save()
	{
		try {
			File f = new File(PARENT_DIRECTORY_PATH + File.separatorChar + "IMPORTANT_DATA_DO_NOT_DELETE.txt");
			if(f.exists()) {
				f.delete();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
			writer.append(PARENT_DIRECTORY_PATH+"\n");
			for(String s: PHOTO_PATH_ARRAY) {
				writer.append(s+"\n");
			}
			writer.close();
			studentDB.saveBackup(f);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public StudentDB getStudentDB()
	{
		return studentDB;
	}
	
	public String getPhotoPath(Homeroom homeroom)
	{
		if(homeroom == Homeroom.A) {
			return PHOTO_PATH_ARRAY[0];
		} else if(homeroom == Homeroom.B) {
			return PHOTO_PATH_ARRAY[1];
		} else if(homeroom == Homeroom.C) {
			return PHOTO_PATH_ARRAY[2];
		} else if(homeroom == Homeroom.D) {
			return PHOTO_PATH_ARRAY[3];
		} else if(homeroom == Homeroom.E) {
			return PHOTO_PATH_ARRAY[4];
		} else{
			return PHOTO_PATH_ARRAY[5];
		}
	}
}