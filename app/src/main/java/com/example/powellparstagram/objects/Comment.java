package com.example.powellparstagram.objects;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends ParseObject {

    public static final String KEY_COMMENT_TEXT = "commentText";
    public static final String KEY_USER = "commentUser";
    public static final String KEY_POST = "commentPost";
    public static final String KEY_CREATED_AT = "createdAt";

    // GET/SET Text
    public String getCommentText(){
        return getString(KEY_COMMENT_TEXT);
    }

    public void setCommentText(String string){
        put(KEY_COMMENT_TEXT, string);
    }


    // GET/SET User
    public ParseUser getCommentUser(){
        return getParseUser(KEY_USER);
    }

    public void setCommentUser(ParseUser parseUser){
        put(KEY_USER, parseUser);
    }


    // GET/SET Post
    public ParseObject getCommentPost(){
        return getParseObject(KEY_POST);
    }

    public void setCommentPost(Post post){
        put(KEY_POST, post);
    }

}
