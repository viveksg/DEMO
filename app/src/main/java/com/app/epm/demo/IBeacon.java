package com.app.epm.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
}

public class IBeacon extends Activity {
    Activity act = null;
    JSONArray ibeaconar = null;
    BeaconElement bel[] = null;
    ListView lsv = null;
    BecAdapt adapt = null;
    String sar[] = null;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ibec);
        act = this;
        lsv = (ListView) findViewById(R.id.becl);
        NetTask nts = new NetTask();
        Void dem = null;
        nts.execute(dem);

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
                bel = new BeaconElement[ibeaconar.length()];
                sar = new String[ibeaconar.length()];
                int i = 0, j = 0, k = 0;
                JSONObject jtemp = null;
                String stemp = "";
                for (i = 0; i < ibeaconar.length(); i++) {
                    jtemp = ibeaconar.getJSONObject(i);
                    stemp = jtemp.getString("id");
                    stemp = stemp.substring(stemp.lastIndexOf("_") + 1);
                    Log.v("OUTPUT", i + " " + stemp);
                    bel[i] = new BeaconElement(stemp, jtemp.getString("beaconMsg"), jtemp.getInt("beaconType"));
                    sar[i] = stemp;
                }


            } catch (Exception rty) {
                Log.v("EXCEPTION", s2 + "\n" + rty.getMessage());
                rty.printStackTrace();
            }
            Log.v("OUPUT", s2);
            return null;
        }

        @Override
        public void onPostExecute(Void v) {
            adapt = new BecAdapt(act, R.layout.listitem, sar);
            lsv.setAdapter(adapt);
            pg.dismiss();


        }
    }


}
