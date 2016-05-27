package com.ehelp.ehelp.square;

import com.ehelp.ehelp.R;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by UWTH on 2015/10/24.
 */
public class AskMsg implements Serializable{
    /*
   “event_id”:int,        事件ID，唯一标识
“launcher_id”:int,      发起人用户ID
“launcher”:string,       发起人用户名
“title”:string,           事件名
“content”:string,        事件内容（描述）
“type”:int,             事件类型，0代表提问，1代表求助，2代
                      表求救
“time”:string,           事件发起时间
“last_time”:string,       事件最后更新时间
“longitude”:float,        事件发起地点，经度
“latitude”:float,          同上，纬度
“state”:int              事件状态，0代表进行中，1代表用户
                       取消事件，2代表事件已正常结束
“follow_number”:int,     提问事件中代表点赞数，求助/求救事件
                       暂无意义
“support_num”:int,       提问事件中代表回答数，求助/求救事件
                       代表响应数
“group_pts”:float,         组得分，暂时忽略
“demand_number”:int,     暂时忽略
“love_coin”:int,            事件悬赏的爱心币数量
“comment”:string,          暂时忽略
“location”:string            暂时忽略
“is_like”:int           仅当提问事件才有此字段，0代表当前用户
                     未点赞，1代表已点赞

*/
    private int head;
    private int event_id;
    private String launcher;
    private int launcher_id;
    private int type;
    private String content;
    private String time;
    private String last_time;
    private String title;
    private int state;
    private int follow_number;
    private int support_number;
    private double group_pts;
    private int demand_number;
    private int love_coin;
    private String comment;
    private String location;
    private int is_like;
    private int is_verify;
    private int occupation;
    private double reputation;

    public AskMsg() {

    }

    public AskMsg(HashMap<String, Object> data) {
        this.head = R.mipmap.head;
        this.event_id = (int)data.get("event_id");
        this.launcher = data.get("launcher").toString();
        this.launcher_id = (int)data.get("launcher_id");
        this.type = (int)data.get("type");
        this.content = data.get("content").toString();
        this.time = data.get("time").toString();
        this.last_time = data.get("last_time").toString();
        this.title = data.get("title").toString();
        this.state = (int)data.get("state");
        this.follow_number = (int)data.get("follow_number");
        this.support_number = (int)data.get("support_number");
        this.group_pts = (double)data.get("group_pts");
        this.demand_number = (int)data.get("demand_number");
        this.love_coin = (int)data.get("love_coin");
        this.comment = data.get("comment").toString();
        this.location = data.get("location").toString();
        this.is_like = (int)data.get("is_like");
        this.reputation = (double)data.get("reputation");
        this.is_verify = (int)data.get("is_verify");
        this.occupation = (int)data.get("occupation");
    }

    public boolean getIsLike() {return is_like == 1;}

    public int getLauncher_id() {return launcher_id;}

    public int getType() {return type;}

    public int getState() {return state;}

    public int getFollow_number() {return follow_number;}

    public int getSupport_number() {return support_number;}

    public int getDemand_number() {return demand_number;}

    public int getLove_coin() {return  love_coin;}

    public String getLauncher() {return  launcher;}

    public String getLast_time() {return last_time;}

    public String getComment() {return  comment;}

    public String getLocation() {return  location;}

    public int getEvent_id() {return event_id;}

    public String getContent() {return content;}

    public int getHead() {
        return head;
    }

    public String getName() {
        return launcher;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public int getThumbnum() {
        return follow_number;
    }

    public int getReplynum() {
        return support_number;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public void setName(String name) {
        this.launcher = name;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setThumbnum(int thumbnum) {
        this.follow_number = thumbnum;
    }

    public void setReplynum(int replynum) {
        this.support_number = replynum;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public void setLauncher(String launcher) {
        this.launcher = launcher;
    }

    public void setLauncher_id(int launcher_id) {
        this.launcher_id = launcher_id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLast_time(String last_time) {
        this.last_time = last_time;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setFollow_number(int follow_number) {
        this.follow_number = follow_number;
    }

    public void setSupport_number(int support_number) {
        this.support_number = support_number;
    }

    public double getGroup_pts() {
        return group_pts;
    }

    public void setGroup_pts(double group_pts) {
        this.group_pts = group_pts;
    }

    public void setDemand_number(int demand_number) {
        this.demand_number = demand_number;
    }

    public void setLove_coin(int love_coin) {
        this.love_coin = love_coin;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getIs_verify() {
        return is_verify;
    }

    public void setIs_verify(int is_verify) {
        this.is_verify = is_verify;
    }

    public int getOccupation() {
        return occupation;
    }

    public void setOccupation(int occupation) {
        this.occupation = occupation;
    }

    public double getReputation() {
        return reputation;
    }

    public void setReputation(double reputation) {
        this.reputation = reputation;
    }
}
