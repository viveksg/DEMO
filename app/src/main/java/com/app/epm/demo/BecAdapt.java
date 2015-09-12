package com.app.epm.demo;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by HOME on 12-09-2015.
 */
public class BecAdapt extends ArrayAdapter<String> {

    Context context;
    String minordata[] = null;

    public BecAdapt(Context cont, int rid, String minorvals[]) {
        super(cont, rid, minorvals);
        this.context = cont;
        this.minordata = minorvals;

    }

    @Override
    public View getView(int pos, View v, ViewGroup bg) {
        LayoutInflater linf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = linf.inflate(R.layout.listitem, bg, false);
        TextView temp = (TextView) view.findViewById(R.id.tv1);
        temp.setText(minordata[pos]);
        temp.setTextColor(Color.WHITE);
        temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        temp.setGravity(Gravity.CENTER);
        return view;
    }
}
