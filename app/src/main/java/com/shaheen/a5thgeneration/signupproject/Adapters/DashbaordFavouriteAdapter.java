package com.shaheen.a5thgeneration.signupproject.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shaheen.a5thgeneration.signupproject.DataModels.FavouriteModel;
import com.shaheen.a5thgeneration.signupproject.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Shani on 10/29/2017.
 */

public class DashbaordFavouriteAdapter  extends RecyclerView.Adapter<DashbaordFavouriteAdapter.RecycleHolder> {

    Context context;
    ArrayList<FavouriteModel> arrayList = new ArrayList<>();

    public DashbaordFavouriteAdapter(ArrayList<FavouriteModel> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public DashbaordFavouriteAdapter.RecycleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_dash_row,parent,false);
        DashbaordFavouriteAdapter.RecycleHolder recyclerHolder = new DashbaordFavouriteAdapter.RecycleHolder(view);
        return recyclerHolder;
    }

    @Override
    public void onBindViewHolder(DashbaordFavouriteAdapter.RecycleHolder holder, int position) {

        FavouriteModel favouriteModel = arrayList.get(position);

        Glide.with(getApplicationContext()).load(favouriteModel.getImage()).into(holder.imageView);
        holder.name.setText(favouriteModel.getName());



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RecycleHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name;

        public RecycleHolder(View itemView) {
            super(itemView);

            imageView = (CircleImageView)itemView.findViewById(R.id.newImageFavourite);
            name = (TextView) itemView.findViewById(R.id.name);



        }
    }
}
