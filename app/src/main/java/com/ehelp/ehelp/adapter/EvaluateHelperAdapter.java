package com.ehelp.ehelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ehelp.ehelp.R;
import com.ehelp.ehelp.square.Evaluate;

import java.util.List;

/**
 * Created by Yunzhao on 2016/3/23.
 */
public class EvaluateHelperAdapter extends BaseAdapter {
    private List<Evaluate> evaluates;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private SharedPreferences sharedPref;

    public EvaluateHelperAdapter(List<Evaluate> evaluates, int resource, Context context) {
        this.evaluates = evaluates;
        this.resource = resource;
        this.context = context;
        sharedPref = context.getSharedPreferences("user_id", Context.MODE_PRIVATE);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void refresh(List<Evaluate> evaluates) {
        this.evaluates = evaluates;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return evaluates.size();
    }

    @Override
    public Object getItem(int position) {
        return evaluates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }
        final View view = convertView;
        final Evaluate evaluate = evaluates.get(position);

        TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        RatingBar rb_evaluate2 = (RatingBar) convertView.findViewById(R.id.rb_evaluate2);

        rb_evaluate2.setRating(evaluate.getRating());
        tv_name.setText(evaluate.getName());

        return convertView;
    }
}
