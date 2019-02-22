package com.uday.drinks.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.uday.drinks.Database.ModelDB.Cart;
import com.uday.drinks.Database.ModelDB.Favourites;

@Database(entities = {Cart.class, Favourites.class},version = 1)
public abstract class RoomThingsDatabase extends RoomDatabase {

    public abstract CartDao cartDao();
    public abstract FavouriteDao favouriteDao();

    public static RoomThingsDatabase instance;

    public static RoomThingsDatabase getInstance(Context context){

        if (instance == null)

            instance = Room.databaseBuilder(context, RoomThingsDatabase.class,"DrinkDb")
                    .allowMainThreadQueries()
                    .build();

        return instance;
    }

}
