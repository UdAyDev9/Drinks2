package com.uday.drinks;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.uday.drinks.Database.ModelDB.Cart;
import com.uday.drinks.Database.ModelDB.Favourites;
import com.uday.drinks.Utils.Common;
import com.uday.drinks.Utils.RecyclerItemTouchHelper;
import com.uday.drinks.Utils.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavouritesActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerViewFav;
    CompositeDisposable compositeDisposable;
    FavouritesAdapter favouritesAdapter;
    List<Favourites> localFavorites = new ArrayList<>();
    RelativeLayout rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        compositeDisposable = new CompositeDisposable();
        recyclerViewFav = (RecyclerView) findViewById(R.id.recycler_fav);
        recyclerViewFav.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewFav.setHasFixedSize(true);
        rootLayout = (RelativeLayout)findViewById(R.id.root_layout);

        ItemTouchHelper.SimpleCallback simpleCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerViewFav);
        loadFavoritesItem();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    private void loadFavoritesItem() {
        compositeDisposable.add(Common.favRepository.getFavItems()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Favourites>>() {
                    @Override
                    public void accept(List<Favourites> favorites) throws Exception {

                        displayFavoriteItem(favorites);
                    }
                }));
    }

    private void displayFavoriteItem(List<Favourites>favorites) {
        localFavorites=favorites;
        favouritesAdapter=new FavouritesAdapter(this,favorites);
        recyclerViewFav.setAdapter(favouritesAdapter);



    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof FavouritesAdapter.FavViewHolder){
            String name=localFavorites.get(viewHolder.getAdapterPosition()).name;
            final Favourites deletedItem=localFavorites.get(viewHolder.getAdapterPosition());
            final int deletedIndex=viewHolder.getAdapterPosition();

            //delete item from Adapter
            favouritesAdapter.removeItem(deletedIndex);

            //delete item from the database
            Common.favRepository.deleteFavouritesItem(deletedItem);
            Snackbar snackbar=Snackbar.make(rootLayout,new StringBuilder(name).append("Removed from Favourites List").toString(),
                    Snackbar.LENGTH_LONG) ;
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    favouritesAdapter.restoreItem(deletedItem,deletedIndex);
                    Common.favRepository.insertToFavourites(deletedItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }

    }

}
