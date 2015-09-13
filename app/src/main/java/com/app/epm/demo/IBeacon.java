package com.app.epm.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by HOME on 12-09-2015.
 */
class BeaconElement {
    String minor = "";
    String message = "";
    int type = -1;

    public BeaconElement(String min, String msg, int tp) {
        this.minor = min;
        this.message = msg;
        this.type = tp;
    }

    public String getMessage() {
        return message;
    }
}

public class IBeacon extends FragmentActivity implements ListView.OnItemClickListener {
    Activity act = null;
    JSONArray ibeaconar = null;
    BeaconElement beaconelement[] = null;
    ListView minorlist = null;
    BecAdapt adapt = null;
    String adapterarray[] = null;
    LinearLayout beaconlayout = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ibec);
        act = this;
        beaconlayout = (LinearLayout) findViewById(R.id.ibeacon);
        beaconlayout.setBackgroundColor(Color.parseColor("#009999"));
        minorlist = (ListView) findViewById(R.id.becl);
        minorlist.setOnItemClickListener(this);
        NetTask nts = new NetTask();
        Void dem = null;
        nts.execute(dem);

    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle messagebundle = new Bundle();
        messagebundle.putString(MessageFragment.MESSAGE, beaconelement[position].getMessage());
        MessageFragment messagefrag = new MessageFragment();
        messagefrag.setArguments(messagebundle);
        messagefrag.show(getSupportFragmentManager(), "Message" + System.currentTimeMillis());
    }

    class NetTask extends AsyncTask<Void, Void, Void> {
        String link = "http://tccserverwc.appspot.com/assets/data/showcase.json";

        ProgressDialog pg = null;

        @Override
        public void onPreExecute() {
            pg = new ProgressDialog(act);
            pg.setMessage("Getting Data...");
            pg.setCancelable(false);
            pg.show();
        }

        public Void doInBackground(Void... v) {
            String s1 = "", s2 = "";
            try {
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while (true) {
                    s1 = br.readLine();
                    if (s1 == null) break;
                    s2 += s1;
                }
                JSONObject jobject = new JSONObject(s2);
                ibeaconar = jobject.getJSONArray("beaconList");
                beaconelement = new BeaconElement[ibeaconar.length()];
                adapterarray = new String[ibeaconar.length()];
                int i = 0, j = 0, k = 0;
                JSONObject jtemp = null;
                String stemp = "";
                for (i = 0; i < ibeaconar.length(); i++) {
                    jtemp = ibeaconar.getJSONObject(i);
                    stemp = jtemp.getString("id");
                    stemp = stemp.substring(stemp.lastIndexOf("_") + 1);
                    Log.v("OUTPUT", i + " " + stemp);
                    beaconelement[i] = new BeaconElement(stemp, jtemp.getString("beaconMsg"), jtemp.getInt("beaconType"));
                    adapterarray[i] = stemp;
                }


            } catch (Exception exception) {
                Log.v("EXCEPTION", s2 + "\n" + exception.getMessage());
                exception.printStackTrace();
            }
            Log.v("OUPUT", s2);
            return null;
        }

        @Override
        public void onPostExecute(Void v) {
            adapt = new BecAdapt(act, R.layout.listitem, adapterarray);
            minorlist.setAdapter(adapt);
            pg.dismiss();


        }
    }


}
