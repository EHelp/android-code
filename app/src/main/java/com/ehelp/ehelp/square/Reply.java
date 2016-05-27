package com.ehelp.ehelp.square;

import com.ehelp.ehelp.R;

import org.json.JSONObject;

/**
 * Created by UWTH on 2015/10/25.
 */
public class Reply {
    /*“answer_id”:int         回答的id，唯一标识
“event_id”:int          回答所属提问事件的Id
“author_id”:int          回答者的用户Id
“author”:string           回答作者的用户名
“content”:string           回答的内容
“time”:string              回答的更新时间
“is_adopted”:int           回答是否被采纳为最佳
“liking_num”:int           暂时忽略
*/
    private int head;
    private int answer_id;
    private int author_id;
    private int event_id;
    private int is_adopted;
    private int liking_num;
    private String author;
    private String time;
    private String content;

    public Reply(JSONObject jsonObject) {
        head = R.mipmap.head;
        try {
            answer_id = jsonObject.getInt("answer_id");
            author_id = jsonObject.getInt("author_id");
            event_id = jsonObject.getInt("event_id");
            is_adopted = jsonObject.getInt("is_adopted");
            liking_num = jsonObject.getInt("liking_num");
            author = jsonObject.getString("author");
            time = jsonObject.getString("time");
            content = jsonObject.getString("content");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getHead() {
        return head;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getIs_adopted() {
        return is_adopted;
    }

    public void setIs_adopted(int is_adopted) {
        this.is_adopted = is_adopted;
    }

    public int getLiking_num() {
        return liking_num;
    }

    public void setLiking_num(int liking_num) {
        this.liking_num = liking_num;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
