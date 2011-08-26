package yonsei.highfive.library.circulation;

import java.util.ArrayList;

import yonsei.highfive.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookAdapter extends BaseAdapter{
	private Context mainContext;
	private LayoutInflater inflater;
	private ArrayList<BookSpec> src;
	private int layout;
	
	public BookAdapter(Context context, int layout, ArrayList<BookSpec> src){
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
	public BookSpec getItem(int pos) {
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
		TextView title = (TextView)convertView.findViewById(R.id.booktitle);
		title.setText(src.get(position).getTitle());
		TextView bookid = (TextView)convertView.findViewById(R.id.bookid);
		bookid.setText(src.get(position).getBookid());
		TextView author = (TextView)convertView.findViewById(R.id.author);
		author.setText(src.get(position).getAuthor());
		TextView publisher = (TextView)convertView.findViewById(R.id.publisher);
		publisher.setText(src.get(position).getPublisher());
		
		return convertView;
	}

}
