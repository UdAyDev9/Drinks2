package com.uday.drinks.Database.DataSource;

import com.uday.drinks.Database.ModelDB.Favourites;

import java.util.List;

import io.reactivex.Flowable;

public class FavRepository implements IFavDataSource {

    private IFavDataSource iFavDataSource;
    private static FavRepository instacne;

    public FavRepository(IFavDataSource iFavDataSource) {
        this.iFavDataSource = iFavDataSource;
    }

    public static FavRepository getInstance(IFavDataSource iFavDataSource){

        if (instacne == null)

            instacne = new FavRepository(iFavDataSource);
        return instacne;
    }


    @Override
    public Flowable<List<Favourites>> getFavItems() {
        return iFavDataSource.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return iFavDataSource.isFavorite(itemId);
    }


    @Override
    public void insertToFavourites(Favourites... favourites) {

        iFavDataSource.insertToFavourites(favourites);
    }

    @Override
    public void deleteFavouritesItem(Favourites favourites) {

        iFavDataSource.deleteFavouritesItem(favourites);
    }
}
