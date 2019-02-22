package com.uday.drinks.Utils;

import com.uday.drinks.Database.DataSource.CartRepository;
import com.uday.drinks.Database.DataSource.FavRepository;
import com.uday.drinks.Database.Local.RoomThingsDatabase;
import com.uday.drinks.Model.Category;
import com.uday.drinks.Model.Drink;
import com.uday.drinks.Model.User;
import com.uday.drinks.Retrofit.IDrinkShopAPI;
import com.uday.drinks.Retrofit.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static final String BASE_URL="http://192.168.0.109:9999/SAI/Drinks/";
    public  static User currentuser = null;
    public  static Category currentCategory = null;
    public static  final String ToppingMenuId="11";
    public static  double toppingprice=0.0;
    //Hold of Field

    public static  int sizeOfBottle =-1;

    public static  int soda =-1;

    public static  int ice=-1;

    public static List<Drink> toppingList=new ArrayList<>();

    public static List<String> toppingAdded=new ArrayList<>();

    //Database

    public static RoomThingsDatabase roomThingsDatabase;

    public static CartRepository cartRepository;

    public static FavRepository favRepository;

    public static IDrinkShopAPI getAPI()
    {
        return RetrofitClient.getClient(BASE_URL).create(IDrinkShopAPI.class);

    }




}
