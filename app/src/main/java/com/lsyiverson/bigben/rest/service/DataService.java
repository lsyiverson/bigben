package com.lsyiverson.bigben.rest.service;

import android.support.v4.media.session.MediaSessionCompatApi14;

import com.lsyiverson.bigben.model.BeaconInfo;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface DataService {
    @GET("/{uuid}")
    void getBeaconInfo(@Path("uuid") String uuid, Callback<BeaconInfo> callback);
}
