/*
 * GroupManager.java 管理所有已注册的群
*/
package server.my.group;
import java.util.Map;
import java.io.*;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import server.my.group.GroupInfo;

public class GroupManager implements Serializable{
	private Map<String,GroupInfo> groups = new HashMap<String,GroupInfo>();		//服务端运行时保存所有群信息

	public synchronized boolean addGroup(String groupAccount,String groupName){ 	//创建新的群
		if(groups.containsKey(groupAccount))
			return false;
		GroupInfo gi = new GroupInfo(groupAccount,groupName);
		groups.put(groupAccount,gi);
		return true;
	}

	public synchronized boolean removeGroup(String account){		//移除已创建的群
		if(!groups.containsKey(account))
			return false;

		groups.remove(account);
		return true;
	}

	public synchronized boolean addMember(String groupAccount,String memberAccount,String name){ //向群中添加成员
		if(!groups.containsKey(groupAccount)){
			System.out.println(groupAccount+": addMember failed");
			return false;
		}

		GroupInfo gi = groups.get(groupAccount);
		gi.addMember(memberAccount,name);
		groups.put(groupAccount,gi);
		return true;
	}

	public synchronized boolean removeMember(String groupAccount,String memberAccount){ //移除群成员
		if(!groups.containsKey(groupAccount))
			return false;

		GroupInfo gi = groups.get(groupAccount);
		if(!gi.containMember(memberAccount))
			return false;

		gi.removeMember(memberAccount);
		return true;
	}

	public synchronized ArrayList<String> memberList(String groupAccount){		//返回群成员列表
		ArrayList<String> al = groups.get(groupAccount).getAllMember();

		return al;
	}
	public synchronized String getManagerAccount(String groupAccount){		//返回管理员帐号
		GroupInfo gi = groups.get(groupAccount);
		return gi.managerAccount();
	}

	public synchronized String getGroupName(String groupAccount){			//返回群成员
		return groups.get(groupAccount).getName();
	}
	public synchronized int groupSize(){
		return this.groups.size();
	}
}
