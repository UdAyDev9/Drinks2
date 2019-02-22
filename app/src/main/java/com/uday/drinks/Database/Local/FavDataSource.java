package com.uday.drinks.Database.Local;


import com.uday.drinks.Database.DataSource.IFavDataSource;
import com.uday.drinks.Database.ModelDB.Favourites;

import java.util.List;

import io.reactivex.Flowable;

public class FavDataSource implements IFavDataSource {

        private FavouriteDao favouriteDao;
        private static FavDataSource instance;

    public FavDataSource(FavouriteDao favouriteDao) {
        this.favouriteDao = favouriteDao;
    }

    public static FavDataSource getInstance(FavouriteDao favouriteDao){

        if (instance == null)

            instance = new FavDataSource(favouriteDao);

        return instance;
    }

    @Override
    public Flowable<List<Favourites>> getFavItems() {
        return favouriteDao.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return favouriteDao.isFavorite(itemId);
    }



    @Override
    public void insertToFavourites(Favourites... favourites) {

        favouriteDao.insertToFav(favourites);
    }
    @Override
    public void deleteFavouritesItem(Favourites favourites) {
        favouriteDao.deleteFavItem(favourites);
    }

}
