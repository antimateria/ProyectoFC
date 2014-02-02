package com.android.aesafe;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Adapter extends SQLiteOpenHelper {
	
    // All Static variables
	private static Context _ctx;
	
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "main.sql3";
 	
    // Instance
    private SQLiteDatabase _db;
 
    // Mensajes de retorno de operaciones de SQL
    static String MSG_ERR = "ERROR";
    static String MSG_OK = "CORRECTO";
    static String MSG_EXIST = "EXISTE";
    
    /** *********************************************************************************************************************
     ** CREACION DE LA BASE DE DATOS **************************************************************************************** 
     ** ********************************************************************************************************************* **/
    public DB_Adapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);    
        setContext(context);       
        
        // Creamos la base de datos
    	fCreateDataBase();
      
    }
        
    public void setContext (Context context){
    	DB_Adapter._ctx = context;
    }
    
    /** *********************************************************************************************************************
     ** MANEJO DE BASE DE DATOS (DDL) *************************************************************************************** 
     ** ********************************************************************************************************************* **/
         
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	try {
            // Drop older table if existed
        	_db = this.getWritableDatabase();
            _db.execSQL("DROP TABLE IF EXISTS " + _ctx.getString(R.string.T_CATEGORIAS));
            _db.execSQL("DROP TABLE IF EXISTS " + _ctx.getString(R.string.T_USUARIOS));
            _db.execSQL("DROP TABLE IF EXISTS " + _ctx.getString(R.string.T_VAULTS));        
            _db.close();
            // Create tables again
            onCreate(db);			
		} catch (Exception e) {
			// TODO: handle exception			
		}
    }
    
    
    /** *********************************************************************************************************************
     ** MANEJO DE TABLA USUARIOS (DML) ************************************************************************************** 
     ** ********************************************************************************************************************* **/
    private void fCreateDataBase(){
    	  String CreateTable = "";
    	  String path2DB = "";
    	  _db = this.getWritableDatabase();
    	  path2DB = _ctx.getDatabasePath(this.getDatabaseName()).toString();
    	  
    	  try {
                          
	          CreateTable = "CREATE TABLE " + _ctx.getString(R.string.T_VAULTS) + "("
	          	  + _ctx.getString(R.string.T_VAULTS_ID) + " INTEGER PRIMARY KEY AUTOINCREMENT," 
	          	  + _ctx.getString(R.string.T_VAULTS_CATEG) + " INTEGER,"
	          	  + _ctx.getString(R.string.T_VAULTS_DESCR) + " NVARCHAR(100)," 
	          	  + _ctx.getString(R.string.T_VAULTS_VALOR) + " NVARCHAR(500))";    	  
//	          	  + _ctx.getString(R.string.T_VAULTS_DESCR) + " TEXT," 
//	          	  + _ctx.getString(R.string.T_VAULTS_VALOR) + " TEXT)";
	      
	          _db.execSQL(CreateTable);
	          // #########################################################        
	          CreateTable = "CREATE TABLE " + _ctx.getString(R.string.T_USUARIOS) + "("
	            	  + _ctx.getString(R.string.T_USUARIOS_ID) + " INTEGER PRIMARY KEY AUTOINCREMENT," 
	            	  + _ctx.getString(R.string.T_USUARIOS_USER) + " TEXT,"
	            	  + _ctx.getString(R.string.T_USUARIOS_PASSWD) + " NVARCHAR(15))";
	    
	          _db.execSQL(CreateTable);
    	  } catch (Exception e){
    		  //LA BASE DE DATOS YA EXISTE  
    		  System.out.println("Ruta de la base de datos: " + path2DB);
    	  }
          
          _db.close();
    }
    
      
    // INSERT INTO USUARIOS (PARAMETRO OBJETO USUARIOS CON VALORES A INSERTAR)
    public void insertUsuario(DB_UserAccess user, String cipher) {
    	try {
        	_db = this.getWritableDatabase();
    		ContentValues values = new ContentValues();
    		values.put(_ctx.getString(R.string.T_USUARIOS_USER), user.getUser()); 
    		values.put(_ctx.getString(R.string.T_USUARIOS_PASSWD), Utils.cifraString(user.getPassWord(), cipher)); 
     
    		// Inserting Row
    		_db.insert(_ctx.getString(R.string.T_USUARIOS), null, values);    	
    		_db.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("!!! insertUsuario() ERROR: "+e.getMessage());
		}

    }
    
    /** ********************************************************************************************************************* **/  
    // Getting single user como la tabla solo va a tener un usuario, no hace falta extraer todo el contenido de la tabla.
    public DB_UserAccess getUsuario(String puser) {
		try {
		    // formato db.query ( TABLA, ARRAY CAMPOS A EXTRAER, CONDICION/ES WHERE, ARRAY VALORES INCOGNITAS WHERE, NULL,NULL,NULL,NULL)
		 	_db = this.getReadableDatabase();
		    Cursor cursor = _db.query(_ctx.getString(R.string.T_USUARIOS), 
		    		new String[] { _ctx.getString(R.string.T_USUARIOS_ID),
		    					   _ctx.getString(R.string.T_USUARIOS_USER), 
		    					   _ctx.getString(R.string.T_USUARIOS_PASSWD)}, 
		    					   _ctx.getString(R.string.T_USUARIOS_USER) + "=?", 
		    					   new String[] { String.valueOf(puser) }, 
		    					   null, null, null, null);
		    
		    if (cursor != null)
		        cursor.moveToFirst();
		 
		    // CREACION DE OBJETO DBUSERACCESS A PARTIR DEL RESULTADO DEL CURSOR.
		    DB_UserAccess user = new DB_UserAccess(Integer.parseInt(cursor.getString(0)),
		            										  	  cursor.getString(1), 
		            										  	  cursor.getString(2));
		    _db.close();
		    // return user
		    return user;
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    }
    
    /** ********************************************************************************************************************* **/
    // Updating single user, el objeto usuario tiene que tener en el campo id, un 1, puesto que se modificaria
    // solamente el unico registro que hay
    public int updateUsuario(DB_UserAccess user) {
    	try {
        	_db = this.getWritableDatabase();
        	 
    		ContentValues values = new ContentValues();
    		values.put(_ctx.getString(R.string.T_USUARIOS_USER), user.getUser()); 		
    		values.put(_ctx.getString(R.string.T_USUARIOS_PASSWD), user.getPassWord()); 
        	 
        	// updating row
        	return _db.update(_ctx.getString(R.string.T_USUARIOS),  values, 
        					 _ctx.getString(R.string.T_USUARIOS_ID) + " = ?", 
        					 new String[] { String.valueOf(user.getID()) });
			
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
    	
    }
     
    /** ****** PARA MEJORA VERSION 1.1 ****************************************************************************************** 
    // Deleting single user, el objeto usuario tiene que tener en el campo id, un 1, solo hay un registro
    public void deleteUsuario(dbUserAccess user) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(_ctx.getString(R.string.T_USUARIOS), 
				  _ctx.getString(R.string.T_USUARIOS_ID) + " = ?", 
				  new String[] { String.valueOf(user.getID()) });
		db.close();    	
    }        
    **/
    
    /** ********************************************************************************************************************* **/
    // Getting usuarios Count
    public int getNumUsuarios() {
    	try {
        	int value = 0;
        	_db = this.getReadableDatabase();
        	String countQuery = "SELECT * FROM " + _ctx.getString(R.string.T_USUARIOS);
    		Cursor cursor = _db.rawQuery(countQuery, null);
    		value = cursor.getCount();
    		cursor.close();
    		_db.close();
    		
             // return count
             return value;			
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
    }
    
       
    // Getting All distinct categories from secretos
    /** ********************************************************************************************************************* **/
    public List<String []> getDistinctVaultCateg() {
  		try {
    	   	List<String[]> categorias = null;    	 
    	    // Select All Query
    	    String selectQuery = "SELECT DISTINCT categoria FROM " + _ctx.getString(R.string.T_VAULTS) + " ORDER BY " + _ctx.getString(R.string.T_VAULTS_CATEG);
    	    SQLiteDatabase db = this.getWritableDatabase();  
    	    Cursor cursor = db.rawQuery(selectQuery, null);
    	    categorias = new ArrayList<String[]>(cursor.getCount());
    	    
    	    // looping through all rows and adding to list
    	    if (cursor.moveToFirst()) {
    	        do {  	        	
    	        	String[] data = new String[1];
    	        	data[0] = cursor.getString(0);
    	            // Adding categoria to list
    	        	categorias.add(data);
    	        	
    	        } while (cursor.moveToNext());
    	    }
    	 
    	    // return user list
    	    return categorias;
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    }
    
      
    /** *********************************************************************************************************************
     ** MANEJO DE TABLA VAULTS (DML) **************************************************************************************** 
     ** ********************************************************************************************************************* **/
    
    public String insertVault(DB_Vault vault, String ciphkey) {
    	try {   
    		String Desc = Utils.cifraString(vault.getDescripcion(), ciphkey);
    		String Value = Utils.cifraString(vault.getValor(), ciphkey);
    		_db = this.getWritableDatabase();
    		
    		// Primero miramos a ver si existe algun registro con el mismo categoria/descripcion (Clave unica)
    		String countQuery = "SELECT * FROM " + _ctx.getString(R.string.T_VAULTS) + 
    						    " WHERE " + _ctx.getString(R.string.T_VAULTS_DESCR) +  
    						    " LIKE '" + Desc.substring(0, Desc.length()-1) + "%'" + 
    						    " AND " + _ctx.getString(R.string.T_VAULTS_CATEG) + " = '" + vault.getCategoria() + "'";
    		
    		Cursor cursor = _db.rawQuery(countQuery, null);
    		
    		if (cursor.getCount() > 0){
    			// Ya existe un registro en la tabla con esa descripcion, se informa al usuario que introduzca otra
    			return MSG_EXIST;
    		}
    		
    		ContentValues values = new ContentValues();

    		values.put(_ctx.getString(R.string.T_VAULTS_CATEG), vault.getCategoria());  		
    		values.put(_ctx.getString(R.string.T_VAULTS_DESCR), Desc);
    		values.put(_ctx.getString(R.string.T_VAULTS_VALOR), Value); 
     
    		// Inserting Row
    		_db.insert(_ctx.getString(R.string.T_VAULTS), null, values);
    		_db.close();
    		return MSG_OK;
    		
		} catch (Exception e) {
			// TODO: handle exception	
			return MSG_ERR;
		}
    			
    }
    
    /** ********************************************************************************************************/
    public void updateVault(DB_Vault oldVault, DB_Vault newVault, String ciphkey){
    	try {    	    		
    		_db = this.getWritableDatabase();
    		String oldDesc  = Utils.cifraString(oldVault.getDescripcion(), ciphkey);
    		String oldValue = Utils.cifraString(oldVault.getValor(), ciphkey);
    		String newDesc  = Utils.cifraString(newVault.getDescripcion(), ciphkey);
    		String newValue = Utils.cifraString(newVault.getValor(), ciphkey);
    		
    		// Inserting Row
    		_db.execSQL("UPDATE " +  _ctx.getString(R.string.T_VAULTS) +  
    				   " SET " + 
    				   _ctx.getString(R.string.T_VAULTS_DESCR) + " = '" + newDesc.substring(0, newDesc.length()-1) +"', " +
    				   _ctx.getString(R.string.T_VAULTS_VALOR) + " = '" + newValue.substring(0, newValue.length()-1) + "', " +
    				   _ctx.getString(R.string.T_VAULTS_CATEG) + " = '" + newVault.getCategoria() + "' "+
    				   " WHERE " +
					   _ctx.getString(R.string.T_VAULTS_DESCR) + " LIKE '" + oldDesc.substring(0, oldDesc.length()-1) +"%' " +
    				   "AND " +
				       _ctx.getString(R.string.T_VAULTS_VALOR) + " LIKE '" + oldValue.substring(0, oldValue.length()-1) + "%' " +
				       "AND " +
				       _ctx.getString(R.string.T_VAULTS_CATEG) + " LIKE '" + oldVault.getCategoria() + "' ");
    		
    		
//    		_db.execSQL("DELETE FROM " + _ctx.getString(R.string.T_VAULTS) + 
//					     " WHERE " + _ctx.getString(R.string.T_VAULTS_DESCR) + 
//					     " like '" + Desc.substring(0, Desc.length()-1) +
//					     "%' AND " + _ctx.getString(R.string.T_VAULTS_VALOR) + 
//					     " like '" + Value.substring(0,Value.length()-1) + "%'");
	    		
    		_db.close();

		} catch (Exception e) {
			// TODO: handle exception		
			System.out.println("Error al eliminar el registro: " + e.getMessage());
		}
    }
    
    /** *************************************************************************************************************************** **/
    public void deleteVault(DB_Vault vault, String ciphkey) {
    	try {    	    		
    		_db = this.getWritableDatabase();
    		String Desc = Utils.cifraString(vault.getDescripcion(), ciphkey);
    		String Value = Utils.cifraString(vault.getValor(), ciphkey);
    		// Inserting Row

    		_db.execSQL("DELETE FROM " + _ctx.getString(R.string.T_VAULTS) + 
					     " WHERE " +_ctx.getString(R.string.T_VAULTS_DESCR) + 
					     " like '" + Desc.substring(0, Desc.length()-1) +
					     "%' AND " + _ctx.getString(R.string.T_VAULTS_VALOR) + 
					     " like '" + Value.substring(0,Value.length()-1) + "%'");
	    		
    		_db.close();

		} catch (Exception e) {
			// TODO: handle exception		
			System.out.println("Error al eliminar el registro: " + e.getMessage());
		}
    }
   
              
    /** ********************************************************************************************************************* **/
    public List<String []> getAllVaults (String pCat, String ciph){
    	List<String[]> vaults = null;
    	String selectQuery = "";
    	
    	if (ciph == null){
      	  return new ArrayList<String[]>(0);
      	}
    	
    	try {
    		if (pCat.isEmpty()){
    	    	// Select All Query
    		    selectQuery = "SELECT * FROM " + _ctx.getString(R.string.T_VAULTS) + " ORDER BY " + _ctx.getString(R.string.T_VAULTS_CATEG);
        	} else {
        		selectQuery = "SELECT * FROM " + _ctx.getString(R.string.T_VAULTS) + 
        						     " WHERE " + _ctx.getString(R.string.T_VAULTS_CATEG) + " = '" + pCat + "'" + 
        						     " ORDER BY " + _ctx.getString(R.string.T_VAULTS_CATEG);
        	}
        	
    	    _db = this.getWritableDatabase();    	   
    	    Cursor cursor = _db.rawQuery(selectQuery, null);
    	    vaults = new ArrayList<String[]>(cursor.getCount());
     	    
    	    // looping through all rows and adding to list
    	    if (cursor.moveToFirst()) {
    	        do {
    	        	DB_Vault vlt = new DB_Vault();
    	        	//	Desciframos el contenido de la cadena insertada en la base de datos
    	        	
    	        	vlt.setID(Integer.parseInt(cursor.getString(0)));
    	        	vlt.setCategoria(cursor.getString(1));
    	        	vlt.setDescripcion(Utils.descifraString(cursor.getString(2), ciph));
    	        	vlt.setValor(Utils.descifraString(cursor.getString(3), ciph));
    	        	
    	            // Adding categoria to list
    	        	vaults.add(vlt.getArrayfromObject());
    	        	
    	        } while (cursor.moveToNext());
    	    }
    	    _db.close();
    	    
        	return vaults;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR: "+ e.getMessage());
			return null;
		}
    	
    
    }
    
     /** ********************************************************************************************************************* **/  
    // Getting single user como la tabla solo va a tener un usuario, no hace falta extraer todo el contenido de la tabla.
    public DB_Vault getVault(int id) {
    	try {
    	   	 _db = this.getReadableDatabase();
        	 
     	    // formato db.query ( TABLA, ARRAY CAMPOS A EXTRAER, CONDICION/ES WHERE, ARRAY VALORES INCOGNITAS WHERE, NULL,NULL,NULL,NULL)
     	 
     	    Cursor cursor = _db.query(_ctx.getString(R.string.T_VAULTS), 
     	    		new String[] { _ctx.getString(R.string.T_VAULTS_ID),
     	    					   _ctx.getString(R.string.T_VAULTS_CATEG), 
     	    					   _ctx.getString(R.string.T_VAULTS_DESCR),
     	    					   _ctx.getString(R.string.T_VAULTS_VALOR) }, 
     	    		_ctx.getString(R.string.T_VAULTS_ID) + "=?", 
     	            new String[] { String.valueOf(id) }, 
     	            null, null, null, null);
     	    
     	    if (cursor != null)
     	        cursor.moveToFirst();
     	 
     	    // CREACION DE OBJETO DBUSERACCESS A PARTIR DEL RESULTADO DEL CURSOR.
     	    DB_Vault vault = new DB_Vault(Integer.parseInt(cursor.getString(0)),
     	            									 cursor.getString(1), 
     	            									 cursor.getString(2),
     	            									 cursor.getString(3));
     	    _db.close();
     	    // return user
     	    return vault;
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
     }
 
    /** ********************************************************************************************************************* **/
    public int getNumVaults(String pCateg){
    	String countQuery = "";
    	try {
        	if (!pCateg.isEmpty()){
        		countQuery = "SELECT * FROM " + _ctx.getString(R.string.T_VAULTS) + 
    		            	 " WHERE " + _ctx.getString(R.string.T_VAULTS_CATEG) + " = '" + pCateg + "'" + 	
    		            	 " ORDER BY " + _ctx.getString(R.string.T_VAULTS_DESCR);
        	} else {
        		countQuery = "SELECT * FROM " + _ctx.getString(R.string.T_VAULTS) + 
        		             " ORDER BY " + _ctx.getString(R.string.T_VAULTS_DESCR);
        	}
        	
        	int nvault = 0;
        	_db = this.getReadableDatabase();
    		Cursor cursor = _db.rawQuery(countQuery, null);
    		nvault = cursor.getCount();
    		cursor.close();
    		_db.close();

    		// return count
            return nvault;
			
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
    }
    
    /** ********************************************************************************************************************* **/  
    public String fDesencriptaBD(byte[] pPwd){  
    	String dataDecripted = "";
		try {
			AES256cipher cifrador = new AES256cipher(pPwd);
			dataDecripted = cifrador.decrypt(pPwd).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return dataDecripted;
		
    }//desencripta
    
    /** ********************************************************************************************************************* **/  
    public String fEncriptaBD(String pPwd){
		byte dataEncripted []= null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			AES256cipher cifrador = new AES256cipher(pPwd);			
			dataEncripted = cifrador.encrypt(md.digest(pPwd.getBytes("UTF-8")));			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataEncripted.toString();
		
    }//fEncriptaBD

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		
	}
   
}