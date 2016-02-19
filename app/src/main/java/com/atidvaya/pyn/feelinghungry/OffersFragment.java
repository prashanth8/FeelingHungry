package com.atidvaya.pyn.feelinghungry;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import backup.OrderDataClass;


public class OffersFragment extends Fragment
{



    Context context;

    TextView offersTv;

    public OffersFragment()
    {

    }


    public OffersFragment(Context cont)
    {
        context=cont;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_offers,null);
        offersTv= (TextView) view.findViewById(R.id.offerTextId);


        if(OrderDataClass.offer_data.equals("")) {
            new BackgroundTask().execute("");
        }else
        {
            offersTv.setText(OrderDataClass.offer_data);
        }
        return view;
    }



    class BackgroundTask extends AsyncTask<String,Void,String>
    {

        ProgressDialog progressDialog;
        HttpURLConnection urlConnection;
        @Override
        protected void onPreExecute() {

            progressDialog=new ProgressDialog(context);
            progressDialog.setMessage("Please wait");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();


            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            StringBuffer result = new StringBuffer();

            try {
                URL url = new URL("https://feelinghungry.firebaseio.com/TodayOffer.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            }catch( Exception e) {
                e.printStackTrace();
                result.append("error");
            }
            finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }


        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();

            System.out.println(result);

            if(result.equals("null"))
            {
                offersTv.setText("No offers for today!!!");
            }else if(result.equals("error"))
            {

                offersTv.setText("Problem in fetching offers at this moment");
            }else
            {

                try {



                    JSONObject jsonObject=new JSONObject(result);

                    String offersGiven=jsonObject.getString("offer");

                    OrderDataClass.offer_data=offersGiven;
                    offersTv.setText(offersGiven);

                } catch (JSONException e) {
                    e.printStackTrace();
                    offersTv.setText("No offers for today!!@");


                }


            }


            super.onPostExecute(result);
        }
    }


}


