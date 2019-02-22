package com.uday.drinks.Database.ModelDB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Favourites")
public class Favourites {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "amount")
    public int amount;

    @ColumnInfo(name = "itemLink")
    public String link ;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "menuId")
    public String menuId;


  /*  @ColumnInfo(name = "soda")
    public int soda;

    @ColumnInfo(name = "ice")
    public int ice;

    @ColumnInfo(name = "toopingExtras")
    public String toppingExtras;

*/

}
