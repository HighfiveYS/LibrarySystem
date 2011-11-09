package yonsei.highfive.junction;

import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;
import edu.stanford.junction.JunctionException;
import edu.stanford.junction.android.AndroidJunctionMaker;
import edu.stanford.junction.api.activity.JunctionActor;
import edu.stanford.junction.provider.xmpp.XMPPSwitchboardConfig;

public class JunctionAsyncTask extends AsyncTask<JSONObject, Void, Void> {
	private ProgressDialog mDialog;
	private XMPPSwitchboardConfig config;
	private JunctionActor actor;
	private Activity activity;
	private String switchboard;
	private String message;
	private String sessionID;
	
	public JunctionAsyncTask(Activity activity,  JunctionActor actor, String sessionID, String message){
		this.actor = actor;
		this.activity = activity;
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
		this.switchboard = pref.getString("switchboard", "mobilesw.yonsei.ac.kr"); 
		this.config =  new XMPPSwitchboardConfig(switchboard);
		this.sessionID = sessionID;
		this.message = message;
	}
	public JunctionAsyncTask(){
		
	}
	protected Void doInBackground(JSONObject... params) {
		try {
			URI jxSession = URI.create("junction://"+switchboard+"/"+sessionID);
			AndroidJunctionMaker.getInstance(config).newJunction(jxSession,	actor);
			synchronized (actor) {
				try {
					actor.sendMessageToRole("director", params[0]);
					actor.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		} catch (JunctionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			activity.finish();
		}
		return null;
	};
	
	/*
	 * implements Junction Connection Timeout
	 */
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			mDialog.cancel();
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(activity.getLocalClassName().equals("library.Settings"))
							activity.finish();
					Toast.makeText(activity, "Timeout", Toast.LENGTH_LONG).show();
					try {
						this.finalize();
					} catch (Throwable e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}
	};
	
	@Override
	protected void onPreExecute() {
		if (mDialog == null) {
			mDialog = new ProgressDialog(activity);
			mDialog.setMessage(message);
			mDialog.setIndeterminate(true);
			mDialog.setCancelable(true);
			mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface arg0) {
					actor.leave();
				}
			});
			mDialog.show();	
			timer.schedule(task, 10000);
		
		}
	}

	protected void onPostExecute(Void result) {
		mDialog.hide();
		timer.cancel();
	};
}
