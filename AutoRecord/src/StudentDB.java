import java.util.*;
import java.io.*;

public class StudentDB 
{
	private static ArrayList<Student> houseA = new ArrayList<Student>();
	private static ArrayList<Student> houseB = new ArrayList<Student>();
	private static ArrayList<Student> houseC = new ArrayList<Student>();
	private static ArrayList<Student> houseD = new ArrayList<Student>();
	private static ArrayList<Student> houseE = new ArrayList<Student>();
	private static ArrayList<Student> houseF = new ArrayList<Student>();
	private static ArrayList<Student> backup = new ArrayList<Student>();
	
	private static final int HOMEROOM_COLUMN = 0;
	private static final int ID_COLUMN = 1;
	private static final int CHINESE_NAME_COLUMN = 2;
	private static final int ENGLISH_NAME_COLUMN = 3;
	
	public void importStudent(String path) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(path));
		reader.readLine();
		String line = reader.readLine();
		while(line != null) {
			String[] arr = line.replace(" ","").split(",");
			if(arr[HOMEROOM_COLUMN].length() == 1){
				try {
					int ID = Integer.parseInt(arr[ID_COLUMN].substring(arr[ID_COLUMN].length()-2));
                    Homeroom homeroom = getHomeroom(arr[HOMEROOM_COLUMN]);
                    Student student = new Student(arr[CHINESE_NAME_COLUMN],arr[ENGLISH_NAME_COLUMN],ID,homeroom,false);
                    System.out.println(student);
                    allocate(student,homeroom);	
                    backup.add(student);
				} catch(Exception e){
					reader.close();
	            }
			}
			line = reader.readLine();
		}
		reader.close();
	}
	
	public void loadStudent(String s) throws IOException
	{
		String[] arr = s.split(",");
		Homeroom homeroom = getHomeroom(arr[3]);
		Student student =  new Student(arr[0],arr[1],Integer.parseInt(arr[2]),homeroom,Boolean.parseBoolean(arr[4]));
		allocate(student,homeroom);
		backup.add(student);
	}
	
	public void saveBackup(File f) throws IOException
	{
		BufferedWriter writer = new BufferedWriter(new FileWriter(f,true));
		for(Student student:backup) {
			writer.append(student.toString()+"\n");
		}
		writer.close();
	}
	
	public ArrayList<Student> getBackup()
	{
		return backup;
	}
	
	public Homeroom getHomeroom(String homeroom)
	{
		homeroom = homeroom.toUpperCase();
		if(homeroom.equals("A")) {
			return Homeroom.A;
		} else if(homeroom.equals("B")) {
			return Homeroom.B;
		} else if(homeroom.equals("C")) {
			return Homeroom.C;
		} else if(homeroom.equals("D")) {
			return Homeroom.D;
		} else if(homeroom.equals("E")) {
			return Homeroom.E;
		} else if(homeroom.equals("F")) {
			return Homeroom.F;
		}
		return null;
	}
	
	private void allocate(Student student,Homeroom homeroom)
	{
		if(homeroom == Homeroom.A) {
			houseA.add(student);
		} else if(homeroom == Homeroom.B) {
			houseB.add(student);
		} else if(homeroom == Homeroom.C) {
			houseC.add(student);
		} else if(homeroom == Homeroom.D) {
			houseD.add(student);
		} else if(homeroom == Homeroom.E) {
			houseE.add(student);
		} else if(homeroom == Homeroom.F) {
			houseF.add(student);
		}
	}
	
	public ArrayList<Student> getStudentList(Homeroom homeroom)
	{
		if(homeroom == Homeroom.A) {
			return houseA;
		} else if(homeroom == Homeroom.B) {
			return houseB;
		} else if(homeroom == Homeroom.C) {
			return houseC;
		} else if(homeroom == Homeroom.D) {
			return houseD;
		} else if(homeroom == Homeroom.E) {
			return houseE;
		} else{
			return houseF;
		}
	}
}