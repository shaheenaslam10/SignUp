/*
package com.shaheen.a5thgeneration.signupproject.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shaheen.a5thgeneration.signupproject.Fragments.MainFragment;
import com.shaheen.a5thgeneration.signupproject.R;
import com.shaheen.a5thgeneration.signupproject.UsersData;

import org.w3c.dom.UserDataHandler;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

*/
/**
 * Created by Shani on 11/27/2017.
 *//*


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> implements Adapter {

    private List<UsersData> ContactList;
    View itemView;
    Context conn;

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return ContactList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        View rowView =  view;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        rowView = inflater.inflate(R.layout.swipable_item, parent, false);
        // configure view holder
        viewHolder = new MainFragment.MyAppAdapter.ViewHolder();
        // viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
        viewHolder.infoImage = (ImageView) rowView.findViewById(R.id.information);
        viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
        viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
        rowView.setTag(viewHolder);
    }

    @Override
    public int getViewTypeCount() {
        return ContactList.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public FrameLayout background;
        public ImageView cardImage;
        public ImageView infoImage;


        public MyViewHolder(View rowView) {
            super(rowView);

           infoImage = (ImageView) rowView.findViewById(R.id.information);
            background = (FrameLayout) rowView.findViewById(R.id.background);
           cardImage = (ImageView) rowView.findViewById(R.id.cardImage);

        }
    }


    public UsersAdapter(List<UsersData> ContactList, Context con) {

        this.ContactList = ContactList;
        this.conn = con;
        Log.d("SHAN","size"+ContactList.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.swipable_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



        String imgURL=(ContactList.get(position).getImagePath());
        Glide.with(getApplicationContext()).load(imgURL).fitCenter().into(holder.cardImage);



    }

    @Override
    public int getItemCount() {
        return ContactList.size();
    }
}*/
