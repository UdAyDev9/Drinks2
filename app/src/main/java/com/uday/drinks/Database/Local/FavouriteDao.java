package com.uday.drinks.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.uday.drinks.Database.ModelDB.Favourites;


import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface FavouriteDao {

    @Query("SELECT * FROM Favourites")
    Flowable<List<Favourites>> getFavItems();

    @Query("SELECT EXISTS (SELECT 1 FROM Favourites WHERE id=:id)")
    int isFavorite (int id);

    @Insert
    void insertToFav(Favourites... favourites);

    @Delete
    void deleteFavItem(Favourites favouries);

 /*   @Query("SELECT * FROM Favourites WHERE id =:favItemId")
    Flowable<List<Favourites>> getFavItemById(int favItemId);

    @Query("SELECT COUNT(*) FROM Favourites")
    int countFavItems();

    @Query("DELETE  FROM Favourites")
    void emptyFav();


    @Update
    void updateFav(Favourites...favouries);

*/

}
