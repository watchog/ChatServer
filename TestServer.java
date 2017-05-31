import java.lang.Thread;
import java.util.Scanner;
import server.my.message.Message;
import java.net.*;
import java.io.*;

public class TestServer{
	public static void main(String[] args){
		Scanner s = new Scanner(System.in);
		Message me = new Message();
		try{
			me.sendAccount = "231312";
			me.receiveAccount = "54564";
			me.name = "hello";
			me.password = "123455";
			me.groupName = "eledc";
			me.ipAddress = "192.168.43.118";
			me.acknowledge = true;
			me.isSend = true;
			me.data = "Hello word";
			me.type = 1;
			Socket socket1 = new Socket("127.0.0.1",22138);

			ObjectOutputStream ois = new ObjectOutputStream(socket1.getOutputStream());
			ois.writeObject(me);
			System.out.println("成功发送");
			ois.close();
			ServerSocket server = new ServerSocket(12138);
			Socket socket = server.accept();

			ObjectInputStream oos = new ObjectInputStream(socket.getInputStream());
			me = (Message)oos.readObject();
			Thread.sleep(6000);
			System.out.println("sendAccount:"+me.sendAccount);
			System.out.println("receiveAccount:"+me.receiveAccount);
			System.out.println("name:"+me.name);
			System.out.println("password:"+me.password);
			System.out.println("groupName:"+me.groupName);
			System.out.println("ipAddress:"+me.ipAddress);
			System.out.println("acknowledge"+me.acknowledge);
			System.out.println("isSend:"+me.isSend);
			System.out.println("data"+me.data);
			System.out.println("type"+me.type);
			oos.close();
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}
}
