package yonsei.highfive.junction;

import java.net.URI;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
	
	public JunctionAsyncTask(Activity activity,  JunctionActor actor, String message){
		this.actor = actor;
		this.activity = activity;
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
		this.switchboard = pref.getString("switchboard", "165.132.214.212"); 
		this.config =  new XMPPSwitchboardConfig(switchboard);
		this.message = message;
			
	}
	public JunctionAsyncTask(){
		
	}
	protected Void doInBackground(JSONObject... params) {
		try {
			URI jxSession = URI.create("junction://"+switchboard+"/db");
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
				}
			});
			mDialog.show();
		}
	}

	protected void onPostExecute(Void result) {
		mDialog.hide();
	};
}
