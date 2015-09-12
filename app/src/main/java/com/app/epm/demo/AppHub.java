package com.app.epm.demo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Appdata {

    public String name, type, url, image, last_updated, inapp_purchase, description;
    public double rating;
    public long tstamp;


    public Appdata(JSONObject jso) {
        try {
            this.name = jso.getString("name");
            this.type = jso.getString("type");
            this.url = jso.getString("url");
            this.image = jso.getString("image");
            this.rating = jso.getDouble("rating");
            this.last_updated = jso.getString("last updated");
            this.inapp_purchase = jso.getString("inapp-purchase");
            this.description = jso.getString("description");//.replace(".", ".\n");
            DateFormat dfrm = new SimpleDateFormat("dd MMMM yyyy");
            Date date = dfrm.parse(this.last_updated);
            this.tstamp = date.getTime();
        } catch (Exception exc) {
            Log.v("DataSetException", "" + exc.getMessage());
        }
    }


}

public class AppHub extends FragmentActivity implements OnItemSelectedListener, DetailsFragment.Exf {
    public static final String AR_DATA = "ar_data";
    public static final String URL = "url";
    public static final String DATA = "data";
    public static final String IMAGE = "image";
    Activity apph = this;
    Appdata appdata[];
    int sort_iap[], yes = 0, no = 0, len = 0, srat[], stime[], present[], par = 0;
    long sort_time[];
    double sort_rat[];
    TableLayout tlayout = null;
    TableRow trow[] = null;
    Spinner spin = null, spin2 = null;
    TextView tview1 = null, tview2 = null;
    List<String> list = null, list2 = null;
    TableLayout.LayoutParams tlpara1, tlpara2;
    TableRow.LayoutParams trpara1, trpara2;
    String link = "http://adobe.0x10.info/api/products?type=json";
    NetTasks nettasks = null;
    Void vd = null;
    int complete = 0;
    int colors[] = new int[2];
    RelativeLayout relayout;
    Button close = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        colors[0] = Color.parseColor("#37DC74");
        colors[1] = Color.parseColor("#00B945");
        relayout = (RelativeLayout) findViewById(R.id.rl1);
        relayout.setBackgroundColor(Color.parseColor("#FF4E00"));
        tlpara1 = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tlpara2 = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        trpara1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        trpara2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        spin = (Spinner) findViewById(R.id.spin);
        spin2 = (Spinner) findViewById(R.id.spin2);
        tview1 = (TextView) findViewById(R.id.tview1);
        tview1.setText("Select Page Number");
        tview1.setGravity(Gravity.CENTER_VERTICAL);
        tview1.setTypeface(null, Typeface.BOLD);
        tview1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
        tview1.setTextColor(Color.WHITE);
        tview2 = (TextView) findViewById(R.id.tview2);
        tview2.setGravity(Gravity.CENTER_VERTICAL);
        tview2.setTypeface(null, Typeface.BOLD);
        tview2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
        tview2.setText("Sort By");
        tview2.setTextColor(Color.WHITE);
        spin.setOnItemSelectedListener(this);
        tlayout = (TableLayout) findViewById(R.id.tl1);
        list = new ArrayList<String>();
        list2 = new ArrayList<String>();
        list2.add(0, "Inapp Purchase");
        list2.add(1, "Rating");
        list2.add(2, "Date Modified");
        spin2.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (complete == 0) return;
                if (pos == 0)
                    present = sort_iap;
                else if (pos == 1)
                    present = srat;
                else if (pos == 2)
                    present = stime;
                if (spin.getSelectedItemPosition() != 0)
                    spin.setSelection(0);
                else
                    fillTable(0);

            }

            public void onNothingSelected(AdapterView<?> parent) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

        });
        close = (Button) findViewById(R.id.bok);
        close.setText("CLOSE");
        close.setTextColor(Color.WHITE);
        close.setBackgroundColor(Color.parseColor("#A357DE"));
        close.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        close.setGravity(Gravity.CENTER);
        close.setTypeface(null, Typeface.BOLD);
        close.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        nettasks = new NetTasks();
        nettasks.execute(vd);

    }

    class NetTasks extends AsyncTask<Void, Void, Void> {
        ProgressDialog pg = new ProgressDialog(apph);

        @Override
        public void onPreExecute() {
            pg.setTitle("Adobe AppHub");
            pg.setMessage("Collecting product data.\nPlease wait...");
            pg.setCancelable(false);
            pg.show();
        }

        public Void doInBackground(Void... v) {
            try {
                int i = 0, j = 0;
                URL url = new URL(link);
                HttpURLConnection htc = (HttpURLConnection) url.openConnection();
                BufferedReader br = new BufferedReader(new InputStreamReader(htc.getInputStream()));
                String data = "{\"data\":", temp = "";
                while ((temp = br.readLine()) != null)
                    data += temp;
                data += "}";
                JSONObject jso = new JSONObject(data), jtemp;
                JSONArray jarr = jso.getJSONArray("data");
                appdata = new Appdata[jarr.length()];
                sort_iap = new int[appdata.length];
                sort_rat = new double[appdata.length];
                srat = new int[appdata.length];
                sort_time = new long[appdata.length];
                stime = new int[appdata.length];
                for (i = 0; i < jarr.length(); i++) {
                    jtemp = jarr.getJSONObject(i);
                    if (check(jtemp)) {
                        appdata[len] = new Appdata(jarr.getJSONObject(i));
                        if (appdata[len].inapp_purchase.toLowerCase().equals("yes"))
                            yes++;
                        else if (appdata[len].inapp_purchase.toLowerCase().equals("no"))
                            no++;
                        sort_rat[len] = appdata[len].rating;
                        sort_time[len] = appdata[len].tstamp;
                        srat[len] = len;
                        stime[len] = len;

                        len++;
                        Log.v("data #" + i, "\n" + appdata[i].name +
                                "\n" + appdata[i].type +
                                "\n" + appdata[i].url +
                                "\n" + appdata[i].image +
                                "\n" + appdata[i].rating +
                                "\n" + appdata[i].last_updated +
                                "\n" + appdata[i].inapp_purchase +
                                "\n" + appdata[i].description +
                                "\n" + appdata[i].tstamp);
                    }
                }
                caliap();
                sort();
                for (i = 1; i <= len / 7; i++)
                    list.add("Page " + i);
                if (len % 7 != 0)
                    list.add("Page " + i);
            } catch (Exception ert) {
                Log.v("Exception", "" + ert.getMessage());
            }

            return null;
        }

        @Override
        public void onPostExecute(Void vt) {
            ArrayAdapter<String> adapt = new ArrayAdapter<String>(apph, android.R.layout.simple_spinner_item, list);
            adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin.setAdapter(adapt);

            ArrayAdapter<String> adapt2 = new ArrayAdapter<String>(apph, android.R.layout.simple_spinner_item, list2);
            adapt2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin2.setAdapter(adapt2);
            present = sort_iap;
            complete = 1;
            pg.dismiss();
        }
    }

    public boolean check(JSONObject jso) {
        try {
            if (jso == null) return false;
            if (jso.getString("name") == null || jso.getString("name").trim().length() == 0)
                return false;
            if (jso.getString("type") == null) return false;
            if (jso.getString("url") == null || jso.getString("url").trim().length() == 0)
                return false;
            if (jso.getString("image") == null || jso.getString("image").trim().length() == 0)
                return false;
            if (jso.getString("rating") == null) return false;
            if (jso.getString("last updated") == null) return false;
            ;
            if (jso.getString("inapp-purchase") == null) return false;
            if (jso.getString("description") == null) return false;
        } catch (Exception ert) {
            Log.v("CheckingException", "" + ert.getMessage());
            return false;
        }
        return true;
    }

    public void caliap() {
        int i = 0, j = 0, k = len - no;
        for (i = 0; i < len; i++) {
            if (appdata[i].inapp_purchase.toLowerCase().equals("yes")) {
                sort_iap[j] = i;
                j++;
            } else if (appdata[i].inapp_purchase.toLowerCase().equals("no")) {
                sort_iap[k] = i;
                k++;
            }

        }
    }

    public void sort() {
        int i = 0, j = 0, k = 0;
        double d1 = 0;
        long l1 = 0;
        for (i = 1; i < len; i++) {
            d1 = sort_rat[i];
            k = srat[i];
            j = i - 1;
            while (j > -1 && sort_rat[j] < d1) {
                sort_rat[j + 1] = sort_rat[j];
                srat[j + 1] = srat[j];
                j--;
            }
            sort_rat[j + 1] = d1;
            srat[j + 1] = k;
        }

        for (i = 1; i < len; i++) {
            l1 = sort_time[i];
            k = stime[i];
            j = i - 1;
            while (j > -1 && sort_rat[j] < l1) {
                sort_time[j + 1] = sort_time[j];
                stime[j + 1] = stime[j];
                j--;
            }
            sort_time[j + 1] = l1;
            stime[j + 1] = k;
        }
        for (i = 0; i < len; i++)
            Log.v("inapp" + (i + 1), "" + sort_iap[i]);

        for (i = 0; i < len; i++)
            Log.v("rat" + (i + 1), "" + sort_rat[i] + " " + srat[i]);

        for (i = 0; i < len; i++)
            Log.v("time" + (i + 1), "" + sort_time[i] + " " + stime[i]);
    }

    public void onItemSelected(AdapterView<?> aview, View view, int pos, long id) {
        fillTable(pos);
    }

    public void onNothingSelected(AdapterView<?> aview) {

    }

    public void fillTable(int page) {

        int i = 0, j = 0, k = 0, st = 7 * page, size = 0;
        size = len - st >= 7 ? 7 : len - st;
        trow = new TableRow[size];
        Appdata temp = null;
        TextView tv1, tv2;
        for (i = 0; i < trow.length; i++) {
            trow[i] = new TableRow(this);
            trow[i].setLayoutParams(tlpara1);
            temp = appdata[present[st + i]];
            tv1 = new TextView(this);
            tv1.setText(temp.name);
            tv1.setLayoutParams(trpara1);
            tv1.setGravity(Gravity.CENTER_VERTICAL);
            tv1.setTypeface(null, Typeface.BOLD);
            tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv1.setTextColor(Color.WHITE);
            tv2 = new TextView(this);
            tv2.setText("In-App" + temp.inapp_purchase);
            tv2.setLayoutParams(trpara2);
            tv2.setGravity(Gravity.CENTER_VERTICAL);
            tv2.setTypeface(null, Typeface.BOLD);
            tv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tv2.setTextColor(Color.WHITE);
            trow[i].addView(tv1);
            trow[i].addView(tv2);
            trow[i].setTag("" + present[st + i]);
            trow[i].setBackgroundColor(colors[i % 2]);
            trow[i].setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    TableRow tr = (TableRow) v;
                    int x = Integer.parseInt(tr.getTag().toString());
                    Log.v("tag", "" + x);
                    openFragment(appdata[x], appdata[x].name);

                }

            });


        }

        k = tlayout.getChildCount();
        if (k > 0) tlayout.removeAllViews();
        for (i = 0; i < trow.length; i++)
            tlayout.addView(trow[i], tlpara1);

    }

    public void share(String shstr) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shstr);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.Share)));
    }

    public void sendSMS(String sms) {
        Intent smsintent = new Intent();
        smsintent.setAction(Intent.ACTION_VIEW);
        smsintent.putExtra("sms_body", sms);
        smsintent.setType("vnd.android-dir/mms-sms");
        startActivity(smsintent);
    }

    public void openLink(String Link) {
        Intent ix = new Intent(Intent.ACTION_VIEW, Uri.parse(Link));
        startActivity(ix);
    }

    public void openFragment(Appdata ad, String data) {
        String values[] = new String[6];
        Bundle bdata = new Bundle();

        values[0] = ad.name;
        values[1] = ad.type;
        values[2] = "Rating: " + ad.rating;
        values[3] = "Last Updated: " + ad.last_updated;
        values[4] = "Inapp Purchase: " + ad.inapp_purchase;
        values[5] = "Description: " + ad.description;
        bdata.putStringArray(AR_DATA, values);
        bdata.putString(DATA, data);
        bdata.putString(URL, ad.url);
        bdata.putString(IMAGE, ad.image);
        DetailsFragment dfrag = new DetailsFragment();
        dfrag.setArguments(bdata);

        dfrag.show(getSupportFragmentManager(), data);

    }
}
