/*
 *消息接收和发送的缓冲区
 */
package server.my.message;
import server.my.message.Message;
import server.my.user.UserManager;
import java.util.Deque;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;

public class MessageLinkedList{
	private int num = 1000;		//最大消息数目（未用）
	private Deque<Message> ml;
	private Deque<Message> md;
	private UserManager um;
	private ArrayList<Message> offline;
	
	public MessageLinkedList(UserManager um){
		ml = new LinkedList<Message>();
		md = new LinkedList<Message>();
		offline = new ArrayList<Message>();
		this.um = um;
	}

	public synchronized void push(Message me){		//向接收消息缓冲区添加消息
		ml.offer(me);
	}

	public synchronized Message pop(){		//从接收消息缓冲区获取消息
		return ml.poll();
	}

	public synchronized boolean isEmpty(){		//接收消息缓冲区是否为空
		if(ml.size() > 0)
			return false;
		else
			return true;
	}

	public synchronized void pushe(Message me){	//向消息发送缓冲区添加消息
		md.offer(me);
	}

	public synchronized Message  pope(){		//从消息发送缓冲区获取消息
		return md.poll();
	}

	public synchronized boolean isEmptye(){		//消息缓冲区是否为空
		if(md.size() > 0)
			return false;
		else
			return true;
	}

	public synchronized void pusho(Message me){	//保存发向接收者未上线的信息
		offline.add(me);
	}

	public synchronized void popo(String account){	//接收者上线信息加入发送缓冲区
		for(int i = 0;i < offline.size();i++){
			if(offline.get(i).receiveAccount == account){
				this.pushe(offline.get(i));
				offline.remove(i);
			}
		}
	}
}
