package com.android.aesafe;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Act_ModifyActivity extends Activity {
	 	 	
		//atributos usados para parametros de activities
		protected String _prmPwdSaved,
						 _prmValor,
						 _prmDescripcion,
						 _prmCategoria, 
						 _prmPathImg; 
		protected int REQUEST_SELECT_PHOTO = 50;
		protected int REQUEST_CAMERA = 51;
		private Spinner  _spnCateg;
		private TextView _lblCateg, 
						 _lblTitulo;
		public  EditText _txtDescrip,
						 _txtValor;
		private Button _cmdAceptar,
		               _cmdCancelar;
		public  Button _cmdImagen;
		public  ImageView _imgPanel;
		private Bitmap    bmp;
		private DB_Adapter _db;
		private Context   _ctx;		
		private String[]  _strings;
		
		public  int [] arr_images = {R.drawable.passport,
									 R.drawable.creditcard,
									 R.drawable.imagenico,
									 R.drawable.bankweb,
									 R.drawable.webmail,
									 R.drawable.bankcodes,
									 R.drawable.otros};
		
		// Subclases que implementan las acciones ***********************************************
		class Button_accept implements Button.OnClickListener {
			@Override
		    public void onClick(View v) {
				fCrearImagenCifrada();
				fModificarRegistro();					
		    }
		}
	        
		// Subclases que implementan las acciones ***********************************************
	    class Button_cancel implements Button.OnClickListener {
			@Override
		    public void onClick(View v) {
				finish();
		    }
		}
	    
	    // Subclases que implementan las acciones ***********************************************
	    class Button_image implements Button.OnClickListener{	    	
	    	@Override
	    	public void onClick(View arg0) {
	    		// Lo primero es que este boton estará visible siempre que me 
	    		// seleccionen del combo la categoria imagen
// 
//	    		SELECCIONA FOTO DE LA GALERIA
//	    		
//	    		Intent i = new Intent(Intent.ACTION_PICK) ;
//		    	i.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
//		    					 MediaStore.Images.Media.CONTENT_TYPE) ;
//		    	startActivityForResult(i,REQUEST_SELECT_PHOTO) ;
		    	
//				TOMA FOTO CON LA CAMARA		
	    		try {
			    	Intent i = new Intent("android.media.action.IMAGE_CAPTURE");
			    	startActivityForResult(i,REQUEST_CAMERA) ;
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
	    	}
	    }
	        		
	    // Subclases que implementan las acciones *************************************************
	    class Combo_Click implements OnItemSelectedListener {
    	    
			@Override //***************************************************************************
			public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
				try {
					// TODO Auto-generated method stub
					if (arr_images[position] == R.drawable.imagenico){		           
			            _imgPanel.setVisibility(ImageView.VISIBLE);
			            _cmdImagen.setVisibility(Button.VISIBLE);
			            _txtValor.setVisibility(EditText.INVISIBLE);
					} else {
			            _imgPanel.setVisibility(ImageView.INVISIBLE);
			            _cmdImagen.setVisibility(Button.INVISIBLE);
			            _txtValor.setVisibility(EditText.VISIBLE);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
			@Override //************************************************************
			public void onNothingSelected(AdapterView<?> parentView) {
				// TODO Auto-generated method stub
			}
	    }

	 	/** **************************************************************** */	
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);        
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.scr_modvault);
	        
	        /* OBTENCION DE LOS PARAMETROS QUE RECIBO DEL INTENT */
	  	  	_prmPwdSaved 	= getIntent().getStringExtra("privdata");
	  	  	_prmPathImg     = getIntent().getStringExtra("pathImage");
	  	  	_prmDescripcion = getIntent().getStringExtra("descParam");
	  	  	_prmCategoria 	= getIntent().getStringExtra("categParam");
	  	  	_prmValor 		= getIntent().getStringExtra("valorParam");	  	  	
	  	  	
	  	  	/* PRECARGAMOS EN LOS CONTROLES LOS DATOS DEL ELEMENTO SELECCIONADO PARA MODIFICAR*/	  	  	
	        Typeface fonta = Typeface.createFromAsset(this.getAssets(), "fonts/heav.ttf"); 
	        _ctx      = getApplicationContext();
	        _spnCateg = (Spinner) findViewById(R.id.cmbModVaultCateg);
	        _lblCateg = (TextView) findViewById(R.id.lblModVaultcmbcateg);
	        
	        _lblTitulo = (TextView) findViewById(R.id.lblModVaultTitulo);	        
	        _lblTitulo.setTypeface(fonta);
	        _lblTitulo.setTextColor(Color.parseColor("#FFB200"));
	        
	        _txtDescrip = (EditText) findViewById(R.id.txtModVaultDescripcion);
	    	_txtValor   = (EditText) findViewById(R.id.txtModVaultValor);
	    	
	        _strings = getResources().getStringArray(R.array.Categorias);
	        _lblCateg.setText(_ctx.getString(R.string.CategText));
	        _imgPanel = (ImageView) findViewById(R.id.imgModPpreview);
	        _imgPanel.setVisibility(ImageView.INVISIBLE);
	        
	        _spnCateg.setAdapter(new ComboCategAdapter(Act_ModifyActivity.this, R.layout.spinnercat, _strings));	        
	        _spnCateg.setOnItemSelectedListener(new Combo_Click());	
	        
	        _cmdAceptar  = (Button) findViewById(R.id.btnmodvault);
	        _cmdCancelar = (Button) findViewById(R.id.btncancelmodvault);
	        _cmdImagen   = (Button) findViewById(R.id.btnSelModImage);
	        _cmdImagen.setVisibility(Button.INVISIBLE);
	        
	        _cmdAceptar.setOnClickListener(new Button_accept());
	        _cmdCancelar.setOnClickListener(new Button_cancel());
	        _cmdImagen.setOnClickListener(new Button_image());

	        RellenarControles(_prmDescripcion,
	        				  _prmValor,
	        				  _prmCategoria,	        				   
	        				  _prmPwdSaved);	        	       	        	        
	    }
	    
	    /** *************************************************************************************** */	    
	    public void RellenarControles(String _pDescripcion,
	    							  String _pValor,
	    							  String _pCategoria,
	    							  String _pPwdSaved) {
	    	
	    	 //#MAPEO DE VALORES EN CONTROLES
	        _txtDescrip.setText((String)_pDescripcion);
	        _txtValor.setText((String)_pValor);
	        _spnCateg.setSelection(Utils.getCategoryIndex(_pCategoria));
	        
	        // La imagen si existe se carga en el control 
			if (_pCategoria == "CATEG3_DESC") {
				
				FileInputStream in = null;
				try {
					in = openFileInput(_pValor);			
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		        File fileIn = new File(this.getFileStreamPath(_pValor).toString());
				
				int tamanio = (int)fileIn.length();
				byte[] bufImgDecrypted = new byte[tamanio];				
				byte[] bufImgPlain = new byte[tamanio];
				
				try {
					in.read(bufImgDecrypted);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// DESENCRIPTACION DE LA IMAGEN
				bufImgPlain = Utils.desCifraBytes(bufImgDecrypted, _pPwdSaved);
				
				// ASIGNACION DE LA IMAGEN AL OBJETO IMAGEVIEW.
				_imgPanel.setImageBitmap(BitmapFactory.decodeByteArray(bufImgPlain,0,tamanio));		
				
				// Visibilidad de controles
				_cmdImagen.setVisibility(Button.VISIBLE);
				_imgPanel.setVisibility(ImageView.VISIBLE);
				_txtValor.setVisibility(EditText.VISIBLE);
			}	        
	    }
	    
	    /* ******************************************************************* */
	    public void fCrearImagenCifrada(){
	    	
			String newImageFileName = "";
    		String FileInfo[] = null;
    		int sufijoImgFileName = 1;	    		
    		Boolean fileNamePassed = false;
    		File fichImagen = null;
    		newImageFileName = this._txtDescrip.getText().toString();
    		
    		if(this.bmp != null ) {	
    			 
    			//  CHEQUEO DEL NOMBRE DEL FICHERO IMAGEN, PARA QUE NO SE REPITAN EN EL DISCO
    			while (!fileNamePassed){
    				fichImagen = this.getFileStreamPath(newImageFileName);
    				if (fichImagen.exists()){
    					FileInfo = newImageFileName.split(".");// separamos datos de nombre
    					newImageFileName = FileInfo[0];// cogemos el nombre
    					newImageFileName += (++sufijoImgFileName);
    					newImageFileName += FileInfo[1];
    				} else{
    					fileNamePassed = true;
    					if (FileInfo == null)
    						newImageFileName += ".jpg";
    				}	    			
    			}    				
    			
				try {
					FileOutputStream out = this.openFileOutput(newImageFileName,MODE_PRIVATE);
	    			bmp.compress(CompressFormat.JPEG, 12, out) ;
	    			out.close();
	    			
	    			/* Cifrado de la imagen en el disco */		
	    			FileInputStream fin = new FileInputStream(this.getFileStreamPath(newImageFileName));
	    			File f = new File(this.getFileStreamPath(newImageFileName).toString());
	    			    			
	    			byte [] fileData = new byte[(int)f.length()];
	    			fin.read(fileData);
	    			fin.close();
	    			
	    			// Una vez leido en buffer el fichero, lo borramos del disco
	    			// para que solo permanezca el que creamos a continuacion que está cifrado.
	    			f.delete();
	    			
	    			/* Grabamos cifrado a disco */
	    			if (fileData != null){
	    				FileOutputStream fileOut = this.openFileOutput(newImageFileName, MODE_PRIVATE);
	    				byte [] EncryptedData = new byte [fileData.length];
	    				EncryptedData = Utils.cifraBytes(fileData, _prmPwdSaved);
	    				fileOut.write(EncryptedData);
	    				fileOut.close();
	    			}		    			

		    		// Establecemos el valor del path de la imagen en el campo oculto.
	    			this._txtValor.setText(newImageFileName); 
	    			
				} catch (FileNotFoundException e) {
					System.out.println("FileNotFoundException generated when using camera") ;
				} catch (IOException e) {
					System.out.println(" IOException generated when using camera") ;
				} catch (Exception g){}

    		} else {
    			clearImage();
    		}
	    }
	      

	    /* ***************************************************************** */
	    public void fModificarRegistro(){
	    	try{
	    			    	
		    	DB_Vault newVault = new DB_Vault();
		    	_db = new DB_Adapter(getApplicationContext());		    	
		    	
		    	if (_txtDescrip.getText().length() == 0) {
		    		Toast.makeText(this, getString(R.string.ErrDescripVacia),Toast.LENGTH_LONG).show();		    		
		    		return;
		    	}
		    	if (_txtValor.getText().length() == 0) {
		    		Toast.makeText(this, getString(R.string.ErrValorVacio),Toast.LENGTH_LONG).show();		    	
		    		return;
		    	}
		    			    	
		    	String CatKey = "";
		    	
		    	switch (_spnCateg.getSelectedItemPosition()){
		    		case 0: CatKey = "CATEG1_DESC"; break;		    				
		    		case 1: CatKey = "CATEG2_DESC"; break;
		    		case 2: CatKey = "CATEG3_DESC"; break;
		    		case 3: CatKey = "CATEG4_DESC"; break;
		    		case 4: CatKey = "CATEG5_DESC"; break;
		    		case 5: CatKey = "CATEG6_DESC"; break;
		    		case 6: CatKey = "CATEG7_DESC"; break;		    		
		    	}
		    	
		    	//newVault.setCategoria(_spnCateg.getSelectedItem().toString());
		    	newVault.setCategoria(CatKey);		    	
		    	newVault.setDescripcion(_txtDescrip.getText().toString());
		    	newVault.setValor(_txtValor.getText().toString());
		    			    	
		    	DB_Vault oldVault = new DB_Vault();
		    	oldVault.setCategoria(_prmCategoria);
		    	oldVault.setDescripcion(_prmDescripcion);
		    	oldVault.setValor(_prmValor);
		    	
		    	_db.updateVault(oldVault, newVault, _prmPwdSaved);

		    	//_db.insertVault(newVault,_prmPwdSaved);
		    	_db.close();
		    	Toast.makeText(this,getString(R.string.InfoRecargaData),Toast.LENGTH_SHORT).show();
		    	
	    	} catch (Exception e){
	    		Toast.makeText(this,getString(R.string.ErrModVault),Toast.LENGTH_SHORT).show();
	    	}
	    		    	
			finish();
	    }

	    /* ******************************************************************************** */
	    public class ComboCategAdapter extends ArrayAdapter<String> {
		 	   
	        public ComboCategAdapter(Context context, int textViewResourceId,   String[] objects) {
	            super(context, textViewResourceId, objects);
	        }
	 
	        @Override
	        public View getDropDownView(int position, View convertView,ViewGroup parent) {
	            return getCustomView(position, convertView, parent);
	        }
	 
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            return getCustomView(position, convertView, parent);
	        }
	 
	        public View getCustomView(int position, View convertView, ViewGroup parent) {

	            LayoutInflater inflater=getLayoutInflater();
	            View row=inflater.inflate(R.layout.spinnercat, parent, false);
	            TextView label=(TextView)row.findViewById(R.id.lblspnCategoria);
	            label.setText(_strings[position]);	 	            
	            ImageView icon=(ImageView)row.findViewById(R.id.catimage);
	            icon.setImageResource(arr_images[position]);	            	        
	            return row;
	       }
	    } 

	    @Override  // ****************************************************************************/
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub	    	
	    	if (requestCode == REQUEST_SELECT_PHOTO) {
	    		if( resultCode != 0 ) {
		    		Cursor c = managedQuery(data.getData(),null,null,null,null) ;
		    		if(c.moveToFirst()) {
		    			c.getString(1);
		    			
		    		}else 
		    			clearImage();
	    		}else 
	    			clearImage();
	    	}
	    	
	    	if (requestCode == REQUEST_CAMERA) {
	    		Bundle extras;	    		
	    		
	    		try {
	    			 extras = data.getExtras();
	    			 bmp = (Bitmap)extras.get("data");
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("Error al recuperar la imagen tomada");
					return;
				}
	    		
	    		if (bmp != null)
	    			_imgPanel.setImageBitmap(bmp);    					    			
				
	    	}
	    }
	    	
	    /* ********************************************************************************************* */
		private void clearImage() {
			// TODO Auto-generated method stub			
		}

}
