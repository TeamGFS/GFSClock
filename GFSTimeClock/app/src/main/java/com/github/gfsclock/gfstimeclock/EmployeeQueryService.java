package com.github.gfsclock.gfstimeclock;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EmployeeQueryService {
    @GET("/emps/rest/employee/{id}")
    Call<EmployeeData> getData(
            @Path("id") String id
    );
}
