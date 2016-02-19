package com.atidvaya.pyn.feelinghungry;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import backup.OrderDataClass;

public class ConfirmOrderActivity extends AppCompatActivity {


    ListView itemsListView;

    Button confirmOrderBtn;
    TextView nameTV;
    MyAdapter ma;

    Firebase rootDataReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_order);

        Firebase.setAndroidContext(ConfirmOrderActivity.this);
        rootDataReference=new Firebase("https://feelinghungry.firebaseio.com/food_orders");
        confirmOrderBtn= (Button) findViewById(R.id.confirmOrderButton);
        itemsListView= (ListView) findViewById(R.id.listView);
        nameTV= (TextView) findViewById(R.id.userNameTv);
        nameTV.setText("Hi "+OrderDataClass.finalOrderList.get(0).getName()+",");
        ma=new MyAdapter();
        itemsListView.setAdapter(ma);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pDialog=new ProgressDialog(ConfirmOrderActivity.this);
                pDialog.setMessage("Please wait while we take your order");
                pDialog.setIndeterminate(true);
                pDialog.setCancelable(false);

                pDialog.show();

                for(int i=0;i<OrderDataClass.finalOrderList.size();i++)
                {
                    rootDataReference.push().setValue(OrderDataClass.finalOrderList.get(i));
                }



                rootDataReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                    pDialog.dismiss();

                        AlertDialog.Builder builder=new AlertDialog.Builder(ConfirmOrderActivity.this);
                        builder.setTitle("Success");
                        builder.setMessage("Your order has been taken. Thank you");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });

                        AlertDialog alert=builder.create();
                        alert.show();



                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError)
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(ConfirmOrderActivity.this);
                        builder.setTitle("Something went wrong!!");
                        builder.setMessage("We are not able to take your order at this moment. Please try again");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog alert=builder.create();
                        alert.show();

                    }
                });




            }
        });










    }



    class MyAdapter extends BaseAdapter
    {

        TextView food_nameTv,quantity_priceTv,priceTv;


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
        public View getView(int position, View convertView, ViewGroup parent) {

            View view=getLayoutInflater().inflate(R.layout.custom_items_ordered_row,null);
            food_nameTv= (TextView) view.findViewById(R.id.itemName);
            quantity_priceTv= (TextView) view.findViewById(R.id.itemQuantityAndPrice);
            priceTv= (TextView) view.findViewById(R.id.itemTotalPrice);


            food_nameTv.setText(OrderDataClass.finalOrderList.get(position).food_item_name);
            quantity_priceTv.setText("( "+OrderDataClass.finalOrderList.get(position).getQuantity()+" * "+OrderDataClass.finalOrderList.get(position).getFood_item_price_per_unit()+")");
            priceTv.setText("= "+OrderDataClass.finalOrderList.get(position).getTotal_price()+" Rs");


            return view;
        }
    }


}
