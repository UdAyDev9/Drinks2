package com.uday.drinks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.facebook.accountkit.AccountKit;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;
import com.uday.drinks.Database.DataSource.CartRepository;
import com.uday.drinks.Database.DataSource.FavRepository;
import com.uday.drinks.Database.Local.CartDataSource;
import com.uday.drinks.Database.Local.FavDataSource;
import com.uday.drinks.Database.Local.RoomThingsDatabase;
import com.uday.drinks.Database.ModelDB.Favourites;
import com.uday.drinks.Model.Banner;
import com.uday.drinks.Model.Category;
import com.uday.drinks.Model.Drink;
import com.uday.drinks.Retrofit.IDrinkShopAPI;
import com.uday.drinks.Utils.Common;
import com.uday.drinks.Utils.ProgressRequestBody;
import com.uday.drinks.Utils.UploadCallback;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UploadCallback {
    private static final int PICK_FILE_CODE = 200 ;
    TextView txt_name,txt_phone;
    SliderLayout sliderLayout;
    IDrinkShopAPI mService;
    RecyclerView list_menu_recyclerView;
    NotificationBadge badge;
    ImageView cartIcon;
    CircleImageView navImgProfile;
    //RxJAva
    //call back methods
    CompositeDisposable compositeDisposable=new CompositeDisposable();

    Uri selectedFileUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){

            if (requestCode == PICK_FILE_CODE){


                if (data!=null){

                    selectedFileUri = data.getData();

                    if (selectedFileUri!=null && !selectedFileUri.getPath().isEmpty()){

                        navImgProfile.setImageURI(selectedFileUri);
                        uploadFilePhoto();

                    }else {

                        Toast.makeText(HomeActivity.this,"Cannot Upload File to Server",Toast.LENGTH_LONG).show();
                    }

                }
            }
        }
    }

    private void uploadFilePhoto() {

        try {
            if (selectedFileUri!=null){


                File file = FileUtils.getFile(this,selectedFileUri);
                String filename = new StringBuilder(Common.currentuser.getPhone()).append(FileUtils.getExtension(file.toString())).toString();
                Log.d("File", "uploadFilePhoto File Name :"+filename);

                ProgressRequestBody requestBody = new ProgressRequestBody(file,this);
                final MultipartBody.Part photoBody = MultipartBody.Part.createFormData("upload_file",filename,requestBody);
                final MultipartBody.Part phoneBody = MultipartBody.Part.createFormData("phone",Common.currentuser.getPhone());

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        mService.uploadPhoto(phoneBody,photoBody).enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {

                                Toast.makeText(HomeActivity.this,response.body(),Toast.LENGTH_LONG).show();
                                Log.d("resp", "onResponse: "+response.body());
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Log.e("respError", "onFailure: "+t.getMessage());

                            }
                        });

                    }
                }).start();

            }
        }catch (Exception e){

            Log.d("TAG12", "uploadFilePhoto: ");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sliderLayout=(SliderLayout)findViewById(R.id.slider);
        list_menu_recyclerView = (RecyclerView)findViewById(R.id.list_menu);
        list_menu_recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        list_menu_recyclerView.setHasFixedSize(true);
        mService=Common.getAPI();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView=navigationView.getHeaderView(0);
        txt_name=(TextView)headerView.findViewById(R.id.textview_name);
        txt_phone=(TextView)headerView.findViewById(R.id.textView_phone);
        navImgProfile = (CircleImageView) headerView.findViewById(R.id.image_view_nav);


        //set Info
       txt_name.setText(Common.currentuser.getName());
       txt_phone.setText(Common.currentuser.getPhone());

        if (!TextUtils.isEmpty(Common.currentuser.getProfile()))
        Picasso.with(this).load(new StringBuilder(Common.BASE_URL).append("user_profile/").append(Common.currentuser.getProfile()).toString()).placeholder(R.drawable.img_loading_placeholder).into(navImgProfile);


        navImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(),"Select a File"),PICK_FILE_CODE);
            }
        });



       getBannerImage();

       getMenu();


        //save new Topping list
        getToppingList();

        //Initialing DB

        initDB();
    }

    private void initDB() {

        Common.roomThingsDatabase = RoomThingsDatabase.getInstance(this);

        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.roomThingsDatabase.cartDao()));

        Common.favRepository = FavRepository.getInstance(FavDataSource.getInstance(Common.roomThingsDatabase.favouriteDao()));

    }


    private void getToppingList() {
        compositeDisposable.add(mService.getDrink(Common.ToppingMenuId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Drink>>() {
                    @Override
                    public void accept(List<Drink> drinks) throws Exception {
                        Common.toppingList=drinks;
                    }
                }));

    }


    private void getBannerImage() {
        compositeDisposable.add(mService.getBanners()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Banner>>() {
            @Override
            public void accept(List<Banner> banners) throws Exception {
                displayImage(banners);
            }
        }));

    }
    private void getMenu() {

        compositeDisposable.add(mService.getMenu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Category>>() {
                    @Override
                    public void accept(List<Category> categories) throws Exception {

                        displayMenu(categories);
                    }
                }));

    }

    private void displayMenu(List<Category> categories) {

        CategoryAdapter categoryAdapter = new CategoryAdapter(categories,this);
        list_menu_recyclerView.setAdapter(categoryAdapter);
    }



    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImage(List<Banner> banners) {
        HashMap<String,String> bannerMap=new HashMap<>();
    for(Banner item:banners)
        bannerMap.put(item.getName(),item.getLink());
    for(String name:bannerMap.keySet()){
        TextSliderView textSliderView=new TextSliderView(this);
        textSliderView.description(name.toUpperCase())
                .image(bannerMap.get(name))
                .setScaleType(BaseSliderView.ScaleType.Fit);
        sliderLayout.addSlider(textSliderView);

    }


    }
    boolean isBackButtonPressed = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (isBackButtonPressed){

                super.onBackPressed();
                return;
            }

            this.isBackButtonPressed  = true;
            Toast.makeText(HomeActivity.this,"Press bak again to exit!!",Toast.LENGTH_LONG).show();



        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();

        badge = (NotificationBadge)view.findViewById(R.id.badge);
        cartIcon = (ImageView)view.findViewById(R.id.cart_icon);

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(HomeActivity.this,CartActivity.class));
            }
        });

        updateCartCount();


        return true;
    }

    private void updateCartCount() {

        if (badge == null) return;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (Common.cartRepository.countCartItems()==0){
                    badge.setVisibility(View.INVISIBLE);
                }else {

                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            // Handle the camera action

            startActivity(new Intent(HomeActivity.this,CartActivity.class));

        } else if (id == R.id.nav_favourites) {

            startActivity(new Intent(HomeActivity.this, FavouritesActivity.class));

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_sign_out) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this).setTitle("Exit Apllication").setMessage("Do you want to exit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    AccountKit.logOut();

                    Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            builder.show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateCartCount();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
        isBackButtonPressed = false;
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }





}
