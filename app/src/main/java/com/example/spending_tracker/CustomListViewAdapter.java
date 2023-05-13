package com.example.spending_tracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.spending_tracker.sqlite.object;

import java.util.ArrayList;

public class CustomListViewAdapter extends ArrayAdapter<object> {

    private final LayoutInflater inflater;
    private final Context context;
    private ViewHolder holder;
    private final ArrayList<object> persons;

    public CustomListViewAdapter(Context context, ArrayList<object> persons) {
        super(context,0, persons);
        this.context = context;
        this.persons = persons;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Override
    public object getItem(int position) {
        return persons.get(position);
    }

    @Override
    public long getItemId(int position) {
        return persons.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.list_view_item, null);
            holder = new ViewHolder();
            holder.itemImageView = (ImageView) convertView.findViewById(R.id.itemImageView);
            holder.itemPlace = (TextView) convertView.findViewById(R.id.itemPlace);
            holder.itemPrice = (TextView) convertView.findViewById(R.id.itemPrice);
            holder.itemTime = (TextView) convertView.findViewById(R.id.itemTime);
            convertView.setTag(holder);

        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        object person = persons.get(position);
        if(person != null){
            holder.itemImageView.setImageResource(R.drawable.baseline_delete_24);
            holder.itemPlace.setText(person.getPlace());
            holder.itemPrice.setText(String.valueOf(person.getPrice()));
            holder.itemTime.setText(person.getTime());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView itemPlace;
        TextView itemPrice;
        TextView itemTime;
        ImageView itemImageView;

    }
}