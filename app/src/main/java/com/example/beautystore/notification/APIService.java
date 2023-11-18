package com.example.beautystore.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAQbWEeFk:APA91bEYQk7epdZpu9NBFEbXG4eosTgLvlIaOt2GDg_gxpatFvbOG7uz_MrRfwOPPJW8Chs09vF2XwSZtlUJTuKkczV8Oa9mcbnk2droxVPPSzsUb2033Y6y3eldGI7_gPGGOi3Eoupt"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
