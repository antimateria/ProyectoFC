package com.android.aesafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;



import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public final class Act_SecretosActivity extends Activity{  
 
  protected MyExpandableListAdapter _mAdapter;
  protected ExpandableListView _epView;
  protected AES256cipher _cifrator = null;
  protected DB_Adapter _db = null;
  protected String _pwdSaved, _selValue, _selDesc,_selCateg;
  
  @Override  //********************************************************************************** 
  public void onCreate(Bundle savedInstanceState) {  
	  super.onCreate(savedInstanceState);
	  requestWindowFeature(Window.FEATURE_NO_TITLE);
	  setContentView(R.layout.scr_vaults);
	  
	  this._db = new DB_Adapter(getApplicationContext());
	  /* OBTENCION DEL PARAMETRO QUE RECIBO DEL INTENT INTROD. CONTRASEnnA */
	  _pwdSaved = getIntent().getStringExtra("privdata");
	  startAdapter();
	
	   	  
  }  
  //* ***************************************************************************************** */
  public void startAdapter(){
	  if (_pwdSaved != null){
		  _epView = (ExpandableListView) findViewById (R.id.expandableList000);	 
		  _mAdapter = new MyExpandableListAdapter();	
		  _epView.setAdapter(_mAdapter);	
		  _mAdapter.notifyDataSetChanged();
	  }
  }
  
  @Override //**********************************************************************************
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		_mAdapter.fCargarDatos();
		_mAdapter.notifyDataSetChanged();
	}
  
  @Override // **********************************************************************************
  public boolean onCreateOptionsMenu(Menu menu) {
      //Alternativa 1
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_actions, menu);
      return true;
  }
  
  @Override //**********************************************************************************
  public boolean onOptionsItemSelected(MenuItem item) {  
	  Bundle dic = new Bundle();	   
	  
	  //#TODO: Programar las siguientes opciones:
	  /*
	   * CAMBIAR CONTRASEÑA, con la correspondiente pantalla para verificar la contraseña
	   * anterior y la recepcion de la nueva.
	   * 
	   * AYUDA: se creara una nueva actividad, donde se mostrará en modo texto, la forma
	   * de interactuar con la interfaz grafica, y las posibilidades que tiene la aplicacion.
	   * 
	   */
	  switch (item.getItemId()) {
          case R.id.mnuaddnew:
        	  // Se agregan los valores al diccionario que contendra los parametros pasados
        	  dic.putString("privdata", _pwdSaved);
        	  ActivityRun(dic, Act_AddActivity.class, 1);   	  
              return true;       
          case R.id.mnuchgpwd:
        	  Toast.makeText(getApplicationContext(),"Pulsado Cambiar PWD",Toast.LENGTH_SHORT).show();
              return true;
          case R.id.mnuAyuda:
        	  Toast.makeText(getApplicationContext(),"Pulsado Ayuda",Toast.LENGTH_SHORT).show();
              return true;
          default:
              return super.onOptionsItemSelected(item);
      }
  }
  
  //**********************************************************************************
  /* Wrapper para path de la app */
  public String getStringPath(String file){
	  return this.getFileStreamPath(file).toString();
  }
  
  /** **************************** EJECUTA ACTIVITIES CON PARAMETROS ************************************ */
  private void ActivityRun(Bundle pParams, Class <?> pCls, int pReqcode){
	  Intent i = null;	
	  i = new Intent(this, pCls);
	  
	  if (pParams != null)
		  i.putExtras(pParams);
	  
	  startActivityForResult(i,pReqcode);  
  }
 
  //**********************************************************************************
  public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	  
      // Ejemplo de estructura de datos que lee la lista expandible
      private String[] groups = null; //{ "Names", "Designation", "Gender", "Company" };
      private String[][] children =  null;//{{ "abc", "xyz", "ash", "anu" },{ "SSE", "TJ", "PM", "SE" },{ "Male", "Female" },{ "yyyyyy", "xxxxx" }};
      private DB_Adapter _db;

      
      // **********************************************************************************
      public MyExpandableListAdapter(){
    	  fCargarDatos();    	  
      }
          
      // **********************************************************************************
      public void fCargarDatos(){
    	
    	  _db = new DB_Adapter(getApplicationContext());
    	  List <String[]> categorias = null;    	  
    	  List <String[]> secretos = null;
    	  TextView titulo = (TextView) findViewById(R.id.titulo_vaults);
    	     	  
		  titulo.setText("AESafe DataVAULT");
		  titulo.setBackgroundColor(Color.parseColor("#6B006B"));
		  titulo.setTextColor(Color.parseColor("#FFB200"));
		  Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/heav.ttf");
		  titulo.setTypeface(tf);
		  
		  categorias = new ArrayList<String[]>(_db.getDistinctVaultCateg().size());		  	  
		  categorias = _db.getDistinctVaultCateg();
		  
    	  if (categorias.size() == 0){
    		  Toast.makeText(getApplicationContext(),R.string.InfoAddVaults,Toast.LENGTH_LONG).show();    		      	      		
    	  }
    		      	  
    	  this.groups = new String[categorias.size()];
    	  this.children = new String[groups.length][]; // Reservamos para las categorias
    	  
    	  for (int i=0; i < categorias.size(); ++i){
    		groups[i] = categorias.get(i)[0]; // la posicion 0 corresponde a la descripcion de la categoria
    		children[i] = new String [_db.getNumVaults(groups[i])];
    		secretos = _db.getAllVaults(groups[i],_pwdSaved);
    		for (int j=0; j < secretos.size(); ++j){
    			children[i][j] = secretos.get(j)[2] + "|" + secretos.get(j)[3];
        	  }
    	  }  
    	  
    	  // Inicializacion del elemento seleccionado
    	  _selDesc = "";
    	  _selCateg = "";
    	  _selValue = "";
    	  
    	  _db.close();
      }
           
      // **********************************************************************************
      public Object getChild(int groupPosition, int childPosition) {
          return children[groupPosition][childPosition];
      }
      
      // **********************************************************************************
      public long getChildId(int groupPosition, int childPosition) {
          return childPosition;
      }
      
      // **********************************************************************************
      public int getChildrenCount(int groupPosition) {
          return children[groupPosition].length;
      }
      
      // **********************************************************************************
      // Definicion de la vista generica para quien la quiera usar,
      // en este caso se usa para los group y para los child
      public TextView getGenericView() {
          // Layout parameters for the ExpandableListView
          AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);
          TextView textView = new TextView(Act_SecretosActivity.this);
          textView.setLayoutParams(lp);
                   
          // Center the text vertically
          textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                   
          // Set the text starting position
          textView.setPadding(50, 0, 0, 0);
          
          return textView;
      }
          
      // **********************************************************************************
      // Vista de las lineas de child
      @Override
      public View getChildView (final int groupPosition, 
    		  				    final int childPosition, 
    		  				    boolean isLastChild,
    		  				    View convertView, 
    		  				    ViewGroup parent) {
    	  
		Context ctx = getApplicationContext();
		LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = layoutInflater.inflate(R.layout.lista_vaults_child_row, null);    	  		
		
		TextView textView = (TextView) convertView.findViewById(R.id.TextViewChild01);
		TextView textView2 = (TextView) convertView.findViewById(R.id.TextViewChild02);		
		Typeface fonta = Typeface.createFromAsset(ctx.getAssets(), "fonts/Geometry.otf"); 
		textView.setTypeface(fonta);
		textView2.setTypeface(fonta);
		textView.setTextSize(11);
		textView2.setTextSize(10);
		textView.setText(getChild(groupPosition, childPosition).toString().split("[|]+")[0]);
		

		
		// ################## SE CREA EL EVENTO PULSACION y PULSACION LARGA Y SE PROGRAMA EL CONTENIDO  ###########################
		convertView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View vista) {
				// TODO Auto-generated method stub
				
				//CAMBIA EL COLOR DEL FONDO DE LA FILA seleccionada (RETROALIMENTACION PARA EL USUARIO)				
				vista.setBackgroundColor(Color.GREEN);
				
				LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				View popupView = layoutInflater.inflate(R.layout.popupwindow, null);		
				final PopupWindow popupWin = new PopupWindow(popupView,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
				
				Button btnEditar   = (Button)popupView.findViewById(R.id.btn_PopupEditar);
				Button btnBorrar   = (Button)popupView.findViewById(R.id.btn_PopupBorrar);			
				Button btnCancelar = (Button)popupView.findViewById(R.id.btn_PopupCancelar);
				
				try {
					// Accion o Evento del boton editar
					btnEditar.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Bundle dic = new Bundle();							
							dic.putString("privdata", _pwdSaved);
							
							dic.putString("descParam", getChild(groupPosition, childPosition).toString().split("[|]+")[0]);
							dic.putString("categParam", (String) getGroup(groupPosition));
							dic.putString("valorParam", getChild(groupPosition, childPosition).toString().split("[|]+")[1]);				
							
				        	ActivityRun(dic, Act_ModifyActivity.class, 1); 
							_mAdapter.notifyDataSetChanged();
							popupWin.dismiss();
						}
						
					});
					
					// Accion o Evento del boton borrar
					btnBorrar.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							int GrpPos, ChdPos;
							GrpPos = groupPosition;
							ChdPos = childPosition;
							
							String childWholeValue = getChild(GrpPos, ChdPos).toString();
							DB_Vault params = new DB_Vault();
							params.setCategoria(getGroup(GrpPos).toString());
							params.setDescripcion(childWholeValue.split("[|]+")[0]);
							params.setValor(childWholeValue.split("[|]+")[1]);
							
							//Si es una imagen borramos tambien el fichero del disco
							if (getGroup(GrpPos).toString().equals("CATEG3_DESC")){
								File f = new File(getStringPath(childWholeValue.split("[|]+")[1]));								
								try {
									if (f.exists()) 
										f.delete();									
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							_db.deleteVault(params, _pwdSaved);																										
						
							//CIERRA DIALOGO
							popupWin.dismiss();
							startAdapter();
						}
						
					});
					
					// Accion o Evento para cancelar la accion
					btnCancelar.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {							
							popupWin.dismiss();
							_mAdapter.notifyDataSetChanged();
						}
						
					});	
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("El error es: "+e.getMessage());
				}
	
				popupWin.showAtLocation(popupView, Gravity.CENTER, 0, 0);								
				
				System.out.println("Grupo:"+groupPosition+" Hijo:"+childPosition);
				//popupWin.dismiss();		
				return true;
			}			  	  
	    });	  
				
		// ########  FIN EVENTO  ################ 
		
		if (this.groups[groupPosition].equals("CATEG3_DESC")){ 
			// Si es una imagen la mostramos en el icono del elemento en lugar de las llaves
			// Mapeamos el objeto de diseño sobre una instancia
			
			ImageView iconDetail = (ImageView) convertView.findViewById(R.id.imagechild);
			
			FileInputStream in = null;
			
			try {
				in = openFileInput(getChild(groupPosition, childPosition).toString().split("[|]+")[1]);			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Ahora hay que desencriptar la imagen del disco antes de pasarsela al imageview
			// para visualizarla.
			try {
				File fileIn = new File(getStringPath(getChild(groupPosition, childPosition).toString().split("[|]+")[1]));
				int tamanio = (int)fileIn.length();
				byte [] bufImgDecrypted = new byte [tamanio];				
				byte [] bufImgPlain = new byte [tamanio];
				
				in.read(bufImgDecrypted);
				
				// DESENCRIPTACION DE LA IMAGEN
				bufImgPlain = Utils.desCifraBytes(bufImgDecrypted, _pwdSaved);
				
				// ASIGNACION DE LA IMAGEN AL OBJETO IMAGEVIEW.
				iconDetail.setImageBitmap(BitmapFactory.decodeByteArray(bufImgPlain,0,bufImgPlain.length));
				
				in.close();
				// Caso de fichero sin encriptar
				// iconDetail.setImageBitmap(BitmapFactory.decodeStream(in));				
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error a leer fichero imagen en Buffer" + e.getMessage());				
			}
	
			textView2.setText("");
		} else {						
			textView2.setText(getChild(groupPosition, childPosition).toString().split("[|]+")[1]);
		}
			
		return convertView;
    	  
      }
      
      // **********************************************************************************      
      public Object getGroup(int groupPosition) {

          return groups[groupPosition];
      }
      
      // **********************************************************************************
      public int getGroupCount() {
          return groups.length;
      }
      
      // **********************************************************************************
      public long getGroupId(int groupPosition) {
          return groupPosition;
      }
      
      // **********************************************************************************
      // Vista de las lineas de grupo
      @Override
      public View getGroupView (int groupPosition, 
    		  					boolean isExpanded, 
    		  					View convertView, 
    		  					ViewGroup parent) {
    	  
    	  Context ctx = getApplicationContext();
    	  LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	  convertView = layoutInflater.inflate(R.layout.lista_vaults_group_row, null);
    	  
    	  ImageView iconGroup = (ImageView) convertView.findViewById(R.id.imageGroup);
    	  String dataType = getGroup(groupPosition).toString();
    	  TextView textView = (TextView) convertView.findViewById(R.id.TextViewGroup);
    	  Typeface fonta = Typeface.createFromAsset(ctx.getAssets(), "fonts/Geometry.otf"); 
    	  textView.setTextSize(14);
    	  textView.setTypeface(fonta);
    	  
    	  if (dataType.equalsIgnoreCase("CATEG1_DESC")){
    		  iconGroup.setImageDrawable(getResources().getDrawable(R.drawable.passport));  
    		  textView.setText(getString(R.string.CATEG1_DESC));
    	  } else if (dataType.equalsIgnoreCase("CATEG2_DESC")){
    		  iconGroup.setImageDrawable(getResources().getDrawable(R.drawable.creditcard));
    		  textView.setText(getString(R.string.CATEG2_DESC));
    	  } else if (dataType.equalsIgnoreCase("CATEG3_DESC")){
    		  iconGroup.setImageDrawable(getResources().getDrawable(R.drawable.imagenico));
    		  textView.setText(getString(R.string.CATEG3_DESC));
    	  } else if (dataType.equalsIgnoreCase("CATEG4_DESC")){
    		  iconGroup.setImageDrawable(getResources().getDrawable(R.drawable.bankweb));
    		  textView.setText(getString(R.string.CATEG4_DESC));
    	  } else if (dataType.equalsIgnoreCase("CATEG5_DESC")){
    		  iconGroup.setImageDrawable(getResources().getDrawable(R.drawable.webmail));
    		  textView.setText(getString(R.string.CATEG5_DESC));
    	  } else if (dataType.equalsIgnoreCase("CATEG6_DESC")){
    		  iconGroup.setImageDrawable(getResources().getDrawable(R.drawable.bankcodes));
    		  textView.setText(getString(R.string.CATEG6_DESC));
    	  } else if (dataType.equalsIgnoreCase("CATEG7_DESC")){
    		  iconGroup.setImageDrawable(getResources().getDrawable(R.drawable.otros));
    		  textView.setText(getString(R.string.CATEG7_DESC));
    	  }
    		        
    	  return convertView;
      }
      
      // **********************************************************************************
      public boolean isChildSelectable(int groupPosition, int childPosition) {
          return true;
      }
      
      // **********************************************************************************
      public boolean hasStableIds() {
          return true;
      }


   }


}

