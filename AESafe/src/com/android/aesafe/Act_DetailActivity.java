package com.android.aesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Act_DetailActivity extends Activity{
	
	  @Override  
	  public void onCreate(Bundle savedInstanceState) {  
		  super.onCreate(savedInstanceState);
		  requestWindowFeature(Window.FEATURE_NO_TITLE);
		  setContentView(R.layout.src_detailvault);
	  }  
	  
	  /**
	   * #TODO COMPLETAR ESTA PANTALLA
	   */
}
