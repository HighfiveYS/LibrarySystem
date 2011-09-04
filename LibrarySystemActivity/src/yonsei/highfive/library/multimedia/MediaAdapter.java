package yonsei.highfive.library.multimedia;

import java.util.ArrayList;

import yonsei.highfive.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MediaAdapter extends BaseAdapter{
	private Context mainContext;
	private LayoutInflater inflater;
	private ArrayList<MediaSpec> src;
	private int layout;
	
	public MediaAdapter(Context context, int layout, ArrayList<MediaSpec> src){
		this.mainContext = context;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.src = src;
		this.layout = layout;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return src.size();
	}

	@Override
	public MediaSpec getItem(int pos) {
		// TODO Auto-generated method stub
		return src.get(pos);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int pos = position;
		if(convertView==null){
			convertView = inflater.inflate(layout, parent, false);
		}
		TextView title = (TextView)convertView.findViewById(R.id.title);
		title.setText(src.get(position).getTitle());
		TextView mediaid = (TextView)convertView.findViewById(R.id.mediaid);
		mediaid.setText(src.get(position).getmediaid());
		TextView director = (TextView)convertView.findViewById(R.id.director);
		director.setText(src.get(position).getdirector());
		TextView production = (TextView)convertView.findViewById(R.id.production);
		production.setText(src.get(position).getproduction());
		
		return convertView;
	}

}
