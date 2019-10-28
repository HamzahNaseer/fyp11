package com.example.tourismof.Fragments;

import com.example.tourismof.Notifications.MyResponse;
import com.example.tourismof.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "content-Type:application/json",
                    "Authorization:Key=AAAATCT5xp4:APA91bEyvu12mQJh3bPnA_wOibt_G0ZCHEI-e3APCAymtJJsimU54LZVBTAuoRQWxWGuQtq7OJpzzC-rCqouvgJSjjrGr5I9Gp-xh6xlOnBcsGm7JmoWo2fhJeRPXYvyIODmAtv1O_Il"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
