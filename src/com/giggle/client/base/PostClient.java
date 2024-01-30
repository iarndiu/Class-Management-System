package com.giggle.client.base;

import com.giggle.Global;
import com.giggle.bean.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class PostClient {
    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;

    static {
        try {
            socket = new Socket(Global.HOST, Global.INFO_PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册账号
     *
     * @param account  账号
     * @param username 用户名
     * @param password 密码
     * @return 注册结果
     */
    public synchronized static int register(String account, String username, String password) {
        try {
            outputStream.writeInt(Global.REGISTER);
            outputStream.writeUTF(account);      // 账号
            outputStream.writeUTF(username);       // 用户名
            outputStream.writeUTF(password);          // 密码
            //=============================
            outputStream.writeInt(Global.DIARY);
            outputStream.writeUTF("undefined ");
            outputStream.writeUTF(account + " register");

            return inputStream.readInt();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -128;
    }

    /**
     * 登录信息服务器
     *
     * @param account  账号
     * @param password 密码
     * @return 登录结果
     */
    public synchronized static int login(String account, String password) {
        try {
            outputStream.writeInt(Global.LOGIN);
            outputStream.writeUTF(account);
            outputStream.writeUTF(password);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -128;
    }

    /**
     * 将用户设置为管理员或普通用户，只有系统用户（type=-1）可以进行设置
     *
     * @param account 要设置的用户账号
     * @param type    类型 0 普通用户 1 管理员
     * @return 设置结果
     */
    public synchronized static int setAdmin(String account, int type) {
        try {
            outputStream.writeInt(Global.SET_ADMIN);
            outputStream.writeUTF(account);
            outputStream.writeInt(type);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -128;
    }

    /**
     * 添加好友
     *
     * @param account 要添加的好友账号
     * @return 添加结果
     */
    public synchronized static int addFriend(String account) {
        try {
            outputStream.writeInt(Global.ADD_FRIEND);
            outputStream.writeUTF(account);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -128;
    }

    /**
     * 获取好友列表
     *
     * @return 链表，包含所有好友id
     */
    public synchronized static List<String> getFriendList() {
        try {
            outputStream.writeInt(Global.FRIEND_LIST);
            var size = inputStream.readInt();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                list.add(inputStream.readUTF());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取用户类型
     *
     * @param account 要获取的账号
     * @return 用户类型
     */
    public synchronized static int userType(String account) {
        try {
            outputStream.writeInt(Global.IS_ADMIN);
            outputStream.writeUTF(account);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public synchronized static String password(String account) {
        try {
            outputStream.writeInt(Global.PASSWORD);
            outputStream.writeUTF(account);
            return inputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    //get avatar
    public synchronized static String getUserAvatar(String account) {
        try {
            outputStream.writeInt(Global.AVATAR);
            outputStream.writeUTF(account);
            return inputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    public synchronized static int userCount() {
        try {
            outputStream.writeInt(Global.USER_COUNT);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 发布公告
     *
     * @param content 公告内容
     * @return 发布结果
     */
    public synchronized static int postAnnouncement(String content) {
        try {
            outputStream.writeInt(Global.POST_ANNOUNCEMENT);
            outputStream.writeUTF(content);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -128;
    }

    /**
     * 获取公告列表
     *
     * @return 链表，包含所有公告id，默认按时间顺序排列
     */
    public synchronized static List<Integer> getAnnouncementList() {
        try {
            outputStream.writeInt(Global.ANNOUNCEMENT_LIST);
            var size = inputStream.readInt();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                list.add(inputStream.readInt());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取用户名
     *
     * @param account 用户账户
     * @return 用户名，不存在时为undefined
     */
    public synchronized static String getUserName(String account) {
        try {
            outputStream.writeInt(Global.USERNAME);
            outputStream.writeUTF(account);
            return inputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "undefined";
    }

    /**
     * 获取公告的具体信息
     *
     * @param id 公告id
     * @return 0: 发送者id，1：公告内容，2：发布时间戳，3：发送者用户名，4.jpg：将时间戳设置为LocalDateTime的结果
     */
    public synchronized static ArrayList getAnnouncement(int id) {
        try {
            outputStream.writeInt(Global.GET_ANNOUNCEMENT);
            outputStream.writeInt(id);
            var sender = inputStream.readUTF();
            var content = inputStream.readUTF();
            var time = inputStream.readLong();
            var list = new ArrayList<>();
            list.add(sender);
            list.add(content);
            list.add(time);
            list.add(getUserName(sender));
            list.add(LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8)));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    /**
     * 发布投票
     *
     * @param content 投票内容
     * @return 结果
     */
    public synchronized static int postVote(String content) {
        try {
            outputStream.writeInt(Global.POST_VOTE);
            outputStream.writeUTF(content);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -128;
    }

    /**
     * 添加建议
     *
     * @param voteId 投票编号 content 投票内容
     * @return 结果
     */
//    =============================================
    public synchronized static int addAdvice(int voteId, String content) {
        try {
            outputStream.writeInt(Global.ADD_ADVICE);
            outputStream.writeInt(voteId);
            outputStream.writeUTF(content);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -128;
    }

    //获取建议=======================================
    public synchronized static String getAdvice(int voteId) {
        try {
            outputStream.writeInt(Global.GET_ADVICE);
            outputStream.writeInt(voteId);
            return inputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "no advice";
    }

    /**
     * 获取投票列表
     *
     * @return 链表，包含所有投票id
     */
    public synchronized static List<Integer> getVoteList() {
        try {
            outputStream.writeInt(Global.VOTE_LIST);
            var size = inputStream.readInt();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                list.add(inputStream.readInt());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 进行投票
     *
     * @param voteId 投票id
     * @param item   选择哪个选项 0为同意，1为不同意
     * @return
     */
    public synchronized static int vote(int voteId, int item) {
        try {
            outputStream.writeInt(Global.VOTE);
            outputStream.writeInt(voteId);
            outputStream.writeInt(item);
            return inputStream.readInt();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -128;
    }

    /**
     * 获取投票具体信息
     *
     * @param voteId 投票id
     * @return 0：发送者id，1：投票内容，2：投票时间戳，3：投票状态，4.jpg：同意人数，5：不同意人数，6：发送者用户名，7：投票时间
     * 投票状态：0：正在进行，1：当前登录用户已经进行投票，2：投票已经结束，发布投票24小时后自动结束。
     */
    public synchronized static ArrayList getVote(int voteId) {
        try {
            outputStream.writeInt(Global.GET_VOTE);
            outputStream.writeInt(voteId);
            var sender = inputStream.readUTF();
            var text = inputStream.readUTF();
            var time = inputStream.readLong();
            var status = inputStream.readInt();
            var agree = inputStream.readInt();
            var disagree = inputStream.readInt();
            var list = new ArrayList<>();
            list.add(sender);
            list.add(text);
            list.add(time);
            list.add(status);
            list.add(agree);
            list.add(disagree);
            list.add(getUserName(sender));
            list.add(LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8)));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    //获取投票状态
    public synchronized static int getVoteStatus(int voteId) {
        return (int) getVote(voteId).get(3);
    }

    /**
     * 获取文件信息
     *
     * @param fileId 文件id
     * @return 0: 上传者id，1：文件名，2：文件长度，3：上传时间戳，4.jpg：上传者用户名，5：上传时间LocalDateTime
     */
    public synchronized static ArrayList getFileInfo(int fileId) {
        try {
            outputStream.writeInt(Global.FILE_INFO);
            outputStream.writeInt(fileId);
            var uploader = inputStream.readUTF();        // 上传者
            var name = inputStream.readUTF();        // 文件名
            var length = inputStream.readLong();     // 文件长度
            var time = inputStream.readLong();       // 上传时间
            var list = new ArrayList<>();
            list.add(uploader);
            list.add(name);
            list.add(length);
            list.add(time);
            list.add(getUserName(uploader));
            list.add(LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8)));
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList();
    }

    /**
     * 得到群文件列表
     *
     * @return 所有群文件id
     */
    public synchronized static List<Integer> getFileList() {
        try {
            outputStream.writeInt(Global.FILE_LIST);
            var size = inputStream.readInt();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                list.add(inputStream.readInt());
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取最新消息
     *
     * @param sender 发送者/目标角色的账号
     * @param type   消息类型
     * @return 消息列表
     */
    public synchronized static List<Message> getMessageList(String sender, int type) {
        try {
            outputStream.writeInt(Global.MESSAGE_LIST);
            outputStream.writeUTF(type == 0 ? sender : "undefined");
            outputStream.writeInt(type);
            var size = inputStream.readInt();
            List<Message> list = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                var message = new Message();
                message.sender = inputStream.readUTF();
                message.receiver = inputStream.readUTF();
                message.content = inputStream.readUTF();
                message.messageType = inputStream.readInt();
                message.sendTime = LocalDateTime.ofEpochSecond(inputStream.readLong() / 1000, 0, ZoneOffset.ofHours(8));
                list.add(message);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 判断指定目标是否有新消息
     *
     * @param target 目标角色，
     * @param type   消息类型，0为普通消息，1为群消息
     * @return 是否有新消息
     */
    public synchronized static boolean hasNewMessage(String target, int type) {
        try {
            outputStream.writeInt(Global.NEW_MESSAGE);
            outputStream.writeUTF(target);
            outputStream.writeInt(type);
            return inputStream.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public synchronized static String getDiary(String account) {
        try {
            outputStream.writeInt(Global.DIARY);
            outputStream.writeUTF(account);

//            var manager = inputStream.readUTF();
//            var content = inputStream.readUTF();
//            var time = inputStream.readLong();
//            var list = new ArrayList<>();
//            list.add(manager);
//            list.add(content);
//            list.add(time);
//            list.add(getUserName(manager));
//            list.add(LocalDateTime.ofEpochSecond(time / 1000, 0, ZoneOffset.ofHours(8)));
//            return list;
            return inputStream.readUTF();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        return new ArrayList();
//            return inputStream.readUTF();
        return null;
    }

}



