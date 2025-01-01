package com.personal.development.travelhub;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NominatimApiService {
    @GET("search")
    Call<List<NominatimResult>> search(
            @Query("q") String query,
            @Query("format") String format,
            @Query("limit") int limit
    );
}

