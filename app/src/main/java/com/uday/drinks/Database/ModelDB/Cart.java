package com.uday.drinks.Database.ModelDB;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "Cart")
public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "amount")
    public int amount;

    @ColumnInfo(name = "itemLink")
    public String link ;

    @ColumnInfo(name = "price")
    public double price;

    @ColumnInfo(name = "soda")
    public int soda;

    @ColumnInfo(name = "ice")
    public int ice;

    @ColumnInfo(name = "toopingExtras")
    public String toppingExtras;


    public Cart() {
    }
}
