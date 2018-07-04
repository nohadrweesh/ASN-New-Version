package com.example.a.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import  android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Belal on 10/18/2017.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {


    private Context mCtx;
    private List<Product> productList;
    SellUtils sellUtils;
    RelativeLayout mRelativeLayout;
    Button mButton;
    Activity mActivity;
    PopupWindow mPopupWindow;

    TextView engcool;
    TextView trouble;
    TextView longterm;
    TextView intake;
    ProgressDialog progressDialog;

    public ProductsAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cars_list, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        //loading the image


        holder.model.setText(Html.fromHtml("<b>" + "Model :" + "</b> " + product.getModel()));
        holder.price.setText(Html.fromHtml("<b>" + "Price :" + "</b> " +String.valueOf(product.getPrice())));
        //  holder.status.setText(Html.fromHtml("<b>" + "Status :" + "</b> " +String.valueOf(product.getStatus())));
        // holder.soldbought.setText(String.valueOf(product.getSoldBought()));
        holder.ownerPhone.setText(Html.fromHtml("<b>" + "Phone :" + "</b> " +String.valueOf(product.getOwnerPhone())));
        holder.ownerName.setText(Html.fromHtml("<b>" + "Seller :" + "</b> " +String.valueOf(product.getOwnerName())));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView model, price, status, /*soldbought,*/ownerName,ownerPhone;
        Button btn_details, btn_buy;


        public ProductViewHolder(View itemView) {
            super(itemView);

            ownerPhone = itemView.findViewById(R.id.textownerphone);
            model = itemView.findViewById(R.id.textmodel);
            price = itemView.findViewById(R.id.textprice);

            btn_details = (Button) itemView.findViewById(R.id.btndetails);
            btn_buy = (Button) itemView.findViewById(R.id.btnsell);
            // soldbought = itemView.findViewById(R.id.textsoldbought);
            ownerName = itemView.findViewById(R.id.textownername);
            btn_details.setTag(R.integer.btn_details_view, itemView);
            btn_buy.setTag(R.integer.btn_buy_view, itemView);
            btn_details.setOnClickListener(this);
            btn_buy.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == btn_details.getId()){

                View tempview = (View) btn_details.getTag(R.integer.btn_details_view);
                mRelativeLayout = (RelativeLayout) tempview.findViewById(R.id.rl);
                LayoutInflater inflater = (LayoutInflater) mCtx.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.custom_layout,null);


                mPopupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);


                }

                // Get a reference for the custom view close button
                ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);
                engcool = (TextView)customView.findViewById(R.id.engcool);
                trouble = (TextView)customView.findViewById(R.id.trouble);
                longterm = (TextView)customView.findViewById(R.id.longterm);
                intake = (TextView)customView.findViewById(R.id.intake);
                progressDialog = new ProgressDialog(mCtx);
                // Set a click listener for the popup window close button
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window
                        mPopupWindow.dismiss();
                    }
                });
                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_RETRIEVEOBD,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d(TAG, "onOBD Response: starts");
                                progressDialog.dismiss();
                                try {
                                    //   JSONArray array = new JSONArray(response);
                                    Log.d(TAG, "onResponse: "+response);
                                    JSONArray jsonArray = new JSONArray(response);
                                    for(int i=0;i<jsonArray.length();i++)
                                    {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                        engcool.setText(Html.fromHtml("<b>" + "coolant: " + "</b> " +jsonObject1.optString("EngineCoolantTemperature")+"<b>" + "  decision: " + "</b> "+jsonObject1.optString("decisionEngineCoolantTemperature")));
                                        trouble.setText(Html.fromHtml("<b>" + "trouble code: " + "</b> "+ jsonObject1.optString("TroubleCodes")));
                                        longterm.setText(Html.fromHtml("<b>" + "long termfuel: " + "</b> " +jsonObject1.optString("LongTermFuelTrimBank1")+"<b>" + "  decision: " + "</b> "+jsonObject1.optString("decisionLongTermFuelTrimBank1")));
                                        intake.setText(Html.fromHtml("<b>" + "Intake Manifold: " + "</b> " +jsonObject1.optString("IntakeManifoldPressure")+"<b>" + "  decision: " + "</b> "+jsonObject1.optString("decisionIntakeManifoldPressure")));


                                    }





                                } catch (JSONException e) {
                                    Log.d(TAG, "onResponse: error"+response);
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "onErrorResponse: starts");
                                progressDialog.dismiss();

                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Log.d(TAG, "getParams: starts with ");
                        Map<String, String> params = new HashMap<>();
                        params.put("carId",productList.get(getAdapterPosition()).getCarID() );


                        return params;
                    }

                };

                RequestHandler.getInstance(mCtx).addToRequestQueue(stringRequest);


                customView.measure(View.MeasureSpec.makeMeasureSpec(tempview.getWidth(), View.MeasureSpec.AT_MOST), View.MeasureSpec.UNSPECIFIED);
                mPopupWindow.setWidth(customView.getMeasuredWidth());
                mPopupWindow.setHeight(600);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);
                mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);




            }
            else  if (v.getId() == btn_buy.getId()){

                sellUtils=SellUtils.getInstance(mCtx);
                Product p=productList.get(getAdapterPosition());
                Log.d("OWNER",p.getOwnerName());
                Log.d("MY IDF",String.valueOf(SharedPrefManager.getInstance(mCtx)
                        .getUserId()));
                sellUtils.sendbuy(productList.get(getAdapterPosition()),p.getModel()+" car "+"with price "+p.getPrice());
                Intent int2 = new Intent(mCtx, CBHome.class);
                mCtx.startActivity(int2);
                Toast.makeText(mCtx,"Request Sent to seller with phone "+SharedPrefManager.getInstance(mCtx).getUserphone(),Toast.LENGTH_SHORT).show();
            }


        }
    }
}
