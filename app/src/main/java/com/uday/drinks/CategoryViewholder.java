package com.uday.drinks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CategoryViewholder extends RecyclerView.ViewHolder {

    ImageView img_product;
    TextView text_menu_name;

    public CategoryViewholder(View itemView) {
        super(itemView);

        img_product = (ImageView) itemView.findViewById(R.id.image_product);
        text_menu_name = (TextView) itemView.findViewById(R.id.txt_menu_name);

    }
}
