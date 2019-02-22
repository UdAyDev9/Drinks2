package com.uday.drinks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;
import com.uday.drinks.Database.ModelDB.Cart;
import com.uday.drinks.Database.ModelDB.Favourites;
import com.uday.drinks.Utils.Common;

import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavViewHolder> {

    Context context;
    List<Favourites> favouritesList;

    public FavouritesAdapter(Context context, List<Favourites> favouritesList) {
        this.context = context;
        this.favouritesList = favouritesList;
    }

    @NonNull
    @Override
    public FavViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.fav_item_layout,viewGroup,false);
        return new FavouritesAdapter.FavViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavViewHolder holder, int position) {


        Picasso.with(context)
                .load(favouritesList.get(position).link).placeholder(R.drawable.img_loading_placeholder).into(holder.img_product);
        holder.txt_price.setText(new StringBuilder("Rs.").append(favouritesList.get(position).price));
        holder.txt_product_name.setText(favouritesList.get(position).name);

        }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    public class FavViewHolder extends RecyclerView.ViewHolder {

        ImageView img_product;
        TextView txt_product_name,txt_price;
        public RelativeLayout view_background;
        public LinearLayout view_foreground;

        public FavViewHolder(View itemView) {
            super(itemView);
            img_product=(ImageView)itemView.findViewById(R.id.imge_products);
            txt_product_name=(TextView)itemView.findViewById(R.id.txt_product_names);
            txt_price=(TextView)itemView.findViewById(R.id.txt_pricess);
            view_background=(RelativeLayout)itemView.findViewById(R.id.view_background);
            view_foreground=(LinearLayout)itemView.findViewById(R.id.view_foreground);
        }
           }
    public void removeItem(int position){
        favouritesList.remove(position);
        notifyItemRemoved(position);

    }
    public void restoreItem(Favourites item,int position){
        favouritesList.add(position,item);
        notifyItemInserted(position);
    }
}
