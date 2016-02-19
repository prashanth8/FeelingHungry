package com.atidvaya.pyn.feelinghungry;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
import java.util.Iterator;
import java.util.Map;

import backup.OrderDataClass;


public class MyOrdersFragment extends Fragment
{



    Context context;

    ListView orderListViewId;

    ArrayList<Orders> myOrdersList;

    MyOrdersAdapter ma;

    public MyOrdersFragment()
    {

    }


    public MyOrdersFragment(Context cont)
    {
        context=cont;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_my_orders,null);
        orderListViewId= (ListView) view.findViewById(R.id.orderListViewId);


        myOrdersList=new ArrayList<Orders>();
        ma=new MyOrdersAdapter();

        orderListViewId.setAdapter(ma);


            new BackgroundTask().execute("");



            return view;
    }



    class MyOrdersAdapter extends BaseAdapter
    {

        TextView food_nameTv,quantityTv,deliveryPointTv,totalPricetv;


        @Override
        public int getCount() {
            return myOrdersList.size();
        }

        @Override
        public Object getItem(int position) {
            return myOrdersList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view=getActivity().getLayoutInflater().inflate(R.layout.custom_my_order_row, null);
            food_nameTv= (TextView) view.findViewById(R.id.foodTitleEt);
            quantityTv= (TextView) view.findViewById(R.id.foodQuantityTvId);
            deliveryPointTv= (TextView) view.findViewById(R.id.foodDeliveryPointTvId);
            totalPricetv= (TextView) view.findViewById(R.id.foodTotalPriceTvId);


          food_nameTv.setText(myOrdersList.get(position).getFood_item_name());
            quantityTv.setText(myOrdersList.get(position).getQuantity());
            deliveryPointTv.setText(myOrdersList.get(position).getDelivery_point());
            totalPricetv.setText("Total = "+myOrdersList.get(position).getTotal_price()+" Rs");





            System.out.println("Position:" + position);




            return view;
        }
    }









    class BackgroundTask extends AsyncTask<String,Void,String> {

        ProgressDialog progressDialog;
        HttpURLConnection urlConnection;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(context);
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
                URL url = new URL("https://feelinghungry.firebaseio.com/food_orders.json");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
                result.append("error");
            } finally {
                urlConnection.disconnect();
            }


            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result.equals("null")) {

            } else if (result.equals("error")) {


            } else {

                try {


                    JSONObject mainObject = new JSONObject(result);

                    Iterator<String> keysOfObj = mainObject.keys();

                    while (keysOfObj.hasNext()) {
                        String key = keysOfObj.next();

                        JSONObject localJObj = mainObject.getJSONObject(key);

                        System.out.println(localJObj.toString());

                        String name=localJObj.getString("name").trim();
                        String mobile_number=localJObj.getString("mobile_number").trim();
                        String date_time=localJObj.getString("date_time").trim();

                        String delivery_point=localJObj.getString("delivery_point");
                        String total_price=localJObj.getString("total_price");
                        String quantity=localJObj.getString("quantity");
                        String food_item_name=localJObj.getString("food_item_name");

                        if(name.equals(OrderDataClass.user_name)&&mobile_number.equals(OrderDataClass.mobile_number)&&date_time.equals(OrderDataClass.today_date))
                        {

                            Orders myOrderObj=new Orders(name,mobile_number,food_item_name,quantity,date_time,delivery_point,total_price,"");

                            myOrdersList.add(myOrderObj);

                            ma.notifyDataSetChanged();

                        }else
                        {
                            System.out.println("No orders matched");
                        }






                    }

                  ma.notifyDataSetChanged();
                    OrderDataClass.myOrderList=myOrdersList;
                    orderListViewId.setVisibility(View.VISIBLE);

                    System.out.println("My orders are:"+myOrdersList.size());




                } catch (JSONException e) {
                    e.printStackTrace();
                }


                super.onPostExecute(result);
            }
        }


    }
}
