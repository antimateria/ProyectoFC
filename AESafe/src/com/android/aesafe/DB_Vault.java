package com.android.aesafe;

public class DB_Vault {
	//private variables
    int _id;
    String _categoria;
    String _descripcion;
    String _valor;
     
    // Empty constructor
    public DB_Vault(){ }
    
    // constructor
    public DB_Vault(int id, String cat, String desc, String value){
        this._id = id;
        this._categoria = cat;
        this._descripcion= desc;
        this._valor = value;        
    }
 
    // getting ID
    public int getID(){
        return this._id;
    }
 
    // setting id
    public void setID(int id){
        this._id = id;
    }
 
    // getting categ
    public String getCategoria(){
        return this._categoria;
    }
 
    // setting categ
    public void setCategoria(String cat){
        this._categoria = cat;
    }
 
    // getting desc
    public String getDescripcion(){
        return this._descripcion;
    }
 
    // setting desc
    public void setDescripcion(String desc){
        this._descripcion = desc;
    }
    
    // getting valor
    public String getValor(){
    	return this._valor;
    }
 
    // setting valor
    public void setValor(String value){
        this._valor= value;
    }
    
    //Devuelve un array con los elementos del objeto
    public String[] getArrayfromObject(){
    	String [] toreturn = null;
    	
    	toreturn = new String[4];
    	toreturn[0] = String.valueOf(_id);
    	toreturn[1] = _categoria.replace("\t","").replace("\n", "");
    	toreturn[2] = _descripcion.replace("\t","").replace("\n", "");
    	toreturn[3] = _valor.replace("\t","").replace("\n", "");
    	
    	return toreturn;
    }
}
