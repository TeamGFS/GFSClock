package com.github.gfsclock.gfstimeclock;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PunchQueryService {
    @POST("/sumtotalWebclock/api/service/findPunchesForEmployeesByIDs")
    Call<PunchList> getPunchesByID(
            @Body PunchList punchesByID
    );
}
