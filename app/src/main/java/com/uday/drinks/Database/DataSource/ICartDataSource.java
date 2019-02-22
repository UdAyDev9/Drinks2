package com.uday.drinks.Database.DataSource;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.uday.drinks.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {


    Flowable<List<Cart>> getCartItems();


    Flowable<List<Cart>> getCartItemById(int cartItemId);


    int countCartItems();


    void emptyCart();

    void insertToCart(Cart...carts);



    void updateCart(Cart...carts);


    void deleteCartItem(Cart cart);
}


