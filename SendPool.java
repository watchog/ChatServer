/*
 * 向客户端发送处理后的信息
 */
package server.my.net;
import server.my.message.MessageLinkedList;
import server.my.message.Message;
import java.lang.Runnable;
import java.lang.Thread;
import java.io.*;
import java.net.*;

class Send implements Runnable{
	private MessageLinkedList mll;
	private int port = 22138;
	private Message me;
	private Socket socket;
	private ObjectOutputStream oos;

	public Send(MessageLinkedList mll){
		System.out.println("发送服务启动！！！");
		this.mll = mll;
	}
	
	public void run(){
		while(true){
			try{
			
				if(!mll.isEmptye()){
					me = mll.pope();
					socket = new Socket(InetAddress.getByName(me.ipAddress),port);
					oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(me);
					System.out.println("发送一则消息");
					oos.close();
					socket.close();
				}
			
			}catch(ConnectException e){
				System.out.println("与接收者创建链接失败");
				try{
					oos.close();
					socket.close();
				}catch(IOException s){
					s.printStackTrace();
				}
			}catch(Throwable e){
				e.printStackTrace();
				try{
					oos.close();
					socket.close();
				}catch(IOException s){
					s.printStackTrace();
				}
			}
		}
	}
}

public class SendPool implements Runnable{
	private int threadNum = 20;
	private Thread sendPool;
	
	public SendPool(MessageLinkedList mll){
			sendPool = new Thread(new Send(mll));
			sendPool.start();
	}

	public void run(){}
}
