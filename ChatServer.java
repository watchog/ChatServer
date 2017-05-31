/*
 * 服务端启动程序
 */

import java.lang.Thread;
import java.lang.Runnable;
import java.io.*;
import server.my.exit.Exit;
import server.my.user.UserInfo;
import server.my.user.UserManager;
import server.my.group.GroupInfo;
import server.my.group.GroupManager;
import server.my.handleMessage.HandleMessage;
import server.my.net.ReceivePool;
import server.my.net.SendPool;
import server.my.message.Message;
import server.my.message.MessageLinkedList;
import server.my.file.ReadAndSaveInfo.ReadAndSaveInfo;

public class ChatServer{
	public static void main(String[] args){
		GroupManager gm;
		UserManager um;
		File f1,f2;
		MessageLinkedList mll;
		ReadAndSaveInfo ras;

	
		f1 = new File("userData.dat");
		f2 = new File("groupData.dat");
		ras = new ReadAndSaveInfo();
		if(!f1.exists() || !f2.exists()){
			try{
				f1.createNewFile();		//创建用户信息存储文件
				f2.createNewFile();		//创建组信息存储文件
			}catch(IOException e){}
			gm = new GroupManager();
			um = new UserManager();
			new ReadAndSaveInfo(um);
			new ReadAndSaveInfo(gm);
		}else{
			gm = ras.readGroupInfo();		//读取群信息到内存中
			um = ras.readUserInfo();		//读取用户信息到内存中
		}
		System.out.println("群组数: "+gm.groupSize());
		System.out.println("用户数: "+um.userSize());
		for(long a = 10000;a < 19999;a++){			//初始化为都不在线
			UserInfo ui = um.getUser(Long.toString(a));
			if(ui == null)
				break;
			ui.offLine();
			System.out.println(ui.getIsOnline());
		}
		mll = new MessageLinkedList(um);		//创建消息缓冲区
		Thread sendPool = new Thread(new SendPool(mll));
		Thread receivePool = new Thread(new ReceivePool(mll));//创建消息接收线程
		Thread handleMessage = new Thread(new HandleMessage(mll,um,gm));//创建消息处理线程
		Thread exit = new Thread(new Exit(um,gm));

		sendPool.setPriority(Thread.MAX_PRIORITY);//创建消息发送线程
		sendPool.start();//消息发送线程启动
		receivePool.start();//消息发送线程启动
		handleMessage.start();//消息处理线程启动
		exit.start();//退出线程启动输入quit关闭服务端
	}	
}
