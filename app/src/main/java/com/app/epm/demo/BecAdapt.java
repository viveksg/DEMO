package com.app.epm.demo;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
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
    String data[] = null;

    public BecAdapt(Context cont, int rid, String vals[]) {
        super(cont, rid, vals);
        this.context = cont;
        this.data = vals;

    }

    @Override
    public View getView(int pos, View v, ViewGroup bg) {
        LayoutInflater linf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = linf.inflate(R.layout.listitem, bg, false);
        TextView temp = (TextView) view.findViewById(R.id.tv1);
        temp.setText(data[pos]);
        temp.setTextColor(Color.WHITE);
        temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        return view;
    }
}
