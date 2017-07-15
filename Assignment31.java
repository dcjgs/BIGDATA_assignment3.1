import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;


public class Assignment31 {
	
	private static void listfilesTask1(Path path,FileSystem fs) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		List<String> fileList = new ArrayList<String>();
		FileStatus[] status = fs.listStatus(path);
		
		for (FileStatus stat : status) {
			System.out.println("Name = " + stat.getOwner());
			System.out.println("Path = " + stat.getPath().toString());
			System.out.println("Isfile = " + stat.isFile());
			System.out.println("IsDir = " + stat.isDir());
		}
		
		
	}
	
	private static List<String> listfilesTask2(Path path,FileSystem fs) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		List<String> fileList = new ArrayList<String>();
		FileStatus[] status = fs.listStatus(path);
		
		for (FileStatus stat : status) {
			fileList.add(stat.getPath().toString());
			
			if (stat.isDir())
			{			    
				List<String> deeperList = listfilesTask2(stat.getPath(), fs);
				fileList.addAll(deeperList);
			}
			
			    
		}
		return fileList;
		
	}
	
	private static void displayDetails(String[] arguments, FileSystem fs) throws FileNotFoundException, IOException {
		
		for (int a=1; a< arguments.length; a++){
			System.out.println("===> Recursively list directories - Assignment 3.1 Task3..Now doing path "+arguments[a]+" <====");
			List<String> newlist = listfilesTask2(new Path(arguments[a]), fs);
			for (int i = 0; i < newlist.size(); i++) {
				System.out.println(newlist.get(i));
			}
		}
		
	}

	
	public static void main(String[] args) {
		Configuration conf = new Configuration();
		conf.addResource(new Path("/usr/local/hadoop/etc/hadoop/core-site.xml"));
		conf.addResource(new Path("/usr/local/hadoop/etc/hadoop/hdfs-site.xml"));
		Path path= new Path("/acadguild");
		try {
			FileSystem fs = FileSystem.get(conf);
			
			if (args.length == 0 ){
				System.out.println("************* displaying file/dir details in hdfs for default path /acadguild **********");
				System.out.println("Usage1 takes a default hdfs path /acadguild : hadoop jar assignment3.jar ");
				System.out.println("Usage2 Task1 takes a hdfs path /acadguild : hadoop jar assignment3.jar task1 /acadguild");
				System.out.println("Usage3 Task2( subdirs,files recursive )takes a hdfs path /acadguild : hadoop jar assignment3.jar task2 /acadguild");
				System.out.println("Usage4 Task3( subdirs,files recursive )takes a list of hdfs path /acadguild : hadoop jar assignment3.jar task3 /acadguild /acadguild/testDir");
				//path = new Path("/acadguild");
				System.out.println("==> list directories - Assignment 3.1 Task1");
				listfilesTask1(path, fs);

				
			} 
			// Usage2 Task1 takes a hdfs path /acadguild : hadoop jar assignment3.jar task1 /acadguild
			if (args.length == 2 && "task1".equalsIgnoreCase(args[0])) {
				System.out.println("************* displaying file/dir details in hdfs for input path="+args[0]+" **********");
				path = new Path(args[1]);				
				System.out.println("==> list directories - Assignment 3.1 Task1");
				listfilesTask1(path, fs);
			}
			
			// Usage3 Task2 takes a hdfs path /acadguild : hadoop jar assignment3.jar task2 /acadguild
			if (args.length == 2 && "task2".equalsIgnoreCase(args[0])) {
				System.out.println("==> Recursively list directories - Assignment 3.1 Task2");
				path = new Path(args[1]);	
				List<String> newlist = listfilesTask2(path, fs);
				for (int i = 0; i < newlist.size(); i++) {
					System.out.println(newlist.get(i));
				}
			}
			//Usage4 Task3( subdirs,files recursive )takes a list of hdfs path /acadguild : hadoop jar assignment3.jar task3 /acadguild /acadguild/testDir
			if (args.length > 2  && "task3".equalsIgnoreCase(args[0])) {
				displayDetails(args,fs);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
