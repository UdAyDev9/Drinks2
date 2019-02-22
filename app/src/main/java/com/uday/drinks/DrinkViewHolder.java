package com.uday.drinks;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.uday.drinks.Interface.ItemClickListener;

public class DrinkViewHolder  extends RecyclerView.ViewHolder  implements  View.OnClickListener {

    ImageView img_product;
    TextView txt_drink_name,txt_price;
    ImageButton imgBtnAddCart,imgBtnAddFav;

    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public DrinkViewHolder(View itemView) {
        super(itemView);
        img_product=(ImageView)itemView.findViewById(R.id.image_products);
        txt_drink_name=(TextView)itemView.findViewById(R.id.txt_drink_name);
        txt_price=(TextView)itemView.findViewById(R.id.txt_price);
        imgBtnAddCart =(ImageButton) itemView.findViewById(R.id.img_btn_add_cart);
        imgBtnAddFav =(ImageButton) itemView.findViewById(R.id.img_btn_add_fav);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v);


    }
}
