package com.android.aesafe;


public class DB_Categoria {

    //private variables
    int _id;
    String _language;
    String _descripcion;
    String _icono;
     
    // Empty constructor
    public DB_Categoria(){ }
    
    // constructor
    public DB_Categoria(int id, String lang, String desc){
        this._id = id;
        this._language = lang;        
        this._descripcion = desc;        
    }
 
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
 
    // getting lang
    public String getLanguage(){
        return this._language;
    }
 
    // setting lang
    public void setLanguage(String lang){
        this._language = lang;
    }
   
    // getting desc
    public String getDescripcion(){
    	return this._descripcion;
    }
 
    // setting desc
    public void setDescripcion(String desc){
        this._descripcion = desc;
    }
    
    //Devuelve un array con los elementos del objeto
    public String[] getArrayfromObject(){
    	String [] toreturn = null;
    	
    	toreturn = new String[3];
    	toreturn[0] = String.valueOf(_id);
    	toreturn[1] = _language;
    	toreturn[2] = _descripcion;
    	
    	return toreturn;
    }
    
}
