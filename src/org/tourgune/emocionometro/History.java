package org.tourgune.emocionometro;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.tourgune.emocionometro.bean.SurveyBean;
import org.tourgune.emocionometro.dao.SurveyDAO;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class History extends Activity implements OnClickListener {

	private Button commitButton;
	private static final int commitButtonId = R.id.historyCommitTxt;
	
	private Button deleteButton;
	private static final int deleteButtonId = R.id.historyDropButton;
	
	private ListView list;
	private static final int listId = R.id.historyListView;
	
	ArrayList<Item> itemList;
	ItemsAdapter listAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history);

		initVariables();
		listeners();
		
		listAdapter();
		
	}
	
	public void initVariables(){
		commitButton=(Button) findViewById(commitButtonId);
		deleteButton=(Button) findViewById(deleteButtonId);
		list=(ListView) findViewById(listId);
		
	}
	
	public void listeners(){
		commitButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
	}

	
	
	
	public class Item {
		public Drawable image;
		public int valueArousal;
		public int valuePleasure;
		public int valueDominance;
		public int userId;
		public long time;
		
		public Integer listIndex;

		public Item(int userId, long time, int valueArousal,int valuePleasure,int valueDominance) {
			this.userId = userId;
			this.time=time;
			this.valueArousal=valueArousal;
			this.valuePleasure=valuePleasure;
			this.valueDominance=valueDominance;
		}

	}
		
	public void listAdapter() {
		
		itemList = new ArrayList<Item>();
		
		ArrayList<SurveyBean> surveyList; 
		
		surveyList = SurveyDAO.instance(this).getSurveyList();
		
		for (int i = 0; i < surveyList.size(); i++) {
			
			SurveyBean surveyAux=surveyList.get(i);
			
			Item itemAux=new Item(surveyAux.getUserId(),surveyAux.getTime(),surveyAux.getValueArousal(), surveyAux.getValuePleasure(), surveyAux.getValueDominance());
						
			itemList.add(itemAux);
		}

		listAdapter = new ItemsAdapter(this, R.layout.listitemnew, itemList);

		list.setAdapter(listAdapter);

	}

	private class ItemsAdapter extends ArrayAdapter<Item> {

		private ArrayList<Item> items;

		public ItemsAdapter(Context context, int textViewResourceId,
				ArrayList<Item> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;

			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.listitemnew, null);
			}

			Item it = items.get(position);
			if (it != null) {

				TextView textHate = (TextView) v.findViewById(R.id.listItemValueHate);
				TextView textHappiness = (TextView) v.findViewById(R.id.listItemValueHappiness);
				TextView textSurprise = (TextView) v.findViewById(R.id.listItemValueSurprise);
				
				TextView it1 = (TextView) v.findViewById(R.id.listItemUserId);
				TextView it2 = (TextView) v.findViewById(R.id.listItemTime);

				if (textHate != null) {
					
					it1.setText(Integer.toString(it.userId));
					
					SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
					String hour = formatter.format(it.time);
					it2.setText(hour);
					
//					iv.setImageDrawable(getValueIcon(it.value));					
					textHate.setText(getResources().getString(R.string.emotionsurveyArousal)+": "+Float.toString(getRealValue(it.valueArousal)));
					textHappiness.setText(getResources().getString(R.string.emotionsurveyPleasure)+": "+Float.toString(getRealValue(it.valuePleasure)));
					textSurprise.setText(getResources().getString(R.string.emotionsurveyDominance)+": "+Float.toString(getRealValue(it.valueDominance)));
				}

					v.setBackgroundColor(android.graphics.Color.BLACK);
					it1.setTextColor(android.graphics.Color.WHITE);
					it2.setTextColor(android.graphics.Color.WHITE);
					
					textHate.setTextColor(android.graphics.Color.WHITE);
					textHappiness.setTextColor(android.graphics.Color.WHITE);
					textSurprise.setTextColor(android.graphics.Color.WHITE);
			}
			return v;
		}
	}
	
	public int getRealValue(int value){
		
		return value/10;
		 
	}
	
	
	public void resetDatabase(){
		SurveyDAO.instance(this).dropDatabase();
		SurveyDAO.instance(this).createDatabase();
		listAdapter();
	}
	
	public void showToast(String text, int duration) {
		Toast subscribedToast = new Toast(this);
		subscribedToast = Toast.makeText(this, text, duration);
		subscribedToast.setGravity(Gravity.CENTER, 0, 0);
		subscribedToast.show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case commitButtonId:
			String commitResult=SurveyDAO.instance(this).backupSurveysToSd(this,"");
			showToast(commitResult, 0);
			break;
			
		case deleteButtonId:
			
			AlertDialog.Builder ad = new AlertDialog.Builder(this);
	     	
	 		ad.setTitle(getString(R.string.DeleteWarningTitle));
	 		ad.setMessage(getString(R.string.DeleteWarning));    		

	 		ad.setNegativeButton(getString(R.string.noString), new Dialog.OnClickListener() {

	 			public void onClick(DialogInterface arg0, int arg1) {
	 				
	 			}
	 		});
	 		
	 		ad.setPositiveButton(getString(R.string.yesString), new Dialog.OnClickListener() {

	 			public void onClick(DialogInterface arg0, int arg1) {
	 				resetDatabase();
	 			} 
	 		});
	 		ad.show();
			
			
			break;
		}
	}
}