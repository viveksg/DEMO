/*
  developed by Vivek Singh
 */

package com.app.epm.demo;


import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

public class DetailsFragment extends DialogFragment {
    String sdata[];
    String stitle, imagelink;

    Button ok, sms, share, open;
    RelativeLayout relayout;

    TableLayout tlayout;
    LayoutParams trps, tlps;
    String url = "";
    ImageView img;

    public DetailsFragment() {

    }

    public interface Exf {
        public void share(String s);

        public void sendSMS(String s);

        public void openLink(String s);
    }

    Exf exf;

    @Override
    public View onCreateView(LayoutInflater linf, ViewGroup vgrp, Bundle bundle) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Bundle bdata = getArguments();
        sdata = bdata.getStringArray(AppHub.AR_DATA);
        stitle = bdata.getString(AppHub.DATA);
        url = bdata.getString(AppHub.URL);
        imagelink = bdata.getString(AppHub.IMAGE);
        int colors[] = new int[2];
        colors[0] = Color.parseColor("#37DC74");
        colors[1] = Color.parseColor("#00B945");
        exf = (Exf) getActivity();
        View retview = linf.inflate(R.layout.frag_details, vgrp, false);
        img = (ImageView) retview.findViewById(R.id.im1);
        try {
            InputStream is = (InputStream) new URL(imagelink).getContent();
            img.setImageDrawable(Drawable.createFromStream(is, stitle));
        } catch (Exception drt) {
            Log.v("Draw Exception", "" + drt.getMessage());
        }
        relayout = (RelativeLayout) retview.findViewById(R.id.rel);
        relayout.setBackgroundColor(Color.parseColor("#FF4E00"));
        tlayout = (TableLayout) retview.findViewById(R.id.detailtable);
        trps = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        int i = 0;
        TextView temp = new TextView(getActivity());
        temp.setText(stitle);
        temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
        temp.setTextColor(Color.WHITE);
        temp.setTypeface(null, Typeface.BOLD);
        temp.setGravity(Gravity.CENTER_VERTICAL);
        TableRow trow = new TableRow(getActivity());
        trow.addView(temp);
        tlayout.addView(trow, trps);
        for (i = 0; i < sdata.length; i++) {
            temp = new TextView(getActivity());
            temp.setText(sdata[i]);
            temp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            temp.setTextColor(Color.WHITE);
            temp.setGravity(Gravity.CENTER_VERTICAL);
            temp.setTypeface(null, Typeface.BOLD);
            trow = new TableRow(getActivity());
            trow.setBackgroundColor(colors[i % 2]);
            trow.addView(temp);
            tlayout.addView(trow, trps);
        }
        sms = (Button) retview.findViewById(R.id.bok);
        sms.setText("SEND SMS");
        sms.setTextColor(Color.WHITE);
        sms.setBackgroundColor(Color.parseColor("#A357DE"));
        sms.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        sms.setGravity(Gravity.CENTER);
        sms.setTypeface(null, Typeface.BOLD);
        sms.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                exf.sendSMS("Download " + sdata[0] + " LINK: " + url);
            }
        });
        share = (Button) retview.findViewById(R.id.bok1);
        share.setText("SHARE");
        share.setTextColor(Color.WHITE);
        share.setBackgroundColor(Color.parseColor("#A357DE"));
        share.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        share.setGravity(Gravity.CENTER);
        share.setTypeface(null, Typeface.BOLD);
        share.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                exf.share("Download " + sdata[0] + "\nLINK: " + url + "\nDetails: " + sdata[5]);
            }
        });
        open = (Button) retview.findViewById(R.id.bok2);
        open.setText("PLAY MARKET");
        open.setTextColor(Color.WHITE);
        open.setBackgroundColor(Color.parseColor("#A357DE"));
        open.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        open.setGravity(Gravity.CENTER);
        open.setTypeface(null, Typeface.BOLD);
        open.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                exf.openLink(url);
            }
        });
        ok = (Button) retview.findViewById(R.id.bok3);
        ok.setText("CLOSE");
        ok.setTextColor(Color.WHITE);
        ok.setBackgroundColor(Color.parseColor("#A357DE"));
        ok.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        ok.setGravity(Gravity.CENTER);
        ok.setTypeface(null, Typeface.BOLD);
        ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });


        return retview;
    }

    @Override
    public void onDismiss(DialogInterface dinterface) {
        dismissAllowingStateLoss();
    }
}
