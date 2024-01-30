package com.giggle.server.servers;

import com.giggle.Global;
import com.giggle.server.system.ChatSystem;
import com.giggle.server.system.ClassSystem;
import com.giggle.server.system.FileSystem;
import com.giggle.server.system.UserSystem;
//import javafx.scene.media.MediaMarkerEvent;

import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;

public class PostClientHandler extends ClientHandler {
    private String account = "nullptr";
    private String username = "nullptr";

    PostClientHandler(Socket socket, AbstractServer server) {
        super(socket, server);
    }

    @Override
    public void run() {
        try {
            while (true) {
                var code = inputStream.readInt();
                switch (code) {
                    case Global.LOGIN:
                        login();
                        break;
                    case Global.REGISTER:
                        register();
                        break;
                    case Global.USERNAME:
                        getUsername();
                        break;
                    case Global.POST_ANNOUNCEMENT:
                        postAnnouncement();
                        break;
                    case Global.ANNOUNCEMENT_LIST:
                        getAnnouncementList();
                        break;
                    case Global.FILE_INFO:
                        getFileInfo();
                        break;
                    case Global.FILE_LIST:
                        getFileList();
                        break;
                    case Global.GET_ANNOUNCEMENT:
                        getAnnouncement();
                        break;
                    case Global.GET_VOTE:
                        getVote();
                        break;
                    case Global.POST_VOTE:
                        postVote();
                        break;
                    case Global.VOTE_LIST:
                        getVoteList();
                        break;
                    case Global.SET_ADMIN:
                        setAdmin();
                        break;
                    case Global.FRIEND_LIST:
                        getFriendList();
                        break;
                    case Global.ADD_FRIEND:
                        addFriend();
                        break;
                    case Global.VOTE:
                        vote();
                        break;
                    case Global.MESSAGE_LIST:
                        getMessageList();
                        break;
                    case Global.NEW_MESSAGE:
                        hasNewMessage();
                        break;
                    case Global.IS_ADMIN:
                        isAdmin();
                        break;
                    case Global.USER_COUNT:
                        userCount();
                        break;
                    case Global.PASSWORD:
                        password();
                        break;
                    case Global.AVATAR:
                        avatar();
                        break;
                    case Global.ADD_ADVICE:
                        addAdvice();
                        break;
                    case Global.GET_ADVICE:
                        getAdvice();
                        break;
                    case Global.DIARY:
                        getDiary();
                        break;

                }
            }
        } catch (Exception e) {
            server.remove(this);
        }
    }

    private void isAdmin() throws IOException {
        var account = inputStream.readUTF();
        outputStream.writeInt(UserSystem.userType(account));
    }

    private void hasNewMessage() throws IOException {
        var target = inputStream.readUTF();
        var type = inputStream.readInt();
        outputStream.writeBoolean(ChatSystem.hasNewMessage(account, target, type));
    }

    private void getMessageList() throws IOException {
        var sender = inputStream.readUTF();
        var type = inputStream.readInt();
        var list = ChatSystem.getMessageList(account, sender, type);
        outputStream.writeInt(list.size());
        for (var i : list) {
            outputStream.writeUTF(i.sender);
            outputStream.writeUTF(i.receiver);
            outputStream.writeUTF(i.content);
            outputStream.writeInt(i.messageType);
            outputStream.writeLong(Timestamp.valueOf(i.sendTime).getTime());
        }
    }

    private void getFileList() throws IOException {
        var list = FileSystem.getFileList();
        outputStream.writeInt(list.size());
        for (var i : list) {
            outputStream.writeInt(i);
        }
    }

    private void getFileInfo() throws IOException {
        var fileId = inputStream.readInt();
        var info = FileSystem.getFileInfo(fileId);
        outputStream.writeUTF((String) info.get(0));
        outputStream.writeUTF((String) info.get(1));

        outputStream.writeLong((Long) info.get(2));
        outputStream.writeLong((Long) info.get(3));
    }

    private void vote() throws IOException {
        var voteId = inputStream.readInt();
        var item = inputStream.readInt();
        var code = ClassSystem.vote(account, voteId, item);
        outputStream.writeInt(code);
    }

    private void getVote() throws IOException {
        var voteId = inputStream.readInt();
        var list = ClassSystem.getVote(account, voteId);
        outputStream.writeUTF((String) list.get(0));
        outputStream.writeUTF((String) list.get(1));
        outputStream.writeLong((Long) list.get(2));
        outputStream.writeInt((Integer) list.get(3));
        outputStream.writeInt((Integer) list.get(4));
        outputStream.writeInt((Integer) list.get(5));
    }

    private void getVoteList() throws IOException {
        var list = ClassSystem.getVoteList();
        outputStream.writeInt(list.size());
        for (var i : list) {
            outputStream.writeInt(i);
        }
    }

    private void postVote() throws IOException {
        var text = inputStream.readUTF();

        //++++++++++++++++++++++++++++++++
        ClassSystem.newDiary(account, (account + "posted a vote"));

        var code = ClassSystem.postVote(account, text);
        outputStream.writeInt(code);
    }

    //    ======================================
    private void addAdvice() throws IOException {
        var voteId = inputStream.readInt();
        var text = inputStream.readUTF();
        var code = ClassSystem.addAdvice(voteId, text);
        outputStream.writeInt(code);
    }

    private void getAdvice() throws IOException {
        var voteId = inputStream.readInt();
        var advice = ClassSystem.getAdvice(voteId);
        outputStream.writeUTF(advice);
    }


    private void getAnnouncement() throws IOException {
        var annId = inputStream.readInt();
        var list = ClassSystem.getAnnouncement(annId);
        outputStream.writeUTF((String) list.get(0));
        outputStream.writeUTF((String) list.get(1));
        outputStream.writeLong((Long) list.get(2));

    }

    private void getAnnouncementList() throws IOException {
        var list = ClassSystem.getAnnouncementList();
        outputStream.writeInt(list.size());
        for (var i : list) {
            outputStream.writeInt(i);
        }
    }

    private void postAnnouncement() throws IOException {
        var text = inputStream.readUTF();

        //++++++++++++++++++++++++++++++++
        ClassSystem.newDiary(account, (account + "posted an announcement"));

        var code = ClassSystem.postAnnouncement(account, text);
        outputStream.writeInt(code);
    }

    private void addFriend() throws IOException {
        var friendId = inputStream.readUTF();
        var code = UserSystem.addFriend(account, friendId);
        outputStream.writeInt(code);
    }

    private void getFriendList() throws IOException {
        var list = UserSystem.getFriendList(account);
        outputStream.writeInt(list.size());
        for (var i : list) {
            outputStream.writeUTF(i);
        }
    }

    private void setAdmin() throws IOException {
        var userId = inputStream.readUTF();
        var type = inputStream.readInt();

        //++++++++++++++++++++++++++++++++
        ClassSystem.newDiary(account, (account + "changed privilege"));

        var result = UserSystem.setAdmin(account, userId, type);
        outputStream.writeInt(result);
    }

    private void getUsername() throws IOException {
        var userId = inputStream.readUTF();
        var result = UserSystem.username(userId);
        outputStream.writeUTF(result);
    }

    private void register() throws IOException {
        var account = inputStream.readUTF();
        var username = inputStream.readUTF();
        var password = inputStream.readUTF();
        var code = UserSystem.register(account, username, password);
        outputStream.writeInt(code);
    }

    private void login() throws IOException {
        var account = inputStream.readUTF();
        var password = inputStream.readUTF();

        //+++++++++++++++++++++++++++++++
        ClassSystem.newDiary(account, (account+" login"));

        var code = UserSystem.login(account, password);
        if (code >= 0) {
            // login success
            this.account = account;
            this.username = UserSystem.username(account);
        }
        outputStream.writeInt(code);
    }

    private void userCount() throws IOException {
        var count = UserSystem.userCount();
        outputStream.writeInt(count);
    }

    private void password() throws IOException {
        var account = inputStream.readUTF();
        outputStream.writeUTF(UserSystem.password(account));
    }

    //新写的
    private void avatar() throws IOException {
        var account = inputStream.readUTF();
        outputStream.writeUTF(UserSystem.avatar(account));
    }

    //++++++++++++++++++++++++++++++
    private void getDiary() throws IOException{
        System.out.println("5555");
        var account = inputStream.readUTF();
        System.out.println("6666");
        var diary = ClassSystem.getDiary(account);
        System.out.println("7777");
        outputStream.writeUTF(diary);
        System.out.println("8888");
    }

//    private void getDiary() throws IOException {
//        var account = inputStream.readUTF();
//        var list = ClassSystem.getDiary(account);
//        outputStream.writeUTF((String) list.get(0));
//        outputStream.writeUTF((String) list.get(1));
//        outputStream.writeLong((Long) list.get(2));
//    }
}