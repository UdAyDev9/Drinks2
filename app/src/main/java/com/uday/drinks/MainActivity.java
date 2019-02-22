package com.uday.drinks;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.uday.drinks.Model.CheckUserResponse;
import com.uday.drinks.Model.User;
import com.uday.drinks.Retrofit.IDrinkShopAPI;
import com.uday.drinks.Utils.Common;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Permission;

import dmax.dialog.SpotsDialog;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static  final int REQUEST_CODE=1000;
    private static final int REQUEST_CODE_READ_STORAGE = 10000 ;
    Button btn_continue;
    IDrinkShopAPI mService;
    String mPhone = "+917 ";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){

            case REQUEST_CODE_READ_STORAGE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(MainActivity.this,"Read Storage Permission Granted",Toast.LENGTH_LONG).show();
                }else {

                    Toast.makeText(MainActivity.this,"Read Storage Permission Denied",Toast.LENGTH_LONG).show();

                }

            }
            break;
            default:break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_READ_STORAGE);
        }


        mService= Common.getAPI();

        printKeyHash();
        btn_continue=(Button)findViewById(R.id.btn_contnue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
                //  showRegisterDialog("+917569123485");
            }
        });
        if(AccountKit.getCurrentAccessToken()!=null){
            final AlertDialog alertDialog=new SpotsDialog(MainActivity.this);
            alertDialog.show();
            alertDialog.setMessage("Please wait...");
            // Auto Login
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    mService.checkUserExists(account.getPhoneNumber().toString())


                            .enqueue(new Callback<CheckUserResponse>() {
                                @Override
                                public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                    CheckUserResponse userResponse=response.body();
                                    if(userResponse.isExists()){
                                        mService.getUserInformation(account.getPhoneNumber().toString())
                                                .enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        alertDialog.dismiss();
                                                        Common.currentuser=response.body();
//                                                showRegisterDialog(account.getPhoneNumber().toString());
                                                        startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {
                                                        alertDialog.dismiss();
                                                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        // if user already exists, just start new Activity


                                    }
                                    else {
                                        //else need rregister
                                        alertDialog.dismiss();
                                        showRegisterDialog(account.getPhoneNumber().toString());
                                        mPhone = account.getPhoneNumber().toString();
                                        Log.i("phone", "onResponse: "+mPhone);
                                    }
                                }

                                @Override
                                public void onFailure(Call<CheckUserResponse> call, Throwable t) {
                                    alertDialog.dismiss();
                                    Toast.makeText(MainActivity.this,"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
                                }
                            });

                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.d("ERROR",accountKitError.getErrorType().getMessage());

                }
            });

        }
    }

    private void startLoginPage(LoginType loginType) {
        Intent intent=new Intent(MainActivity.this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder
                builder=new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE){
            AccountKitLoginResult result=   data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if(result.getError()!=null){
                Toast.makeText(this, ""+result.getError().getErrorType().getMessage(), Toast.LENGTH_SHORT).show();
            }
            else if(result.wasCancelled()){
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
            else {
                if (result.getAccessToken()!=null){
                    final AlertDialog alertDialog=new SpotsDialog(MainActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Please waitings...");
                    //Get user phone and exists in the server
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            mService.checkUserExists(account.getPhoneNumber().toString())


                                    .enqueue(new Callback<CheckUserResponse>() {
                                        @Override
                                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                            CheckUserResponse userResponse=response.body();
                                            if(userResponse.isExists()){
                                                mService.getUserInformation(account.getPhoneNumber().toString())
                                                        .enqueue(new Callback<User>() {
                                                            @Override
                                                            public void onResponse(Call<User> call, Response<User> response) {
                                                                alertDialog.dismiss();
                                                  Common.currentuser=response.body();

//                                                showRegisterDialog(account.getPhoneNumber().toString());
                                                                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                                                finish();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<User> call, Throwable t) {
                                                                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                // if user already exists, just start new Activity


                                            }
                                            else {
                                                //else need rregister
                                                alertDialog.dismiss();
                                                showRegisterDialog(account.getPhoneNumber().toString());
                                                mPhone = account.getPhoneNumber().toString();
                                                Log.i("phone", "onResponse: "+mPhone);
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                                        }
                                    });

                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("ERROR",accountKitError.getErrorType().getMessage());

                        }
                    });
                }
            }
        }
    }

    private void showRegisterDialog(String toString) {
        Log.d("verify", "showRegisterDialog: click");
        final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("REGISTER");
        builder.setCancelable(false);


        LayoutInflater inflater=this.getLayoutInflater();
        View register_layout=inflater.inflate(R.layout.register_layout,null);
        final MaterialEditText edt_name=(MaterialEditText)register_layout.findViewById(R.id.edt_name);
        final MaterialEditText edt_address=(MaterialEditText)register_layout.findViewById(R.id.edt_address);
        final MaterialEditText edt_birthdate=(MaterialEditText)register_layout.findViewById(R.id.edt_birthdate);
        Button btn_register=(Button)register_layout.findViewById(R.id.btn_continue);

        edt_birthdate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));
        builder.setView(register_layout);
        final AlertDialog dialog=builder.create();

        final AlertDialog finalDialog = dialog;
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close dialog
                builder.create().dismiss();
//                finalDialog.dismiss();

                if (TextUtils.isEmpty(edt_address.getText().toString())) {
                    Toast.makeText(MainActivity.this, "please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edt_birthdate.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your birthdate", Toast.LENGTH_SHORT).show();
                    edt_birthdate.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(edt_name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                final AlertDialog waitingDialog = new SpotsDialog(MainActivity.this);
                waitingDialog.show();
//                waitingDialog.setMessage("Please Waiting...");


                mService.registerNewUser(mPhone,
                        edt_name.getText().toString(),
                        edt_address.getText().toString(),
                        edt_birthdate.getText().toString())

                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
//                                        waitingDialog.dismiss();
                                User user=response.body();
                                if(TextUtils.isEmpty(user.getError_msg())){
                                    Toast.makeText(MainActivity.this, "User register Successfully", Toast.LENGTH_SHORT).show();
                                    //start new Activity

                                    Common.currentuser =response.body();
                                    waitingDialog.dismiss();
                                    dialog.dismiss();
//                                          finish();
                                    startActivity(new Intent(MainActivity.this,HomeActivity.class));
                                    finish();
                                    Common.currentuser=response.body();
                                }

                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                waitingDialog.dismiss();

                            }
                        });
            }


        });
        dialog.show();

    }

    private void printKeyHash() {
        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.uday.drinks", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures){
                MessageDigest md=MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    boolean isBackButtonPressed = false;

    @Override
    public void onBackPressed() {
        if (isBackButtonPressed){

            super.onBackPressed();
            return;
        }

        this.isBackButtonPressed  = true;
        Toast.makeText(MainActivity.this,"Press bak again to exit!!",Toast.LENGTH_LONG).show();

    }
}
