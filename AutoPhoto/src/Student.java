import java.util.Calendar;

public class Student
{
	private static int ENTRY_YEAR = Calendar.getInstance().get(Calendar.YEAR)-2000;
	private String cName;
	private String eName;
	private Homeroom homeroom;
	private int ID;
	private boolean status;
	
	// new student
	public Student(String cName,String eName,int ID,Homeroom homeroom)
	{
		this.cName = cName;
		this.eName = eName;
		this.homeroom = homeroom;
		this.ID = ID;
		status = false;
	}
	
	// read from studentDB
	public Student(String cName,String eName,int ID,Homeroom homeroom,boolean status)
	{
		this.cName = cName;
		this.eName = eName;
		this.homeroom = homeroom;
		this.ID = ID;
		this.status = status;
	}
	
	public int getID()
	{
		return ID;
	}
	
	public String getCName()
	{
		return cName;
	}
	
	public String getEName()
	{
		return eName;
	}
	
	public Homeroom getHomeroom()
	{
		return homeroom;
	}
	
	public boolean getStatus()
	{
		return status;
	}
	
	public void setStatus(boolean status)
	{
		this.status = status;
	}
	
	@Override
	public String toString()
	{
		return cName + "," + eName + "," + ID + "," + homeroom + "," + status;
	}
	
	public String getFilename(String photo)
	{
		return photo + "_" + ENTRY_YEAR + homeroom + "_" + String.format("%02d",ID) + "_" + cName + "_" + eName + ".JPG";
	}
}