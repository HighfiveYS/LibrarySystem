package yonsei.highfive.library;

import yonsei.highfive.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GalleryMenuAdapter extends BaseAdapter{

	private Context context;
	private int[] imageRes;
	//private String[] textRes;
	private LayoutInflater inflater;
	private int layout;
	
	public GalleryMenuAdapter(Context c, int layout, int[] imageRes){
		this.context = c;
		this.imageRes = imageRes;
		//this.textRes = textRes;
		this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.layout = layout;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		
		return imageRes.length;
	}

	@Override
	public Object getItem(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView = inflater.inflate(layout, parent, false);
		}
		ImageView image = (ImageView)convertView.findViewById(R.id.menu_image);
		//TextView text = (TextView)convertView.findViewById(R.id.menu_text);
		
		image.setImageResource(imageRes[pos]);
		//text.setText(textRes[pos]);
		return convertView;
	}

}
