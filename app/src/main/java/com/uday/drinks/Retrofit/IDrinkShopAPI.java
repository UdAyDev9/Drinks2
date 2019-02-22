package com.uday.drinks.Retrofit;

import com.uday.drinks.Model.Banner;
import com.uday.drinks.Model.Category;
import com.uday.drinks.Model.CheckUserResponse;
import com.uday.drinks.Model.Drink;
import com.uday.drinks.Model.User;


import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IDrinkShopAPI {
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<CheckUserResponse> checkUserExists(@Field("phone")String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone")final String phone,
                               @Field("name")String name,
                               @Field("address")String address,
                               @Field("birthdate")String birthdate);
    @FormUrlEncoded
    @POST("getDrinks.php")
   Observable<List<Drink>>getDrink(@Field("menu_id") String menuID);

    @FormUrlEncoded
    @POST("getUser.php")
    Call<User> getUserInformation(@Field("phone")final String phone);

    //declare end points to get url
    @GET("getBanners.php")
    Observable<List<Banner>> getBanners();

    @GET("getMenu.php")
    Observable<List<Category>> getMenu();

    @Multipart
    @POST("upload_photo.php")
    Call<String> uploadPhoto(@Part MultipartBody.Part phone, @Part MultipartBody.Part file);






}
