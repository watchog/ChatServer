/*
 * Message.java用于控制客户端与服务端之间的消息传递
 */
package server.my.message;
import server.my.user.UserInfo;
import java.util.ArrayList;
import java.io.*;

public class Message implements Serializable{
	private static final long serialVersionUID = 1L;
	public Message(){
		this.acknowledge = false;
		this.isSend = false;
	}
	public String sendAccount;		//发送用户帐号
	public String receiveAccount;		//接收用户帐号
	public String name;			//发送用户名称
	public String password;			//用户密码
	public String groupName;		//群名称
	public String groupAccount;		//群帐号
	public String ipAddress;		//ip地址
	public boolean acknowledge;		//确认字段
	public boolean isSend;			//是否发送
	public int type;			//消息类型
	public UserInfo ui;			//用户信息
	public String data;			//消息内容
	public ArrayList<String> al;		//好友列表或群成员列表
}
/*
 * type 值含义
 * （以下信息过期具体看HandleMessage.java）
0.注销
1.登录
3.注册
4.修改名称
5.请求加群
5.允许加群
6.删群
7.请求加好友
8.允许加好友
8.删好友
9.创建群
10.群删除成员
11.发送好友消息
12.发群消息
13.信息修改确认
*/
