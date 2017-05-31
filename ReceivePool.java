/*
 * 接收线程等待客户端发送的信息
 */
package server.my.net;
import java.lang.Runnable;
import java.lang.Thread;
import server.my.message.Message;
import server.my.message.MessageLinkedList;
import java.net.*;
import java.io.*;

class Receive implements Runnable{	//接受请求
	private static int count = 0;
	private final int port = 12138;
	private boolean isBusy  = false;
	private PipedOutputStream pos;
	private MessageLinkedList mll;
	private ServerSocket server;
	private Socket socket ;
	private InputStream is;
	private ObjectInputStream ois;
	private Message ms;

	public Receive(MessageLinkedList mll){
		System.out.println("接收服务"+count+": 已经启动");
		pos = new PipedOutputStream();
		this.mll = mll;
		count++;
	}
	public boolean threadIsBusy(){
		return this.isBusy;
	}
	public void run(){
		while(true){
			try{
				server = new ServerSocket(port);
				socket = server.accept();
				is = socket.getInputStream();
				ois = new ObjectInputStream(is);
				if(ois == null){
					socket.close();
					server.close();
					continue;
				}
				ms = (Message) ois.readObject();
				this.push(ms);
				System.out.println("收到一则消息");
				System.out.println(mll.isEmpty());
				socket.close();
				server.close();
				ois.close();
			}catch(EOFException e){
				try{
					socket.close();
					server.close();
					ois.close();
				}catch(IOException d){
					;
				}
			}catch(IOException e){
				e.printStackTrace();
			}catch(ClassNotFoundException e){
				e.printStackTrace();
			}
		}
	}

	public synchronized void push(Message me){
		this.mll.push(me);
	}
}
//modify
public class ReceivePool implements Runnable{
	private final int threadNum = 20;
	private Thread pool;

	public ReceivePool(MessageLinkedList mll){
		pool = new Thread(new Receive(mll));	
		pool.start();
	}

	public void run(){}
}


