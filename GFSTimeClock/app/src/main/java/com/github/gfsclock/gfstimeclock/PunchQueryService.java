package com.github.gfsclock.gfstimeclock;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PunchQueryService {
    @POST("/sumtotalWebclock/api/service/findPunchesForEmployeesByIDs")
    Call<List<PunchModel>> getPunchesByID(
            @Body PunchList punchesByID
    );
}
