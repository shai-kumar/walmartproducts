package walmart.com.walmartproducts;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WalmartApi {

    @GET("walmartproducts/{apiKey}/{pageNumber}/{pageSize}")
    Call<WalmartResponse> getProducts(@Path("apiKey") String apiKey,
            @Path("pageNumber") int pageNumber,
            @Path("pageSize") int pageSize);
}
