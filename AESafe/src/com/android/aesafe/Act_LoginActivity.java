package com.android.aesafe;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Act_LoginActivity extends Activity {
 	private TextView _labelpwd; 
 	private TextView _labelretype;
 	private EditText _txtpwd;
 	private EditText _txtretype;	 	
 	private DB_Adapter _db;
 	private Button _cmdAceptar;
 
 	/** **************************************************************** */	
 	class Button_Clicker implements Button.OnClickListener {
		@Override
	    public void onClick(View v) {
			fCheckUsers();
	    }
	}
 	
 	/** **************************************************************** */	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scr_initpwd);
        
        // CAMBIANDO FUENTE DE LAS ETIQUETAS
        Typeface fonta = Typeface.createFromAsset(this.getAssets(), "fonts/Complete.ttf");             
        _labelpwd =(TextView)findViewById(R.id.lblPwdApp);           
        _labelpwd.setTypeface(fonta);
        _labelretype=(TextView)findViewById(R.id.lblRetypePwd);
        _labelretype.setTypeface(fonta);
		
        // ASOCIANDO CONTROLES CON DATOS INTRODUCIDOS POR USUARIO
        _cmdAceptar = (Button) findViewById(R.id.cmdAceptar);
		_txtpwd = (EditText)findViewById(R.id.txtTypePwd);
		_txtretype = (EditText)findViewById(R.id.txtRetypePwd);
		
		// INSTANCIA DE BASE DE DATOS
		_db = new DB_Adapter(this.getApplicationContext());
		
		_cmdAceptar.setOnClickListener(new Button_Clicker());		
		
		fExistUser();		
     }
   
          
   /** **************************************************************** */
   public boolean fExistUser(){
	   try {
		   if (_db.getNumUsuarios() > 0) {	 
				 _labelretype.setVisibility(View.INVISIBLE);
				 _txtretype.setVisibility(View.INVISIBLE);
				 return true;
			   }
			   return false;

	   } catch (Exception e) {
		// 	TODO: handle exception
		   return false;
	   }
   }
   
   /** **************************************************************** */
   public void fCheckUsers(){
	   Intent i = new Intent(this, Act_SecretosActivity.class);
	   Bundle dic = new Bundle();
	   String privdata = _txtpwd.getText().toString();
	   
	   dic.putString("privdata", privdata);
	   i.putExtras(dic);
	   
	   if (fExistUser()){		 
		
		 // Comprobacion de usuario en la base de datos
		 DB_UserAccess usuario = new DB_UserAccess();
		 usuario = _db.getUsuario("ministro");
		 
		 // Comparacion de la contrasenna MD5
		 String dbPwddecrypted = Utils.descifraString(usuario._password, privdata);
		 if (dbPwddecrypted.equalsIgnoreCase(privdata)){

			 //Contraseñas coinciden, se da paso a la pantalla Vaults
			 Toast.makeText(this,getString(R.string.InfoAccessSystem),Toast.LENGTH_SHORT).show();		
			 finish();  
			 startActivity(i);
			
		 } else {
		     // Contraseñas no coinciden, error
			 Toast.makeText(this,getString(R.string.ErrPwdIncorrecta),Toast.LENGTH_SHORT).show();			
		 }
		 		
	   } else {
		   if (fCheckTextBoxes()){   
			   fInsertUserDB();
			   //Se da paso a la pantalla Vaults
			   Toast.makeText(this,getString(R.string.InfoAccessSystem),Toast.LENGTH_SHORT).show();		
			   finish();  
               startActivity(i);
		   }
	   }		  
   }
   
   /** **************************************************************** */
   public void cleanTextBoxes (){
		_txtpwd.setText("");
		_txtretype.setText("");
		_txtpwd.requestFocus();	   
   }
   
   /** **************************************************************** */
   public boolean fCheckTextBoxes(){
	   boolean result = true;
	   if (_txtpwd.getText().toString().length() < 5){
			 Toast.makeText(this,getString(R.string.InfoPwdMin5Chars),Toast.LENGTH_SHORT).show();
			 cleanTextBoxes();	
			 result = false;
		 }
		 if (!_txtpwd.getText().toString().equalsIgnoreCase(_txtretype.getText().toString())) {
			/*Muestro un mensaje diciendo que son distintos*/ 
			Toast.makeText(this,getString(R.string.InfoPwdNotMatch),Toast.LENGTH_SHORT).show();
			cleanTextBoxes();
			result = false;
	     }
		 
		return result;
   }
     
   /** **************************************************************** */
   public void fInsertUserDB(){
	   try {
		   DB_UserAccess user = new DB_UserAccess();	
		   String cipher = _txtpwd.getText().toString();
		   user.setUser("ministro");			 
		   user.setPassWord(cipher);	   
		   _db.insertUsuario(user,cipher);
		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("!!!! ERROR: "+ e.getMessage());			
		}	   
   }
   
}//Activity

