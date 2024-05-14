package com.example.experiencethefutureofsmartermobility;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.aws.ApiAuthProviders;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.example.experiencethefutureofsmartermobility.databinding.ActivityAdminBinding;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class AdminActivity extends AppCompatActivity {
    private final String TAG = AdminActivity.class.getSimpleName();
    private ActivityAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAmplify();


        binding.startBtn.setOnClickListener(v ->{
            startNavActivity();
        });

    }

    public void startNavActivity(){

        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    switch(cognitoAuthSession.getIdentityIdResult().getType()) {
                        case SUCCESS:
                            Log.i("AuthQuickStart", "IdentityId: " + cognitoAuthSession.getIdentityIdResult().getValue());
                            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                            //intent.putExtra("zoneId" , zoneId);
                            startActivity(intent);
                            finish();
                            break;
                        case FAILURE:
                            Log.e("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityIdResult().getError().toString());
                            Log.e(TAG, "is not signed");
                            binding.tvAdmin.setText("Login Fail, "+cognitoAuthSession.getIdentityIdResult().getError().toString());
                            break;
                    }
                },
                error -> Log.e("AuthQuickStart", error.toString())
        );
    }

    public void initAmplify(){

        try {
//            OkHttpClient httpClient = new OkHttpClient.Builder()
//                    .connectTimeout(30, TimeUnit.SECONDS) // 연결 Timeout 30초
//                    .readTimeout(30, TimeUnit.SECONDS) // 읽기 Timeout 30초
//                    .writeTimeout(30, TimeUnit.SECONDS) // 쓰기 Timeout 30초
//                    .build();
            AWSApiPlugin plugin = AWSApiPlugin.builder()
                    .configureClient("api2024Summit", okHttpClientBuilder -> {
                        okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
                    })
                    .build();
            Amplify.addPlugin(plugin);


            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

//            AWSMobileClient mobileClient = (AWSMobileClient) Amplify.Auth.getPlugin("awsCognitoAuthPlugin").getEscapeHatch();
//            AWSMobileClient



//            Amplify.Auth.signIn();
//            if(mobileClient.isSignedIn()){
////                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                startActivity(intent);
////                finish();
//                Log.d(TAG, "is signed");
//                binding.tvAdmin.setText("User name : " +mobileClient.getUsername() +" is SignedIn");
//                if("demouser1".equals(mobileClient.getUsername())){
//                    zoneId = "zone1";
//                }else if("demouser2".equals(mobileClient.getUsername())){
//                    zoneId = "zone2";
//                }else if("demouser3".equals(mobileClient.getUsername())){
//                    zoneId = "zone3";
//                }else{
//                    zoneId = "zone1";
//                }
//
//            }else{
//                Log.d(TAG, "is not signed");
//                binding.tvAdmin.setText("User is not signed");
//            }

            /*
            User 임시가입
             */
//            AuthSignUpOptions options = AuthSignUpOptions.builder()
//                    .userAttribute(AuthUserAttributeKey.email(), "sshyun@amazon.com")
////                    .userAttribute(AuthUserAttributeKey.preferredUsername(),"demouser1")
//                    .build();
//
//            Amplify.Auth.signUp("demouser1", "Password123!" , options,
//                    res -> Log.i("AuthSignupTest", "result : "+res.toString()),
//                    err -> Log.e("AuthSignupTest", err.toString()));

            /*
            User 컨펌코드
             */
//            Amplify.Auth.confirmSignUp("demouser1",
//                    "842565",
//                    res -> Log.i("AuthConfirmTest", "result : "+res.toString()),
//                    err -> Log.e("AuthConfirmTest", err.toString()));

            /*
            user 로그인
             */
            Amplify.Auth.signIn(
                    "demouser1",
                    "Password123!",
                    res -> Log.i("AuthSigninTest", "result : "+res.toString()),
                    err -> Log.e("AuthSigninTest", err.toString()));

            Log.i(TAG, "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e(TAG, "Could not initialize Amplify", error);
        }
    }

}