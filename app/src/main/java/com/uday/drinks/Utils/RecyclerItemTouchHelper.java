package com.uday.drinks.Utils;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.uday.drinks.CartAdapter;
import com.uday.drinks.FavouritesAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs,RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener=listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener!=null)
            listener.onSwiped(viewHolder,direction, viewHolder.getAdapterPosition());

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof FavouritesAdapter.FavViewHolder) {
            View foregroundView = ((FavouritesAdapter.FavViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().clearView(foregroundView);
        } else if (viewHolder instanceof CartAdapter.CartViewHolder) {
            View foregroundView = ((CartAdapter.CartViewHolder) viewHolder).view_foreground;
            getDefaultUIUtil().clearView(foregroundView);
        }
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder!=null)
        {
            if(viewHolder instanceof FavouritesAdapter.FavViewHolder){
                View foregroundView=((FavouritesAdapter.FavViewHolder) viewHolder).view_foreground;
                getDefaultUIUtil().onSelected(foregroundView);
            }
            else if (viewHolder instanceof CartAdapter.CartViewHolder)
           {
                View foregroundView=((CartAdapter.CartViewHolder) viewHolder).view_foreground;
                getDefaultUIUtil().onSelected(foregroundView);


        }
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
       if(viewHolder instanceof FavouritesAdapter.FavViewHolder){
           View foregroundView=((FavouritesAdapter.FavViewHolder) viewHolder).view_foreground;
           getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
       }
       else if(viewHolder instanceof CartAdapter.CartViewHolder){
           View foregroundView=((CartAdapter.CartViewHolder) viewHolder).view_foreground;
           getDefaultUIUtil().onDraw(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
       }

    }

    @Override
    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
      if(viewHolder instanceof FavouritesAdapter.FavViewHolder){
          View foregroundView=((FavouritesAdapter.FavViewHolder) viewHolder).view_foreground;
          getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
      }
      else  if(viewHolder instanceof CartAdapter.CartViewHolder){
          View foregroundView=((CartAdapter.CartViewHolder) viewHolder).view_foreground;
          getDefaultUIUtil().onDrawOver(c,recyclerView,foregroundView,dX,dY,actionState,isCurrentlyActive);
      }
    }
}

