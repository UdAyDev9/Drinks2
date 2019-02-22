package com.uday.drinks.Database.DataSource;

import com.uday.drinks.Database.ModelDB.Favourites;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavDataSource {

    Flowable<List<Favourites>> getFavItems();

    int isFavorite (int itemId);


    void insertToFavourites(Favourites...favourites);


    void deleteFavouritesItem(Favourites favourites);
}
