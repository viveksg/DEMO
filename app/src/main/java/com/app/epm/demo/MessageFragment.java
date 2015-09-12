package com.app.epm.demo;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageFragment extends DialogFragment {
    RelativeLayout relayout = null;
    TextView messageview = null;
    Button okay = null;
    public static final String MESSAGE = "msg";
    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater linf, ViewGroup vgroup, Bundle bundle) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = linf.inflate(R.layout.messagelayout, vgroup);
        relayout = (RelativeLayout) view.findViewById(R.id.mesagerel);
        messageview = (TextView) view.findViewById(R.id.messagetext);
        Bundle argbundle = getArguments();
        String message = argbundle.getString(MESSAGE);
        messageview.setText(message);
        messageview.setGravity(Gravity.CENTER);
        messageview.setTextColor(Color.WHITE);
        messageview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        okay = (Button) view.findViewById(R.id.okay);
        okay.setText("OKAY");
        okay.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onDismiss(DialogInterface dface) {
        dismissAllowingStateLoss();
        super.onDismiss(dface);
    }

}
