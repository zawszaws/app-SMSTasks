package com.KittleApps.app.SmsTasks;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.AsyncTask;
import com.stericson.RootTools.*;
import com.stericson.RootTools.exceptions.RootToolsException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
@SuppressLint("NewApi")
public class RootTask extends AsyncTask<Void, Void, Void> {
    public Dialog dialog1 = null;
    public static int timeout = 120000;

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected Void doInBackground(Void... params) {
	return null;
	}

	@SuppressWarnings("deprecation")
	protected static void uninstall() throws RootToolsException, TimeoutException{
			if (RootTools.isAccessGiven()) {
	    		try {

	    		    String[] commands = new String[] {
    		        		"id",
    	                    "busybox mount -o remount,rw /system",
    	                    "busybox rmdir /data/app-lib/com.sonyericsson.extras.*",
    	                    "busybox rm /system/app/com.sonyericsson.extras.SmsTasks*.apk",
    	                    "busybox pm uninstall com.sonyericsson.extras.Smstasks*.apk",
    	                    "busybox mount -o remount,ro /system",
    	                    "busybox reboot" };
	    		    RootTools.sendShell(commands, 10000, 1);
	    		} catch (IOException e) {
	    		    // something went wrong, deal with it here
	    		}
			}
	    }
									@SuppressWarnings("deprecation")
									protected static void InstallSystem() throws RootToolsException, TimeoutException{
        	                				if (RootTools.isAccessGiven()) {
        	                		    		try {

        	                		    		    String[] commands = new String[] {
        	        	            		        		"id",
        	        	            	                    "busybox mount -o remount,rw /system",
        	        	            	                    "busybox cp /data/app/com.sonyericsson.extras.SmsTasks*.apk /system/app/",
        	        	            	                    "busybox chmod 644 /system/app/com.sonyericsson.extras.SmsTasks*.apk",
        	        	            	                    "busybox pm uninstall com.sonyericsson.extras.SmsTasks",
        	        	            	                    "busybox rmdir /data/app-lib/com.sonyericsson.extras.*",
        	        	            	                    "busybox rm /data/app/com.sonyericsson.extras.SmsTasks*.apk",
        	        	            	                    "busybox mount -o remount,ro /system",
        	        	            	                    "busybox reboot" };
        	                		    		    RootTools.sendShell(commands, 10000, 1);
        	                		    		} catch (IOException e) {
        	                		    		    // something went wrong, deal with it here
        	                		    		}
        	                				}
        	                		    }
	protected static void SU() throws RootToolsException, TimeoutException{
		if (RootTools.isAccessGiven()) {
    		}
    }

    @Override
    protected void onPostExecute(Void result) {
        }
    }