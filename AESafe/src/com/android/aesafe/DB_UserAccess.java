package com.android.aesafe;


public class DB_UserAccess {
 
    //private variables
    int _id;
    String _user;
    String _language;
    String _password;
 
    // Empty constructor
    public DB_UserAccess(){ }
    
    // constructor
    public DB_UserAccess(int id, String user, String pwd){
        this._id = id;
        this._user = user;
        this._password = pwd;
    }
 
    // constructor
    public DB_UserAccess(String user, String pwd){
        this._user = user;
        this._password = pwd;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
 
    // getting name
    public String getUser(){
        return this._user;
    }
 
    // setting name
    public void setUser(String user){
        this._user = user;
    }
 
    // getting pwd
    public String getPassWord(){
        return this._password;
    }
 
    // setting pwd
    public void setPassWord(String pwd){
        this._password = pwd;
    }
    
    
}