package com.gpd.gpdimg.bin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gpd.gpdimg.R;
import com.gpd.gpdimg.activity.ImageCapture;
import com.gpd.gpdimg.bin.data.model.CurrencyConvertModel;

import java.util.ArrayList;

public class CurrencyRateMasterAdapter extends RecyclerView.Adapter<CurrencyRateMasterAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CurrencyConvertModel> currencyList;
    private int from;

    public CurrencyRateMasterAdapter(Context context, ArrayList<CurrencyConvertModel> currencyList, int from) {
        this.context = context;
        this.currencyList = currencyList;
        this.from=from;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.layout_currency_country_list,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        Typeface rr = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
//        holder.tx_currency_name.setTypeface(rr);
        holder.tx_currency_name.setText(currencyList.get(position).getCurrency_master_code() + " " + currencyList.get(position).getCurrency_master_name_en());
//        Picasso.with(context)
//                .load(Config.flag_url1 + currencyList.get(position).getGe_countries_2_code().toLowerCase() + ".png")
////                .load(Config.flag_url1 + currencyList.get(position).getCurrency_master_name_en().toLowerCase() + currencyList.get(position).getGe_countries_2_code().toLowerCase() + ".png")
//                .placeholder(R.drawable.default_image)   // optional
//                .error(R.drawable.default_image)      // optional
//                .into(holder.img);

        holder.ll_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from==1){
                    ((ImageCapture) context).setCountryFromrate(currencyList.get(position).getCurrency_master_name_en(), currencyList.get(position).getCurrency_master_code()
                            );
//                            currencyList.get(position).getCurrency_master_code());
                }
                if(from==2){
                    ((ImageCapture) context).setCountryTorate(currencyList.get(position).getCurrency_master_name_en(),  currencyList.get(position).getCurrency_master_code());
                }
            }
        });
    }





    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tx_currency_name;
        RelativeLayout ll_list;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tx_currency_name=itemView.findViewById(R.id.tx_item_name);
            ll_list=itemView.findViewById(R.id.ll_list_country);

        }
    }

    public void filterList(ArrayList<CurrencyConvertModel> filteredList) {
        currencyList = filteredList;
        notifyDataSetChanged();
    }



}

