#!/bin/bash
reset
s=`javac -d . UserInfo.java`
if [ -z $s ]
then
	echo "UserInfo.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . GroupInfo.java`

if [ -z $s ]
then
	echo "GroupInfo.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . GroupManager.java`
if [ -z $s ]
then
	echo "GroupManager.java 编译成功"
else
	echo "$s"
fi
echo `javac -d . UserManager.java`
if [ -z $s ]
then
	echo "UserManagerInfo.java 编译成功"
else
	echo "$s"
fi


echo `javac -d . Message.java`
if [ -z $s ]
then
	echo "Message.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . MessageLinkedList.java`
if [ -z $s ]
then
	echo "MessageLinkedList.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . ReadAndSaveInfo.java`
if [ -z $s ]
then
	echo "ReadAndSaveInfo.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . ReceivePool.java`
if [ -z $s ]
then
	echo "ReceivePool.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . SendPool.java`
if [ -z $s ]
then
	echo "SendPool.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . CreateNewAccount.java`
if [ -z $s ]
then
	echo "CreateNewAccount.java 编译成功"
else
	echo "$s"
fi

echo `javac -d . HandleMessage.java`
if [ -z $s ]
then
	echo "HandleMessage.java 编译成功"
else
	echo "$s"
fi
echo `javac -d . Exit.java`
if [ -z $s ]
then
	echo "Exit.java 编译成功"
else
	echo "$s"
fi

echo `javac ChatServer.java`
if [ -z $s ]
then
	echo "ChatServer.java 编译成功"
else
	echo "$s"
fi
