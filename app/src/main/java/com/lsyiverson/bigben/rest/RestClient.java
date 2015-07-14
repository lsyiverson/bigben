package com.lsyiverson.bigben.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lsyiverson.bigben.BigBenApplication;
import com.lsyiverson.bigben.rest.service.DataService;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RestClient {
    private DataService beaconDataService;

    public RestClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory())
                .create();
        createBeaconDataService(gson);
    }

    private void createBeaconDataService(Gson gson) {
        RestAdapter beaconDataRestAdapter = new RestAdapter.Builder()
                .setEndpoint(BigBenApplication.getInstance().getUrl())
                .setConverter(new GsonConverter(gson))
                .build();

        beaconDataService = beaconDataRestAdapter.create(DataService.class);
    }
}
