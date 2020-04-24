package mr.ncu.com.client;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class ViewHolder
{
    public TextView Room_Name;

    View itemView;
    public ViewHolder(View itemView){
        if(itemView == null){
            throw new IllegalArgumentException("itemView can not be null!");
        }
        this.itemView = itemView;
        Room_Name = (TextView)itemView.findViewById(R.id.Room_Name);
    }
}
public class RoomListAdapter extends BaseAdapter {

    private List<RoomInfo> RoomList;
    private LayoutInflater layoutinflater;
    private Context context;
    private int currentPos;
    private ViewHolder holder = null;

    public RoomListAdapter(Context context, List<RoomInfo> RoomList){
        this.RoomList = RoomList;
        this.context = context;
        layoutinflater = LayoutInflater.from(context);
    }

    public int getCount(){
        return RoomList.size();
    }

    public Object getItem(int position){
        return RoomList.get(position).getRoom_Name();
    }

    public long getItemId(int position){
        return position;
    }

    public void remove(int index){
        RoomList.remove(index);
        notifyDataSetChanged();
    }

    public void refreshDataSet(){
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null)
        {
            convertView = layoutinflater.inflate(R.layout.list_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.Room_Name.setText(RoomList.get(position).getRoom_Name());
        return convertView;
    }
}
