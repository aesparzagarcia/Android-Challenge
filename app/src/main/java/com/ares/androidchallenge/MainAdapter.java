package com.ares.androidchallenge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ESA1GA on 05/10/2017.
 */

public class MainAdapter extends BaseAdapter {

    private List<String> jokes;
    LayoutInflater inflater;

    public MainAdapter(Context mContext, List<String> jokes) {
        inflater = LayoutInflater.from(mContext);
        this.jokes = jokes;
    }

    @Override
    public int getCount() {
        return jokes.size();
    }

    @Override
    public Object getItem(int position) {
        return jokes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        view = null;

        if(view == null){
            view = inflater.inflate(R.layout.item_joke, null);
            holder = new ViewHolder();
            holder.joke = (TextView) view.findViewById(R.id.txtItem);

            holder.joke.setText(jokes.get(i).substring(0,1).toUpperCase() + jokes.get(i).substring(1).toLowerCase());
        }

        return view;
    }

    private static class ViewHolder{
        TextView joke;
    }
}
