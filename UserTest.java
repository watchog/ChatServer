import server.my.user.UserInfo;
import java.io.*;

public class UserTest{
	public static void main(String[] args){
		File f = new File("userTest");
		try{
			f.createNewFile();
			UserInfo ui = new UserInfo("2808393583","1388946532","555");
			ui.setIpAddress("192.168.78.2");
			ui.addFriend("234792","my./");
			ui.addFriend("fsdfsdf","456546");
			ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(f));
			o.writeObject(ui);
			o.close();
			UserInfo ur;

			ObjectInputStream i = new ObjectInputStream(new FileInputStream(f));
			ur = (UserInfo)i.readObject();
			System.out.println("Account: "+ur.getAccount());
			System.out.println("name: "+ur.getName());
			System.out.println("password: "+ur.getPassword());
			System.out.println("Ip: "+ur.getIpAddress());
			System.out.println("friend-1: "+ur.getFriendInfo("234792"));
			System.out.println("friend-2: "+ur.getFriendInfo("fsdfsdf"));
			i.close();
		}catch(IOException e)
		{
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			System.out.println("error2");
		}
	}
}
