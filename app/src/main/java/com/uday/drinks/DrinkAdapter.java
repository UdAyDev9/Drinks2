package com.uday.drinks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.uday.drinks.Interface.ItemClickListener;
import com.uday.drinks.Model.Drink;
import com.uday.drinks.Utils.Common;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {
    Context context;
    List<Drink> drinkList;


    public DrinkAdapter(Context context, List<Drink> drinkList) {
        this.context = context;
        this.drinkList = drinkList;
    }

    @NonNull
    @Override
    public DrinkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.drink_item_layout,null);
        return new DrinkViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkViewHolder holder, final int position) {
        Picasso.with(context).load(drinkList.get(position).getLink()).placeholder(R.drawable.banner1).into(holder.img_product);
        holder.txt_price.setText(new StringBuilder("$").append(drinkList.get(position).Price).toString());
        holder.txt_drink_name.setText(drinkList.get(position).Name);
        holder.btn_add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowAddToCartDialog(position);
            }
        });

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();

                Log.d("verify", "showRegisterDialog: click");
                final AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle(drinkList.get(position).getName());

                View imgViewLayout=LayoutInflater.from(context).inflate(R.layout.img_view_dialog,null);

                ImageView imgPreview = (ImageView)imgViewLayout.findViewById(R.id.img_preview);

                Picasso.with(context).load(drinkList.get(position).getLink().toString()).into(imgPreview);

                builder.setView(imgViewLayout);


                final AlertDialog dialog=builder.create();

                dialog.show();


            }
        });




    }

    private void ShowAddToCartDialog(final int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View itemView=LayoutInflater.from(context).inflate(R.layout.add_to_cart_layout,null);
        ImageView imageView=(ImageView)itemView.findViewById(R.id.img_cart_product);

        final ElegantNumberButton txt_count=(ElegantNumberButton)itemView.findViewById(R.id.txt_count);
        final TextView txt_product_dialog=(TextView)itemView.findViewById(R.id.txt_cart_product_name);
        EditText editText=(EditText)itemView.findViewById(R.id.edt_comment);

        RadioButton rdi_sizeL=(RadioButton)itemView.findViewById(R.id.rdi_sizeL);
        RadioButton rdi_sizeM=(RadioButton)itemView.findViewById(R.id.rdi_sizeM);

        rdi_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sizeofcup=0;
                }

            }

        });
        rdi_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Common.sizeofcup=1;
                }
            }
        });

        RadioButton rdi_sugar_100=(RadioButton)itemView.findViewById(R.id.rdi_sugar_100);
        RadioButton rdi_sugar_70=(RadioButton)itemView.findViewById(R.id.rdi_sugar_70);
        RadioButton rdi_sugar_50=(RadioButton)itemView.findViewById(R.id.rdi_sugar_50);
        RadioButton rdi_sugar_30=(RadioButton)itemView.findViewById(R.id.rdi_sugar_30);
        RadioButton rdi_sugar_free=(RadioButton)itemView.findViewById(R.id.rdi_free);

        rdi_sugar_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sugar=100;
                }

            }
        });
        rdi_sugar_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sugar=70;
                }

            }
        });
        rdi_sugar_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sugar=50;
                }

            }
        });
        rdi_sugar_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sugar=30;
                }

            }
        });
        rdi_sugar_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sugar=0;
                }

            }
        });


        RadioButton rdi_ice_100=(RadioButton)itemView.findViewById(R.id.rdi_ice_100);
        RadioButton rdi_ice_70=(RadioButton)itemView.findViewById(R.id.rdi_ice_70);
        RadioButton rdi_ice_50=(RadioButton)itemView.findViewById(R.id.rdi_ice_50);
        RadioButton rdi_ice_30=(RadioButton)itemView.findViewById(R.id.rdi_ice_30);
        RadioButton rdi_ice_free=(RadioButton)itemView.findViewById(R.id.rdi_ice_free);

        rdi_ice_100.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Common.ice=100;
                }

            }
        });
        rdi_ice_70.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Common.ice=70;
                }

            }
        });
        rdi_ice_50.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Common.ice=50;
                }

            }
        });
        rdi_ice_30.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Common.ice=30;
                }

            }
        });
        rdi_ice_free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Common.ice=0;
                }

            }
        });

        RecyclerView recycler_topping=(RecyclerView)itemView.findViewById(R.id.recycler_topping);
        recycler_topping.setLayoutManager(new LinearLayoutManager(context));
        recycler_topping.setHasFixedSize(true);



        MultiChoiceAdapter multiChoiceAdapter=new MultiChoiceAdapter(context,Common.toppingList);


        recycler_topping.setAdapter(multiChoiceAdapter);

        //set data
        Picasso.with(context)
                .load(drinkList.get(position).Link)
                .into(imageView);
        txt_product_dialog.setText(drinkList.get(position).Name);
        builder.setView(itemView);
        builder.setNegativeButton("Add TO Cart", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(Common.sizeofcup==-1){
                    Toast.makeText(context, "Please select size of the cup", Toast.LENGTH_SHORT).show();
                    
                    return;


                }
                if (Common.sugar==-1){
                    Toast.makeText(context, "Please choose Sugar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Common.ice==-1){
                    Toast.makeText(context, "Please choose Ice", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialogInterface.dismiss();
                showConfirmDialog(position,txt_count.getNumber(),Common.sizeofcup,Common.sugar,Common.ice);
            }
        });
        builder.show();
    }

    private void showConfirmDialog(int position, String number, int sizeofcup, int sugar, int ice) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View itemView=LayoutInflater.from(context).inflate(R.layout.confirm_add_cart_layout,null);

        //View
        ImageView img_product_dialog=(ImageView)itemView.findViewById(R.id.img_product);
        TextView txt_product_dialog=(TextView)itemView.findViewById(R.id.txt_cart_product_name);
        TextView txt_product_price=(TextView)itemView.findViewById(R.id.txt_cart_product_price);
        TextView txt_sugar=(TextView)itemView.findViewById(R.id.txt_sugar);
        TextView txt_ice=(TextView)itemView.findViewById(R.id.txt_ice);
        TextView txt_toppingExtra=(TextView)itemView.findViewById(R.id.txt_topping_extra);

        //set Data
        Picasso.with(context).load(drinkList.get(position).Link).into(img_product_dialog);
        txt_product_dialog.setText(new StringBuilder(drinkList.get(position).Name).append(" x")
        .append(number)
        .append(Common.sizeofcup==0 ? "Size M":" Size L").toString());
        txt_ice.setText(new StringBuilder("Ice: ").append(Common.ice).append("%").toString());
        txt_sugar.setText(new StringBuilder("Sugar: ").append(Common.sugar).append("%").toString());
        double price=(Double.parseDouble(drinkList.get(position).Price)*Double.parseDouble(number))+Common.toppingprice;

        if(Common.sizeofcup==1)

        txt_product_price.setText(new StringBuilder("$").append(price));

        StringBuilder topping_final_comment=new StringBuilder("");
        for(String line:Common.toppingAdded)
            topping_final_comment.append(line).append("\n");
        txt_toppingExtra.setText(topping_final_comment);

        builder.setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setView(itemView);
        builder.show();
    }


    @Override
    public int getItemCount() {
        return drinkList.size();
    }
}
