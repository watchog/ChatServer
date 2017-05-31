/*
 * 服务端处理客户端发送的信息并产生处理后的信息发送回客户端
 */
package server.my.handleMessage;
import server.my.handleMessage.CreateNewAccount;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Thread;
import java.lang.Runnable;
import server.my.message.Message;
import server.my.user.UserManager;
import server.my.user.UserInfo;
import server.my.group.GroupManager;
import server.my.message.MessageLinkedList;
import server.my.file.ReadAndSaveInfo.ReadAndSaveInfo;

class Result{						//确认发送给单个用户或多个用户
	public	static boolean b;
	public  static Message r1;
	public  static Message r2[];
}

public class HandleMessage implements Runnable{
	private Message me;
	private MessageLinkedList mll;
	private UserManager um;
	private GroupManager gm;

	public HandleMessage(MessageLinkedList mll,UserManager um,GroupManager gm){
		System.out.println("信息处理线程启动成功！！！");
		this.mll = mll;
		this.um = um;
		this.gm = gm;
	}
	
	public void run(){
		while(true){
			if(mll.isEmpty()){
				continue;
			}
			this.handle(mll.pop());
			if(Result.b == true){
				if(Result.r1.isSend == true){
					mll.pushe(Result.r1);
					System.out.println("here");
				}else
					mll.pusho(Result.r1);
				System.out.println("isSend: "+Result.r1.isSend);
			}else{
				for(int i = 0;i < Result.r2.length;i++){
					if(Result.r2[i] == null)
						break;
					if(Result.r2[i].isSend == true){
						mll.pushe(Result.r2[i]);
					}else
						mll.pusho(Result.r2[i]);
					System.out.println("isSend: "+Result.r2[i].isSend);
				}
			}
			System.out.println("成功处理一则消息");
		}
	}
	public synchronized void handle(Message me){
		switch(me.type){
			case 0:
				new LogOut(um,me);
				break;
			case 1:
				LogIn li = new LogIn(um,me);
				li.handle();
				Result.r1 = li.handleResult();
				Result.b = true;
				mll.popo(Result.r1.receiveAccount);
				break;
			case 2:
				SignIn si = new SignIn(um,me);
				si.handle();
				Result.r1 = si.handleResult();
				Result.b = true;
				new ReadAndSaveInfo(um);
				break;
			case 3:
				ModifyName mn = new ModifyName(um,me);
				mn.handle();
				Result.r1 = mn.handleResult();
				Result.b = true;
				break;
			case 4:
				RequestJoinGroup rjg1 = new RequestJoinGroup(gm,me,um);
				rjg1.handle();
				Result.r1 = rjg1.handleResult();
				Result.b = true;
				break;
			case 5:
				ResponseJoinGroup rjg2 = new ResponseJoinGroup(gm,me,um);
				rjg2.handle();
				Result.r1 = rjg2.handleResult();
				Result.b = true;
				break;
			case 6:
				QuitFromGroup qfg = new QuitFromGroup(um,gm,me);
				qfg.handle();
				Result.r1 = qfg.handleResult();
				Result.b = true;
				break;
			case 7:
				RequestFriend rf = new RequestFriend(um,me);
				rf.handle();
				Result.r1 = rf.handleResult();
				Result.b = true;
				break;
			case 8:
				ResponeFriend rf1 = new ResponeFriend(um,me);
				rf1.handle();
				Result.r2 = rf1.handleResult();
				Result.b = false;
				break;
			case 9:
				RemoveFriend  rf2 = new RemoveFriend(um,me);
				rf2.handle();
				Result.r2 = rf2.handleResult();
				Result.b = false;
				break;
			case 10:
				CreateNewGroup cng = new CreateNewGroup(um,gm,me);
				cng.handle();
				Result.r1 = cng.handleResult();
				new ReadAndSaveInfo(gm);
				Result.b = true;
				break;
			case 11:
				RemoveGroupMember rgm = new RemoveGroupMember(um,gm,me);
				rgm.handle();
				Result.r1 = rgm.handleResult();
				Result.b = true;
				break;
			case 12:
				SendMessageToFriend smtf = new SendMessageToFriend(um,me);
				smtf.handle();
				Result.r1 = smtf.handleResult();
				Result.b = true;
				break;
			case 13:
				SendMessageToGroup smtg = new SendMessageToGroup(um,gm,me); 
				smtg.handle();
				Result.r2 = smtg.handleResult();
				Result.b = false;
				break;
		}
	}
}
class LogOut{							//用户下线
	public LogOut(UserManager um,Message me){
			UserInfo ui = um.getUser(me.sendAccount);
			ui.offLine();
			if(!ui.getIsOnline())
				System.out.println("帐号"+me.sendAccount+"已下线");
	}
}
class LogIn{							//用户登录
	Message me;
	UserManager um;
	Message result;
	public LogIn(UserManager um,Message me){
		this.me = me;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		String password = um.getUserPassword(me.sendAccount);
		if(password == null)
			return;
		if(password.equals(me.password)){
			System.out.println(um.userIsOnline(me.sendAccount));
			if(!um.userIsOnline(me.sendAccount)){
				result.ui = um.getUser(me.sendAccount);
				System.out.println("帐号"+me.sendAccount+"上线");
			}else{
				result.ui = null;
				result.isSend = true;
				result.ipAddress = me.ipAddress;
				return ;
			}
			um.switchUserState(me.sendAccount);
		}else{
			result.acknowledge = false;
			return ;
		}

		um.setUserIpAddress(me.sendAccount,me.ipAddress);
		result.isSend = true;
		result.acknowledge = true;
		result.ipAddress = me.ipAddress;
	}

	public Message handleResult(){
		result.type = 1;
		return this.result;
	}
}

class SignIn{						//用户注册
	Message me;
	UserManager um;
	Message result;
	public SignIn(UserManager um,Message me){
		this.me = me;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		CreateNewAccount cna = new CreateNewAccount();
		String account = cna.getAccount();
		cna.signIn(um,account,me.name,me.password);
		um.setUserIpAddress(account,me.ipAddress);
		um.switchUserState(account);
		result.acknowledge = true;
		result.ui = um.getUser(account);
		result.isSend = true;
		result.ipAddress = me.ipAddress;
	}

	public Message handleResult(){
		result.type = 1;
		return this.result;
	}
}

class ModifyName{					//修改用户昵称
	Message me;
	UserManager um;
	Message result;
	public ModifyName(UserManager um,Message me){
		this.me = me;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		um.setUserIpAddress(me.sendAccount,me.ipAddress);
		um.modifyUserName(me.sendAccount,me.name);
		result.acknowledge = true;
		result.isSend = true;
		result.ui = um.getUser(me.sendAccount);
		result.ipAddress = me.ipAddress;
	}
	public Message handleResult(){
		result.type = 3;
		return this.result;
	}
}

class RequestJoinGroup{					//请求加入群
	Message me;
	GroupManager gm;
	Message result;
	UserManager um;

	public RequestJoinGroup(GroupManager gm,Message me,UserManager um){
		this.me = me;
		this.gm = gm;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		result.sendAccount = me.sendAccount;
		result.receiveAccount = gm.getManagerAccount(me.groupAccount);
		result.groupAccount = me.groupAccount;
		System.out.println("ManagerAccount: "+result.receiveAccount);
		result.data = new String(me.name+"请求加群！！！");

		if(um.userIsOnline(result.receiveAccount)){
			result.isSend = true;
			result.ipAddress = um.getUserIpAddress(result.receiveAccount);
		}else{
			result.isSend = false;
		}
	}
	public Message handleResult(){
		result.type = 4;
		return result;
	}
}

class ResponseJoinGroup{				//回复是否允许加入群
	Message me;
	GroupManager gm;
	UserManager um;
	Message result;
	ArrayList<String> al;

	public ResponseJoinGroup(GroupManager gm,Message me,UserManager um){
		this.me = me;
		this.gm = gm;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		if(me.acknowledge == true){
			gm.addMember(me.groupAccount,me.receiveAccount,me.name);
			um.addGroup(me.receiveAccount,me.groupAccount,gm.getGroupName(me.groupAccount));
			result.al = gm.memberList(me.groupAccount);
			um.setGroupMember(me.sendAccount,me.groupAccount,result.al);
			result.receiveAccount = me.receiveAccount;
			result.data = "成功加入群"+gm.getGroupName(me.groupAccount);
			result.ui = um.getUser(me.receiveAccount);
		}
		if(um.userIsOnline(result.receiveAccount)){
			result.ipAddress = um.getUserIpAddress(result.receiveAccount);
			result.isSend = true;
		}else
			result.isSend = false;
	}
	public Message handleResult(){
		result.type = 5;
		return result;
	}
}

class QuitFromGroup{					//退出群
	Message me;
	GroupManager gm;
	UserManager um;
	Message result;
	public QuitFromGroup(UserManager um,GroupManager gm,Message me){
		this.me = me;
		this.gm = gm;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		System.out.println(me.sendAccount+" "+me.groupAccount);
		gm.removeMember(me.groupAccount,me.sendAccount);
		result.receiveAccount = me.sendAccount;
		result.acknowledge = true;
		result.al = gm.memberList(me.groupAccount);
		result.receiveAccount = gm.getManagerAccount(me.groupAccount);
		um.setGroupMember(result.receiveAccount,me.groupAccount,result.al);
		UserInfo ui = um.getUser(me.sendAccount);
		ui.removeGroup(me.groupAccount);
		result.ui = ui;
		if(um.userIsOnline(result.receiveAccount)){
			result.ipAddress = um.getUserIpAddress(me.sendAccount);
			result.isSend = true;
		}else
			result.isSend = false;
		result.data = "成功退出群！！！";
	}
	public Message handleResult(){
		result.type = 6;
		return result;
	}
}

class RequestFriend{				//请求加好友
	Message me;
	Message result;
	UserManager um;

	public RequestFriend(UserManager um,Message me){
		this.me = me;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		result.receiveAccount = me.receiveAccount;
		result.sendAccount = me.sendAccount;
		result.data = new String(me.name+"请求加好友！！！");
		result.acknowledge = false;
		result.name = me.name;
		if(um.userIsOnline(me.receiveAccount)){
			result.ipAddress = um.getUserIpAddress(me.receiveAccount);
			result.isSend = true;
		}else
			result.isSend = false;
	}
	public Message handleResult(){
		result.type = 7;
		return result;
	}
}

class ResponeFriend{			//确认是否允许加好友
	Message me;
	Message[] result;
	UserManager um;
	UserInfo ui;
	

	public ResponeFriend(UserManager um,Message me){
		this.me = me;
		this.um = um;
		this.result = new Message[2];
		result[1] = new Message();
		result[0] = new Message();
	}

	public void handle(){
		if(me.acknowledge == true){
			ui = um.getUser(me.receiveAccount);
			ui.addFriend(me.sendAccount,me.name);
			result[0].receiveAccount = me.receiveAccount;
			result[0].ui = ui;
			result[0].data = new String("与"+me.name+"成为好友！！！");
			if(um.userIsOnline(me.receiveAccount)){
				result[0].ipAddress = um.getUserIpAddress(me.receiveAccount);
				result[0].isSend = true;
			}else
				result[0].isSend = false;

			ui = um.getUser(me.sendAccount);
			ui.addFriend(me.receiveAccount,um.getUserName(me.receiveAccount));
			result[1].receiveAccount = me.sendAccount;
			result[1].ui = ui;
			result[1].data = new String("与"+um.getUserName(me.receiveAccount)+"成为好友！！！");
			if(um.userIsOnline(me.sendAccount)){
				result[1].ipAddress = um.getUserIpAddress(me.sendAccount);
				result[1].isSend = true;
			}else
				result[1].isSend = false;

		}else{
			result[0].receiveAccount = me.receiveAccount;
			result[0].data = new String("被"+me.name+"拒绝");
			result[0].ui = null;
			if(um.userIsOnline(me.receiveAccount)){
				result[0].ipAddress = um.getUserIpAddress(me.receiveAccount);
				result[0].isSend = true;
			}else
				result[0].isSend = false;

			result[1].receiveAccount = me.sendAccount;
			result[1].ui = null;
			if(um.userIsOnline(me.sendAccount)){
				result[1].ipAddress = um.getUserIpAddress(me.sendAccount);
				result[1].isSend = true;
			}else
				result[1].isSend = false;
		}
	}
	public Message[] handleResult(){
		result[0].type = 8;
		result[1].type = 8;
		return result;
	}
}

class RemoveFriend{				//移除好友
	Message me;
	UserManager um;
	Message[] result;

	public RemoveFriend(UserManager um,Message me){
		this.me = me;
		this.um = um;
		result = new Message[2];
		result[0] = new Message();
		result[1] = new Message();
	}

	public void handle(){
		System.out.println(me.sendAccount+"  "+me.receiveAccount);
		um.removeFriend(me.sendAccount,me.receiveAccount);
		result[0].ui = um.getUser(me.sendAccount);
		result[1].ui = um.getUser(me.receiveAccount);
		result[0].data = "成功移除"+result[1].ui.getName();
		result[1].data = "成功移除"+result[0].ui.getName();

		if(um.userIsOnline(me.sendAccount)){
			result[0].ipAddress = um.getUserIpAddress(me.sendAccount);
			result[0].isSend = true;
		}else
			result[0].isSend = false;

		if(um.userIsOnline(me.receiveAccount)){
			result[1].ipAddress = um.getUserIpAddress(me.receiveAccount);
			result[1].isSend = true;
		}else
			result[1].isSend = false;
	}
	public Message[] handleResult(){
		result[0].type = 9;
		result[1].type = 9;
		return result;
	}
}

class CreateNewGroup{				//创建组
	Message me;
	GroupManager gm;
	UserManager um;
	Message result;
	public CreateNewGroup(UserManager um,GroupManager gm,Message me){
		this.me = me;
		this.gm = gm;
		this.um = um;
		result = new Message();
	}
	public String newAccount(){
		File f = new File("groupAccount");
		long account = 10000L;
		try{
			if(!f.exists())
				f.createNewFile();
			DataInputStream dis = new DataInputStream(new FileInputStream(f));

			if(f.length() == 0){
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
				dos.writeLong(10000L);
				dos.flush();
				dos.close();
			}

			account = dis.readLong();
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
			dos.writeLong(account+1);
			dos.flush();
			dos.close();
			dis.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return Long.toString(account);
		
	}
	public void handle(){
		String account = this.newAccount();
		System.out.println("account: "+account+" "+me.groupName);
		gm.addGroup(account,me.groupName);
		gm.addMember(account,me.sendAccount,me.name);
		result.al = gm.memberList(account);
		result.receiveAccount = me.sendAccount;
		result.data = "群 "+me.groupName+"成功创建！！！";
		um.addGroup(me.sendAccount,account,me.groupName);
		um.setGroupMember(me.sendAccount,account,result.al);
		result.ui = um.getUser(me.sendAccount);
		System.out.println(result.receiveAccount);
		if(um.userIsOnline(me.sendAccount)){
			result.ipAddress = um.getUserIpAddress(me.sendAccount);
			result.isSend = true;
		}else
			result.isSend = false;
	}
	public Message handleResult(){
		result.type = 10;
		return result;
	}

}

class RemoveGroupMember{		//移除群成员
	Message me;
	GroupManager gm;
	UserManager um;
	Message result;

	public RemoveGroupMember(UserManager um,GroupManager gm,Message me){
		this.me = me;
		this.gm = gm;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		String manager = gm.getManagerAccount(me.groupAccount);
		if(manager.equals(me.sendAccount)){
			gm.removeMember(me.groupAccount,me.receiveAccount);
			result.data = "成功删除群成员！！！";
			result.receiveAccount = me.sendAccount;
			result.al = gm.memberList(me.groupAccount);
			um.setGroupMember(me.receiveAccount,me.groupAccount,result.al);
			if(um.userIsOnline(me.sendAccount)){
				result.ipAddress = um.getUserIpAddress(me.sendAccount);
				result.isSend = true;
			}else
				result.isSend = false;
		}
	}
	public Message handleResult(){
		result.type = 11;
		return result;
	}
}

class SendMessageToFriend{			//向好友发送消息
	Message me;
	UserManager um;
	Message result;

	public SendMessageToFriend(UserManager um,Message me){
		this.me = me;
		this.um = um;
		result = new Message();
	}

	public void handle(){
		result.receiveAccount = me.receiveAccount;
		result.sendAccount = me.sendAccount;
		result.name = me.name;
		result.ipAddress = um.getUserIpAddress(me.receiveAccount);
		result.data = me.data;

		if(um.userIsOnline(me.receiveAccount)){
			result.isSend = true;
		}else{
			result.isSend = false;
		}
	}
	public Message handleResult(){
		result.type = 12;
		return result;
	}
}

class SendMessageToGroup{			//向所有群成员发送消息
	Message me;
	final int MAX_NUM = 1024;
	GroupManager gm;
	UserManager um;
	ArrayList<String> al;
	Iterator<String> iter;
	Message[] result = new Message[MAX_NUM];

	public SendMessageToGroup(UserManager um,GroupManager gm,Message me){
		this.me = me;
		this.gm = gm;
		this.um = um;
		al = gm.memberList(me.groupAccount);
	}

	public void handle(){
		iter = al.iterator();
		String t;
		int i = 0;

		while(iter.hasNext()){
			t = iter.next();
			System.out.println("account: "+t);
			result[i] = new Message();
			result[i].sendAccount = me.sendAccount;
			result[i].receiveAccount = t;
			result[i].name = me.name;
			result[i].groupAccount = me.groupAccount;
			result[i].groupName = me.groupName;
			result[i].ipAddress = um.getUserIpAddress(t);
			result[i].data = me.data;

			if(um.userIsOnline(t)){
				result[i].isSend = true;
			}else{
				result[i].isSend = false;
			}
			result[i].type = 13;
			i++;
		}
	}
	public Message[] handleResult(){
		return result;
	}
}
