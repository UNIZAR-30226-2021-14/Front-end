package eina.unizar.freshtech.io;

import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("users")
    Call<Registrar> registrarUsuario(
            @Field("nombre") String nombre,
            @Field("password") String password
    );


}
