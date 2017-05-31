/*
 * GroupInfo.java保存一个群的基本信息
 */
package server.my.group;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.lang.Runnable;
import java.lang.Thread;

class Member implements Serializable{			//群成员信息
	private String account;				//群成员帐号
	private String name;				//群成员昵称
	private boolean pro;

	public Member(String account,String name,boolean pro){
		this.account = account;
		this.name = name;
		this.pro = pro;
	}

	public String getAccount(){
		return this.account;
	}
	public boolean getPro(){
		return pro;
	}
	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
}


public class GroupInfo implements Serializable{
	private HashMap<String,Member> member = new HashMap<String,Member>();	//群成员列表
	private String account;							//群帐号
	private String managerAccount;						//群管理员帐号
	private String name;							//群成员
	private int memberNum;
	
	public GroupInfo(String account,String name){
		this.account = account;
		this.name = name;
		this.memberNum = 0;
	}

	public synchronized String getAccount(){
		return this.account;
	}

	public synchronized String getName(){
		return this.name;
	}
	public synchronized void setName(String name){
		this.name = name;
	}

	public synchronized void addMember(String account,String name){
		Member m;
		System.out.println(memberNum);
		if(memberNum == 0){
			m = new Member(account,name,true);
			this.managerAccount = account;
		}else
			m = new Member(account,name,false);
		member.put(account,m);
		this.memberNum++;
	}

	public synchronized void removeMember(String account){
		if(!member.containsKey(account)){
			System.out.println("删除用户不存在");
			return ;
		}

		member.remove(account);
		this.memberNum--;
	}
	public synchronized boolean containMember(String account){
		return member.containsKey(account);
	}
	public synchronized String managerAccount(){
		return this.managerAccount;
	}

	public synchronized ArrayList<String> getAllMember(){
		Set<String> s = member.keySet();
		Iterator<String> iter = s.iterator();
		ArrayList<String> al = new ArrayList<String>();
		String t;

		while(iter.hasNext()){
			t = iter.next();
			al.add(t);
		}
		return al;
	}
}

