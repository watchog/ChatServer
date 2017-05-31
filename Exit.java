package server.my.exit;
import java.util.Set;
import java.util.HashMap;
import java.util.Scanner;
import java.io.*;
import java.lang.Thread;
import java.lang.Runnable;
import server.my.user.UserInfo;
import server.my.user.UserManager;
import server.my.group.GroupManager;
import server.my.handleMessage.CreateNewAccount;
import server.my.file.ReadAndSaveInfo.ReadAndSaveInfo;

public class Exit implements Runnable{
	private UserManager um;
	private GroupManager gm;
	private ReadAndSaveInfo ras;
	private File fileUser,fileGroup,fileAccount;
	private CreateNewAccount cna = new CreateNewAccount(true); //可以只读调用帐号

	public Exit(UserManager um,GroupManager gm){
		this.um = um;
		this.gm = gm;
		fileUser = new File("userData.dat");
		fileGroup = new File("groupData.dat");
		fileAccount = new File("Account");
	}

	public void run(){
		String exit;
		Scanner read = new Scanner(System.in);
		while(true){
			exit = read.nextLine();
			if(exit.equals("quit")){
				if(!fileAccount.exists()){
					System.out.println("没有Account文件");
					System.out.flush();
					System.exit(0);
				}
				long account = cna.getMaxAccount();
				for(int a = 10000;a <= account;a++){
					UserInfo ui = um.getUser(Long.toString(account));
					ui.offLine();
					if(!ui.getIsOnline()){
						System.out.println("帐号："+a+"被强制下线");
					}
				}
				new ReadAndSaveInfo(um);
				new ReadAndSaveInfo(gm);
				System.out.println("服务端关闭。。。");
				System.out.flush();
				System.exit(0);
			}
		}
	}
}
