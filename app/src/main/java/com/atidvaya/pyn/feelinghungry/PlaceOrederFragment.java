package com.atidvaya.pyn.feelinghungry;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import backup.OrderDataClass;


public class PlaceOrederFragment extends Fragment {


    Context context;

    Spinner deliveryPointSpinner,foodMenuSpinner,quantitySpinner;
    Button registerBtn;

    CoordinatorLayout coordinatorLayout;

    ArrayList<String> deliveryPointList,foodMenuList,quantityList;
    ArrayAdapter<String> da,fa,qa;
    MyAdapter ma;


    String name="";
    String mobile_number="";
    String food_item_name="";
    String quantity="";
    String delivery_point="";
    String date_time="";
    String total_price="";
    String food_item_price_per_unit="";

    SharedPreferences user_details_sp;


    ImageView addItemBtnId;
    LinearLayout selectedFoodItemsLayoutId;
    ListView foodMenuListViewId;





    public PlaceOrederFragment()
    {

    }


    public PlaceOrederFragment(Context cont)
    {
        context=cont;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view=getActivity().getLayoutInflater().inflate(R.layout.fragment_place_oreder,null);
        deliveryPointSpinner= (Spinner) view.findViewById(R.id.deliveryPointspinner);
        foodMenuSpinner= (Spinner) view.findViewById(R.id.foodMenuSpinner);
        quantitySpinner= (Spinner) view.findViewById(R.id.quantitySpinner);
        registerBtn= (Button) view.findViewById(R.id.orderButtonId);
        coordinatorLayout= (CoordinatorLayout) view.findViewById(R.id.main_content);
        addItemBtnId= (ImageView) view.findViewById(R.id.addItemBtnId);
        selectedFoodItemsLayoutId= (LinearLayout) view.findViewById(R.id.selectedFoodItemsLayoutId);
       if(OrderDataClass.finalOrderList.size()>0)
       {
           selectedFoodItemsLayoutId.setVisibility(View.VISIBLE);
       }


        foodMenuListViewId= (ListView) view.findViewById(R.id.foodMenuListViewId);
        ma=new MyAdapter();
        foodMenuListViewId.setAdapter(ma);


        deliveryPointList=new ArrayList<String>();





        foodMenuList=new ArrayList<String>();
        quantityList=new ArrayList<String>();

        user_details_sp=context.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        name=user_details_sp.getString("name", "Hero");
        mobile_number=user_details_sp.getString("mobile_number","0000");

        deliveryPointList.add("Manyata Embassy Business Park: G1 block cab area");

        for(int i=0;i<OrderDataClass.foodList.size();i++) {

            foodMenuList.add(OrderDataClass.foodList.get(i).get("name"));
        }


        foodMenuList.add(0,"-- Select --");

        for(int i=1;i<=10;i++) {
            quantityList.add(i+"");
        }

        da=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,deliveryPointList);
        fa=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,foodMenuList);
        qa=new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,quantityList);


        deliveryPointSpinner.setAdapter(da);
        foodMenuSpinner.setAdapter(fa);
        quantitySpinner.setAdapter(qa);







        deliveryPointSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                delivery_point = deliveryPointList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        foodMenuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position > 0) {
                    food_item_name = foodMenuList.get(position);

                      food_item_price_per_unit=OrderDataClass.foodList.get(position-1).get("price");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                quantity = quantityList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        addItemBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy");
                date_time = dateformat.format(c.getTime());

                OrderDataClass.today_date=date_time;

                System.out.println(date_time);


                if (food_item_name.equals("")) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Please select the food from Food menu", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {


                    int quant = Integer.parseInt(quantity);
                    total_price = String.valueOf(Integer.parseInt(food_item_price_per_unit) * quant);

                    Toast.makeText(context,"Item added ",Toast.LENGTH_SHORT).show();

                    Orders orders = new Orders(name, mobile_number, food_item_name, quantity, date_time,delivery_point , total_price, food_item_price_per_unit);
                    OrderDataClass.finalOrderList.add(orders);
                    ma.notifyDataSetChanged();

                }
                selectedFoodItemsLayoutId.setVisibility(View.VISIBLE);








            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    Intent intent = new Intent(context, ConfirmOrderActivity.class);
                    context.startActivity(intent);





            }
        });

        return view;
    }




    class MyAdapter extends BaseAdapter
    {

        TextView foodSrNoTv,foodNameTv,foodPriceTv;
        ImageView deleteImageButon;

        @Override
        public int getCount() {
            return OrderDataClass.finalOrderList.size();
        }

        @Override
        public Object getItem(int position) {
            return OrderDataClass.finalOrderList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            View view=getActivity().getLayoutInflater().inflate(R.layout.custom_food_order_item_row,null);
            foodSrNoTv= (TextView) view.findViewById(R.id.foodserialNoTvId);
            foodNameTv= (TextView) view.findViewById(R.id.foodNameTvId);
            foodPriceTv= (TextView) view.findViewById(R.id.foodPriceTvId);
            deleteImageButon= (ImageView) view.findViewById(R.id.foodDeleteId);

            foodSrNoTv.setText(position + 1 + ".");




            String name=OrderDataClass.finalOrderList.get(position).getFood_item_name();
            String price=OrderDataClass.finalOrderList.get(position).getFood_item_price_per_unit();

            String quantity=OrderDataClass.finalOrderList.get(position).getQuantity();
            String total_price=OrderDataClass.finalOrderList.get(position).getTotal_price();


            foodNameTv.setText(name);
            foodPriceTv.setText(quantity+"*"+OrderDataClass.finalOrderList.get(position).getFood_item_price_per_unit()+" = "+total_price+" Rs");


            deleteImageButon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    OrderDataClass.finalOrderList.remove(position);

                    if (OrderDataClass.finalOrderList.size() == 0) {
                        selectedFoodItemsLayoutId.setVisibility(View.GONE);
                    }


                    System.out.println(OrderDataClass.finalOrderList);

                    ma.notifyDataSetChanged();
                }
            });



            return view;
        }
    }





}
