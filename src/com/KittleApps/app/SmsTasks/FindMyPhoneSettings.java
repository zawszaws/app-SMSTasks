package com.KittleApps.app.SmsTasks;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.exceptions.RootToolsException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class FindMyPhoneSettings extends Activity implements OnClickListener {
    static CheckBox cb;
    static EditText op,np,cp;
    static Button b;
    public static SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        try {
			RootTask.SU();
		} catch (RootToolsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        cb = (CheckBox) findViewById(R.id.service_Active);
        op = (EditText) findViewById(R.id.editText1);
        np = (EditText) findViewById(R.id.editText2);
        cp = (EditText) findViewById(R.id.editText3);
        b = (Button) findViewById(R.id.button1);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        cb.setChecked(true);
        b.setOnClickListener(this);
        loadPrefs();
    }

    private void savePrefs(String key, String value) {
       // SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        Editor edit = sp.edit();
        edit.putString(key, value);
        edit.commit();
    }
    public static void loadPrefs() {
    	        //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences();
    	        boolean cbValue;
    	        if(cb.isChecked()){
    	            cb.setChecked(true);
    	            cbValue = sp.getBoolean("service_Active", true);
    	            FindMyPhoneSMSReceiver.active = cbValue;
    	        }else{
    	            cb.setChecked(false);
    	            cbValue = sp.getBoolean("service_Active", false);
    	            FindMyPhoneSMSReceiver.active = cbValue;
    	        }
    	    }
    	    private void savePrefs(String key, boolean value) {
    	        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	        Editor edit = sp.edit();
    	        edit.putBoolean(key, value);
    	        edit.commit();
    	    }

    @Override

    public void onClick(View v) {
    	SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    	savePrefs("service_Active", cb.isChecked());
    	
    	if (op.getText().toString().equalsIgnoreCase(sp.getString("secret_text", ""))){
    		if((cp.getText().toString().equals("")) && (np.getText().toString().equals(""))){
        		Toast.makeText(getApplicationContext(), "New Passwords Cannot Be Blank.", Toast.LENGTH_LONG).show();
        	}
    		if((!np.getText().toString().equals("")) && (cp.getText().toString().equals(np.getText().toString()))){
    		savePrefs("secret_text", np.getText().toString());
    		Toast.makeText(getApplicationContext(), "Settings Saved.", Toast.LENGTH_LONG).show();
    		np.setText("");
    		cp.setText("");
    		op.setText("");
    		Intent main = new Intent(Intent.ACTION_MAIN);
    		main.addCategory(Intent.CATEGORY_HOME);
    		main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    		startActivity(main);
    	}
    		else {
    			Toast.makeText(getApplicationContext(), "New Passwords Do not Match.", Toast.LENGTH_LONG).show();
    		}
    	}
    	else {
			Toast.makeText(getApplicationContext(), "Old Password Incorrect.", Toast.LENGTH_LONG).show();
		}
    	
    }
}
