/*
 * 创建新的用户帐号,帐号从10000开始
 */
package server.my.handleMessage;
import java.io.*;
import server.my.user.UserManager;

class ReadAccount{
	private final long accountNum = 10000;
	private long ta;
	File f;

	public ReadAccount(boolean b){
		;//什么都不做只是防止调用构造函数ReadAccount()
		//达到可以读取最大帐号的目的
	}
	public ReadAccount(){
		try{
			f = new File("Account");
			if(!f.exists()){
				f.createNewFile();
			}
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			if(f.length() == 0){
				ta = accountNum;
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
				dos.writeLong(ta);
				dos.flush();
				dos.close();
			}else{
				ta = dis.readLong();
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
				ta++;
				dos.writeLong(ta);
				dos.flush();
				dos.close();
			}
			dis.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public long onlyRead(){
		try{
			f = new File("Account");
			if(!f.exists()){
				f.createNewFile();
			}
			DataInputStream dis = new DataInputStream(new FileInputStream(f));
			if(f.length() == 0){
				ta = accountNum;
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
				dos.writeLong(ta);
				dos.flush();
				dos.close();
			}else{
				ta = dis.readLong();
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(f));
				dos.writeLong(ta);
				dos.flush();
				dos.close();
			}
			dis.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		return this.ta;

	}
	public String getNewAccount(){
		return Long.toString(this.ta);
	}
		
}
public class CreateNewAccount{
	private String account;
	public CreateNewAccount(boolean b){
		;//同ReadAccount类中的构造函数作用相同
	}
	public CreateNewAccount(){
		ReadAccount ra = new ReadAccount();
		account = ra.getNewAccount();
	}
	public void signIn(UserManager um,String account,String name,String password){
		um.addUser(account,password,name);
	}

	public String getAccount(){
		return this.account;
	}
	public long getMaxAccount(){
		ReadAccount ra = new ReadAccount(true);
		return ra.onlyRead();
	}
}
