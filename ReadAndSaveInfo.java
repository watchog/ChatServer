/*
 * 将用户信息或群信息从内存保存到硬盘上
 */
package server.my.file.ReadAndSaveInfo;
import server.my.user.UserManager;
import server.my.group.GroupManager;
import java.io.*;
public class ReadAndSaveInfo{
	private boolean b;
	private UserManager um;
	private GroupManager gm;
	public ReadAndSaveInfo(UserManager um){
		new WriteToFile(um,1);		//‘1’表示写入用户信息
	}
	public ReadAndSaveInfo(GroupManager gm){
		new WriteToFile(gm,'v');	//‘v’表示写入群信息
	}
	public ReadAndSaveInfo(){
		
	}
	public GroupManager readGroupInfo(){
		return new ReadFromFile().readGroupInfo();
	}
	public UserManager readUserInfo(){
		return new ReadFromFile().readUserInfo();
	}
}
class WriteToFile{
	public WriteToFile(UserManager um,int a){
		try{
			File f = new File("userData.dat");		//用户信息文件
			if(!f.exists())
				f.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(um);
			oos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public WriteToFile(GroupManager gm,char b){
		
		try{
			File f = new File("groupData.dat");		//群信息文件
			if(!f.exists())
				f.createNewFile();
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(gm);
			oos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
class ReadFromFile{			//从硬盘中读取用户信息和群信息到内存中
	private File f1;
	private File f2;
	private UserManager um;
	private GroupManager gm;

	public ReadFromFile(){
		f1 = new File("userData.dat");
		f2 = new File("groupData.dat");
	}

	public GroupManager readGroupInfo(){
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f2));
			gm = (GroupManager) ois.readObject();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return gm;
	}
	public UserManager readUserInfo(){	
		try{
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f1));
			um = (UserManager) ois.readObject();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		return um;
	}
}
