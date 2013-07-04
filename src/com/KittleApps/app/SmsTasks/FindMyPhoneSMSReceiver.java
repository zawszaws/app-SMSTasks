package com.KittleApps.app.SmsTasks;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.exceptions.RootToolsException;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

@SuppressLint("NewApi")
public class FindMyPhoneSMSReceiver extends BroadcastReceiver {

	public static boolean active = false;
	public static String secret = "null";
	public static SharedPreferences pref;
	public static Context CONTEXT;
	public void onReceive(Context context, Intent intent) {
		CONTEXT = context;
		pref = PreferenceManager.getDefaultSharedPreferences(CONTEXT);
		active = pref.getBoolean("service_active", true);
		secret = pref.getString("secret_text", "").toLowerCase();
		//UpdateKey();
		FindMyPhoneSettings.loadPrefs();
		if(active && secret.length() > 0) {
			Bundle bundle = intent.getExtras();
	        if (bundle != null)
	        {
	            Object[] pdus = (Object[]) bundle.get("pdus");
	            for (int i = 0; i < pdus.length; i++) {
					SmsMessage msg = SmsMessage.createFromPdu((byte[])pdus[i]);
					String from = msg.getOriginatingAddress();
					String txt = msg.getMessageBody().toString();
					if(txt.toLowerCase().equalsIgnoreCase("off@"+pref.getString("secret_text", "").toLowerCase())) {
						Log.d(FindMyPhoneHelper.LOG_TAG, "Got SMS with Turn Off Pass Phrase " + from);
						try {
							Toast.makeText(CONTEXT, "Power Off Command Recieved.", Toast.LENGTH_LONG).show();
							TurnOff();
						} catch (RootToolsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(txt.toLowerCase().startsWith("cmd@"+pref.getString("secret_text", "").toLowerCase())) {
						Log.d(FindMyPhoneHelper.LOG_TAG, "Got SMS with Terminal Pass Phrase " + from);
						String CMD = txt.replace("cmd@"+pref.getString("secret_text", "")+" ", "");
						String[] splitCMD = CMD.split("/n");
						if (splitCMD.length >= 0){
						for (int u = 0; u < splitCMD.length; u++){
						try {
							Toast.makeText(CONTEXT, "CMD Command Recieved.", Toast.LENGTH_LONG).show();
							CMD(splitCMD[u]);
						} catch (RootToolsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
						}
						if (splitCMD.length <= 0){
						return;
						}
					}
					if(txt.toLowerCase().startsWith("echo")) {
						String Remainder1 = txt.replace("echo ", "");
						String Remainder2 = Remainder1.replace("%pass%", ""+pref.getString("secret_text", "").toLowerCase()+"");
						String[] Remainder3 = Remainder2.split("\n");
						String separator = System.getProperty("line.separator");

						StringBuilder builder = new StringBuilder(Remainder3[0]);
						for (int b = 1; b < Remainder3.length; b++) {
						    builder.append(separator).append(Remainder3[b]);
						}
						String Remainder4 = builder.toString();
						Toast.makeText(CONTEXT, Remainder4+".", Toast.LENGTH_LONG).show();
						}
					if(txt.toLowerCase().startsWith("multi-echo")) {
						String Remainder1 = txt.replace("multi-echo ", "");
						String Remainder2 = Remainder1.replace("%pass%", ""+pref.getString("secret_text", "").toLowerCase()+"");
						String[] Remainder3 = Remainder2.split("\n");
						for (int a = 0; a < Remainder3.length; a++){
						Toast.makeText(CONTEXT, Remainder3[a]+".", Toast.LENGTH_LONG).show();
						}
					}
					if(txt.toLowerCase().equalsIgnoreCase("reboot@"+pref.getString("secret_text", "").toLowerCase())) {
						Log.d(FindMyPhoneHelper.LOG_TAG, "Got SMS with Reboot Pass Phrase " + from);
						try {
							Toast.makeText(CONTEXT, "Reboot Command Recieved.", Toast.LENGTH_LONG).show();
							Reboot();
						} catch (RootToolsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(txt.toLowerCase().equalsIgnoreCase("recovery@"+pref.getString("secret_text", "").toLowerCase())) {
						Log.d(FindMyPhoneHelper.LOG_TAG, "Got SMS with Reboot Recovery Pass Phrase " + from);
						try {
							Toast.makeText(CONTEXT, "Reboot Recovery Command Recieved.", Toast.LENGTH_LONG).show();
							RebootRecovery();
						} catch (RootToolsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(txt.toLowerCase().startsWith("uninstall@"+pref.getString("secret_text", "").toLowerCase())) {
						Log.d(FindMyPhoneHelper.LOG_TAG, "Got SMS with Uninstall Pass Phrase " + from);
						String CMD = txt.replace("uninstall@"+pref.getString("secret_text", "")+" ", "");
						if (CMD.equalsIgnoreCase("com.KittleApps.app.SmsTasks")){
						return;
						}
						else{
						try {
							Toast.makeText(CONTEXT, "Uninstall Command Recieved.", Toast.LENGTH_LONG).show();
							Uninstall(CMD);
						} catch (RootToolsException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TimeoutException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
						if (CMD.length() <= 0){
						return;
						}
					}
					/*else {
						return;
					}*/
				}
	        }
		}
	}
	@SuppressWarnings("deprecation")
	protected static void Reboot() throws RootToolsException, TimeoutException{
		if (RootTools.isAccessGiven()) {
    		try {
				String[] commands = new String[] {
		        		"id",
	                    "reboot" };
				RootTools.sendShell(commands, 1000, 1);
    		} catch (IOException e) {
    		    // something went wrong, deal with it here
    		}
		}
    }
	public static String multilineToast(String... lines){
		   StringBuilder sb = new StringBuilder();
		   for(String s : lines){
		     sb.append(s);
		     sb.append("\r\n");
		   }
		   return sb.toString();
		}
	@SuppressWarnings("deprecation")
	protected static void RebootRecovery() throws RootToolsException, TimeoutException{
		if (RootTools.isAccessGiven()) {
    		try {
				String[] commands = new String[] {
		        		"id",
	                    "reboot recovery" };
				RootTools.sendShell(commands, 1000, 1);
    		} catch (IOException e) {
    		    // something went wrong, deal with it here
    		}
		}
    }
	@SuppressWarnings("deprecation")
	protected static void TurnOff() throws RootToolsException, TimeoutException{
		if (RootTools.isAccessGiven()) {
    		try {
    		    String[] commands = new String[] {
		        		"id",
	                    "poweroff" };
				RootTools.sendShell(commands, 1000, 1);
    		} catch (IOException e) {
    		    // something went wrong, deal with it here
    		}
		}

    }
	@SuppressWarnings("deprecation")
	protected static void Uninstall(String Value) throws RootToolsException, TimeoutException{
		if (RootTools.isAccessGiven()) {
    		try {
    			String[] commands = new String[] {
		        		"id",
	                    "busybox mount -o remount,rw /system",
	                    "busybox rm /system/app/"+Value+"*.apk",
	                    "busybox pm uninstall "+Value+"*.apk",
	                    "busybox rm /data/app/"+Value+"*.apk",
	                    "busybox rmdir /data/app-lib/"+Value,
	                    "busybox mount -o remount,ro /system", };

    		    Toast.makeText(CONTEXT, "Uninstalled...\r\n"+Value+"\r\nPlease Reboot.", Toast.LENGTH_LONG).show();
    		    RootTools.sendShell(commands, 1000, 1);
    		} catch (IOException e) {
    		    // something went wrong, deal with it here
    		}
		}
	}
	@SuppressWarnings("deprecation")
	protected static void CMD(String Values) throws RootToolsException, TimeoutException{
		if (RootTools.isAccessGiven()) {
    		try {
    			if(Values.length() > 0){
    				String[] Split_Values = Values.split("\n");
    			for (int i = 0; i < Split_Values.length; i++){

    		    String[] commands = new String[] {
		        		"id",
		        		Split_Values[i] };
				RootTools.sendShell(commands, 1000, 1);
    				}
    			} 
    		}
    		catch (IOException e) {
    		    // something went wrong, deal with it here
    			}
		}
    }

}