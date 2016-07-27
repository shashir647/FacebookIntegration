package com.myowncompany.fb_loginapplication;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private TextView textDetail1;
    //declare call back manager
    private CallbackManager callbackManager;
    //after initializing callback manager
    private AccessTokenTracker tokenTracker;
    private ProfileTracker profileTracker;
    private FacebookCallback<LoginResult> callback= new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken=loginResult.getAccessToken();
            Profile profile=Profile.getCurrentProfile();
            displayWelcomeMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        //initialize call back manager
        callbackManager=CallbackManager.Factory.create();
        tokenTracker=new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };
        profileTracker=new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            displayWelcomeMessage(currentProfile);
            }
        };
        tokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
private void displayWelcomeMessage(Profile profile){
    if (profile!=null){
        textDetail1.setText("Welcome to World of Facebook"+ profile.getName());
    }
}
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton= (LoginButton) view.findViewById(R.id.login_button);
        //set permission for login button
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager,callback);
        textDetail1= (TextView) view.findViewById(R.id.textDetail);

    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile=Profile.getCurrentProfile();
        displayWelcomeMessage(profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        tokenTracker.stopTracking();
        profileTracker.stopTracking();
    }
    //write onActivity result inside onCreate

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
