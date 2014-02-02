package com.android.aesafe;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



import android.util.Base64;

public class Utils {
	static AES256cipher cifrador = null;
	
	public static String MD5_Hash(String s) {
        MessageDigest m = null;

        try {
         	 m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
           	 e.printStackTrace();
        }

        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
	}
	
	/** **************************************************************************************************************** **/
    public static int getCategoryIndex(String pCateg){
    	
    	if (pCateg == ""){
    		return 0;
    	}else if (pCateg.equals("Documento")) {
        	 return 0;
        } else if (pCateg.equals("Tarjeta Credito")) {
        	 return 1;
        } else if (pCateg.equals("Imagen")) {
        	 return 2;
        } else if (pCateg.equals("Banco Web")) {
        	 return 3;
        } else if (pCateg.equals("Email Web")) {
        	 return 4;
        } else if (pCateg.equals("Banco Codigos")) {
        	 return 5;
        } else if (pCateg.equals("Otros")) {
        	 return 6;
        }
		return 0;    	
    }
	
	/** ********************************************************************************************************************* **/
    public static String descifraString (String data, String ciph){
    	try {
        	byte[] descripcionPlain = Base64.decode(data, Base64.DEFAULT);
        	cifrador = new AES256cipher(ciph);
        	byte descDecrypt[] = cifrador.decrypt(descripcionPlain);
        	return new String(descDecrypt,"UTF-8");
			
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
        	
    }
    
    /** ********************************************************************************************************************* **/
    public static String cifraString (String data, String ciph){
    	try {
        	byte[] dataPlain = data.getBytes();
        	cifrador = new AES256cipher(ciph);
        	byte[] dataciph = cifrador.encrypt(dataPlain);
        	return Base64.encodeToString(dataciph, Base64.DEFAULT);
			
		} catch (Exception e) {
			// TODO: handle exception
			return "";
		}
        	
    }
	
    /** ********************************************************************************************************************* **/
    public static byte [] cifraBytes (byte[] data, String ciph){
    	try {
         	cifrador = new AES256cipher(ciph);
			return cifrador.encrypt(data);   
		} catch (Exception e) {
			// TODO: handle exception	
			System.out.println("Error cifrando: "+e.getMessage());
			return null;
		}
        	
    }
    /** ********************************************************************************************************************* **/
    public static byte [] desCifraBytes (byte[] data, String ciph){
    	try {
        	cifrador = new AES256cipher(ciph);
         	return cifrador.decrypt(data);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error descifrando: "+e.getMessage());
			return null;
		}
        	
    }

}
