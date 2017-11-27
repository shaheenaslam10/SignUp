package com.shaheen.a5thgeneration.signupproject.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shaheen.a5thgeneration.signupproject.DataModels.SearchTripData;
import com.shaheen.a5thgeneration.signupproject.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Shani on 11/15/2017.
 */

public class Myadapter extends RecyclerView.Adapter<Myadapter.RecycleHolder> {

    public ArrayList<SearchTripData> arrayList;
    public Context context;

    public Myadapter(ArrayList<SearchTripData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_trip_traveling_row,parent,false);
        Myadapter.RecycleHolder recyclerHolder = new Myadapter.RecycleHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(RecycleHolder holder, int position) {

        SearchTripData data = arrayList.get(position);

        holder.usernam.setText(data.getUsernam());
        holder.address.setText(data.getAddress());
        holder.date.setText(data.getDate());
        holder.pay.setText(data.getPay());
        Glide.with(getApplicationContext()).load(data.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecycleHolder extends RecyclerView.ViewHolder {

        public CircleImageView image;
        public TextView usernam;
        public TextView address;
        public TextView date;
        public TextView pay;

        public RecycleHolder(View itemView) {
            super(itemView);

            image = (CircleImageView) itemView.findViewById(R.id.row_image);
            usernam = (TextView) itemView.findViewById(R.id.row_name);
            address = (TextView) itemView.findViewById(R.id.row_address);
            date = (TextView) itemView.findViewById(R.id.row_date);
            pay = (TextView) itemView.findViewById(R.id.row_pay);



        }
    }
}
