/*
 *UserManager.java管理所有用户的信息
 */
package server.my.user;
import server.my.user.UserInfo;
import server.my.user.UserManager;
import server.my.group.GroupManager;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Runnable;

public class UserManager implements Serializable{
	private Map<String,UserInfo> users = new HashMap<String,UserInfo>();		//服务端运行时保存所有已注册用户信息
	
	public synchronized void addUser(String account,String password,String name){	//添加注册用户
		users.put(account,new UserInfo(account,name,password));
	}
	public synchronized UserInfo getUser(String account){				//获取用户所有信息
		if(!users.containsKey(account))
			return null;
		return users.get(account);
	}

	public synchronized void removeUser(String account){				//移除用户（从未提供移除注册用户功能）
		users.remove(account);
	}

	public synchronized int usersNum(){						//返回用户数
		return users.size();
	}

	public synchronized boolean switchUserState(String account){			//调整用户是否在线
		if(!users.containsKey(account)){
			System.out.println("用户不存在");
			return false;
		}

		UserInfo ui = users.get(account);
		ui.setIsOnline();
		return true;
	}
	public synchronized void setGroupMember(String userAccount,String groupAccount,ArrayList<String> gl){	//向用户信息中添加用户加入的群
		System.out.println("group: "+userAccount);
		UserInfo ui = users.get(userAccount);
		ui.setGroupMember(groupAccount,gl);
	}

	public synchronized ArrayList<String> getGroupMember(String userAccount,String groupAccount){ //获取用户信息中的群信息
		return users.get(userAccount).getGroupMember(groupAccount);
	}
	public synchronized boolean userIsOnline(String account){ //判断用户是否在线
		if(!users.containsKey(account))
			return false;

		return users.get(account).getIsOnline();
	}
	public synchronized boolean modifyUserPassword(String account,String password){ //修改用户密码（功能未提供）
		if(!users.containsKey(account))
			return false;
		UserInfo ui = users.get(account);
		ui.setPassword(password);
		return true;
	}

	public synchronized String getUserPassword(String account){ //获取用户密码
		UserInfo ui = users.get(account);
		if(ui == null)
			return null;
		else
			return ui.getPassword();
	}
	public synchronized boolean modifyUserName(String account,String name){ //修改用户昵称（功能未提供）
		if(!users.containsKey(account))
			return false;

		UserInfo ui = users.get(account);
		ui.setName(name);
		return true;
	}
	public synchronized String getUserName(String account){ //获取用户昵称
		return users.get(account).getName();
	}
	public synchronized boolean addFriend(String account_1,String account_2){ 	//用户添加好友
		if(!users.containsKey(account_1) || !users.containsKey(account_2))
			return false;

		UserInfo u1 = users.get(account_1);
		UserInfo u2 = users.get(account_2);

		u1.addFriend(account_2,u2.getName());
		u2.addFriend(account_1,u1.getName());
		
		return true;
	}

	public synchronized boolean removeFriend(String account_1,String account_2){ //用户移除好友
		if(!users.containsKey(account_1) || !users.containsKey(account_2))
			return false;

		UserInfo u1 = users.get(account_1);
		UserInfo u2 = users.get(account_2);

		if(!u1.containFriend(account_2) || !u2.containFriend(account_1))
			return false;

		u1.removeFriend(account_2);
		u2.removeFriend(account_1);
		
		return true;
	}

	public synchronized boolean addGroup(String account,String groupAccount,String groupName){ //用户加入一个群
		if(!users.containsKey(account))
			return false;

		UserInfo u1 = users.get(account);

		if(u1.containGroup(groupAccount))
			return false;
		u1.addGroup(groupAccount,groupName);

		return true;
	}

	public synchronized boolean removeGroup(String account,String groupAccount){ //用户退出群
		if(!users.containsKey(account))
			return false;

		UserInfo ui = users.get(account);
		if(!ui.containGroup(groupAccount))
			return false;

		ui.removeGroup(groupAccount);
		return true;
	}

	public synchronized boolean setUserIpAddress(String account,String ipAddress){ //设置用户ip地址
		if(!users.containsKey(account))
			return false;

		UserInfo ui = users.get(account);
		ui.setIpAddress(ipAddress);
		return false;
	}

	public synchronized String getUserIpAddress(String account){
		if(!users.containsKey(account))
			return null;

		UserInfo ui = users.get(account);
		return ui.getIpAddress();
	}
	public synchronized int userSize(){
		return users.size();
	}
	
}
