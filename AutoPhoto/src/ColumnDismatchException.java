public class ColumnDismatchException extends Exception
{
	public ColumnDismatchException() 
	{
	}
	
	public ColumnDismatchException(String message) 
	{
		super(message);
	}
	
	public ColumnDismatchException(Throwable cause) 
	{
		super(cause);
	}
	
	public ColumnDismatchException(String message,Throwable cause) 
	{
		super(message,cause);
	}
}