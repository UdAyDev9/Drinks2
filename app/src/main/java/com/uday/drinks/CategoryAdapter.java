package com.uday.drinks;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.uday.drinks.Model.Category;
import com.squareup.picasso.Picasso;
import com.uday.drinks.Utils.Common;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewholder> {
    List<Category> categories;
    Context context;

    public CategoryAdapter(List<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(context).inflate(R.layout.menu_item_layout,parent,false);
        return new CategoryViewholder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewholder holder, final int position) {

        Picasso.with(context).load(categories.get(position).getLink()).into(holder.img_product);
        holder.text_menu_name.setText(categories.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
                Common.currentCategory=categories.get(position);
                // start new Activity
                Intent intentDrinks = new Intent(context,DrinkActivity.class);
                intentDrinks.putExtra("menu_drink_img",categories.get(position).getLink().toString());
                intentDrinks.putExtra("menu_drink_name",categories.get(position).getName().toString());
                context.startActivity(intentDrinks);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
