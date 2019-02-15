package com.uday.drinks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uday.drinks.Model.Banner;
import com.uday.drinks.Model.Drink;
import com.uday.drinks.Retrofit.IDrinkShopAPI;
import com.uday.drinks.Utils.Common;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DrinkActivity extends AppCompatActivity {
    IDrinkShopAPI mService;
    ImageView drinkHeaderImg;
    RecyclerView lst_drink;
    TextView banner_name,drinkHeaderText;

    //RxJAva
    //call back methods
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink);
        mService= Common.getAPI();
        lst_drink=(RecyclerView)findViewById(R.id.recycler_drinks);
        lst_drink.setLayoutManager(new GridLayoutManager(this,2));
        lst_drink.setHasFixedSize(true);
        banner_name=(TextView)findViewById(R.id.txt_drink_name);
        drinkHeaderImg = (ImageView)findViewById(R.id.drink_header);
        drinkHeaderText = (TextView)findViewById(R.id.txt_menu_name);
        loadListDrink(Common.currentCategory.id);
        String u = Common.currentCategory.id;
        Log.d("TAG", "onCreate: "+u.toString());
        String imgUrl= getIntent().getStringExtra("menu_drink_img");
        Log.d("TAG9", "onCreate: ImageUrl"  + imgUrl);
        Picasso.with(DrinkActivity.this).load(imgUrl).into(drinkHeaderImg);
        String menuDrinkName = getIntent().getStringExtra("menu_drink_name");
        drinkHeaderText.setText(menuDrinkName);



    }

    private void loadListDrink(final String id) {
        compositeDisposable.add(mService.getDrink(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( new Consumer<List<Drink>>() {
                  @Override
                  public void accept(List<Drink> drinks) throws Exception {



                      displayDrinkList(drinks);
                  }


                  }));


        }

    private void displayDrinkList(List<Drink> drinks) {
        DrinkAdapter adapter=new DrinkAdapter(this,drinks);
        lst_drink.setAdapter(adapter);
    }
}
