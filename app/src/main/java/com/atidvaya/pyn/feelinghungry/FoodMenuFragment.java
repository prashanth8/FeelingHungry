package com.atidvaya.pyn.feelinghungry;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backup.OrderDataClass;


public class FoodMenuFragment extends Fragment {
    Context context;

    List<Map<String,String>> foodList;

    MyAdapter ma;



    ListView foodMenuListView;


    public FoodMenuFragment()
    {

    }


    public FoodMenuFragment(Context cont)
    {
        context=cont;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_food_menu,null);
        foodMenuListView= (ListView) view.findViewById(R.id.foodMenuListViewId);
      foodList=new ArrayList<Map<String,String>>();


       ma=new MyAdapter();
        foodMenuListView.setAdapter(ma);



        if(OrderDataClass.foodList.size()==0) {
            new BackgroundTask().execute("");
        }
        else
        {
            this.foodList=OrderDataClass.foodList;
            ma.notifyDataSetChanged();
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
                URL url = new URL("https://feelinghungry.firebaseio.com/food_menu.json");
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
            if(result.equals("null"))
            {

            }else if(result.equals("error"))
            {


            }else
            {

                try {



                    JSONObject jsonObject=new JSONObject(result);

                    JSONArray jsonArray=jsonObject.getJSONArray("food_menu");

                    for(int i=0;i<jsonArray.length();i++)
                    {

                      JSONObject locJsonObj=jsonArray.getJSONObject(i);

                       String name= locJsonObj.getString("name").trim();
                        String price= locJsonObj.getString("price").trim();

                     Map<String,String> mapObj=new HashMap<String,String>();
                        mapObj.put("name",name);
                        mapObj.put("price",price);

                        foodList.add(mapObj);
                        ma.notifyDataSetChanged();



                    }

                    OrderDataClass.foodList=foodList;


                } catch (JSONException e) {
                    e.printStackTrace();


                }


            }





            super.onPostExecute(result);
        }
    }

    class MyAdapter extends BaseAdapter
    {

        TextView foodSrNoTv,foodNameTv,foodPriceTv;
        ImageView deleteImageButon;

        @Override
        public int getCount() {
            return foodList.size();
        }

        @Override
        public Object getItem(int position) {
            return foodList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            View view=getActivity().getLayoutInflater().inflate(R.layout.custom_food_menu_item_row,null);
            foodSrNoTv= (TextView) view.findViewById(R.id.foodserialNoTvId);
            foodNameTv= (TextView) view.findViewById(R.id.foodNameTvId);
            foodPriceTv= (TextView) view.findViewById(R.id.foodPriceTvId);


            foodSrNoTv.setText(position+1+".");
            Map<String,String> mapObj=foodList.get(position);
            String name=mapObj.get("name");
            String price=mapObj.get("price");

            foodNameTv.setText(name);
            foodPriceTv.setText(price+" Rs");




            return view;
        }
    }
}
