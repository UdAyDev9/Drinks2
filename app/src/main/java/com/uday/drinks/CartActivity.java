package com.uday.drinks;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
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

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RecyclerView recycler_cart;
    CompositeDisposable compositeDisposable;
    Button btn_place_order;
    List<Cart> cartList;
    CartAdapter cartAdapter;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        compositeDisposable = new CompositeDisposable();
        recycler_cart = (RecyclerView) findViewById(R.id.recycler_cart);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        recycler_cart.setHasFixedSize(true);
        rootLayout = (RelativeLayout)findViewById(R.id.root_layout_cart);
        ItemTouchHelper.SimpleCallback simpleCallback=new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

        btn_place_order = (Button) findViewById(R.id.btn_place_order);
        loadCartItems();
    }

    private void loadCartItems() {
        compositeDisposable.add(Common.cartRepository.getCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {

                        displaycartItem(carts);

                    }


                }));


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        loadCartItems();
    }

    private void displaycartItem(List<Cart> carts) {
        cartList = carts;
         cartAdapter = new CartAdapter(CartActivity.this, carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartAdapter.CartViewHolder){
            String name=cartList.get(viewHolder.getAdapterPosition()).name;
            final Cart deletedItem=cartList.get(viewHolder.getAdapterPosition());
            final int deletedIndex=viewHolder.getAdapterPosition();

            //delete item from Adapter
            cartAdapter.removeItem(deletedIndex);

            //delete item from the database
            Common.cartRepository.deleteCartItem(deletedItem);
            Snackbar snackbar=Snackbar.make(rootLayout,new StringBuilder(name).append("Removed from Cart List").toString(),
                    Snackbar.LENGTH_LONG) ;
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deletedItem,deletedIndex);
                    Common.cartRepository.insertToCart(deletedItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();

        }
    }
}
