package org.tourgune.emocionometro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class main extends Activity implements OnClickListener {
	
	private Button acceptButton;
	private EditText et;
	public static int usuario;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		acceptButton = (Button) findViewById(R.id.acceptButton1);
		et = (EditText) findViewById(R.id.editText1);
		acceptButton.setOnClickListener(this);
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.acceptButton1){
			usuario = Integer.parseInt(et.getText().toString());
			
			Intent intent = new Intent(this, mainAct.class);
			startActivity(intent);
			
		}
	}
	
	
		
}