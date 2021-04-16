package eina.unizar.freshtech.io;

import eina.unizar.freshtech.model.Login;
import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @FormUrlEncoded
    @POST("signin")
    Call<Registrar> registrarUsuario(
            @Field("nombre") String nombre,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login")
    Call<Login> IniciarSesion(
            @Field("nombre") String nombre,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("changepw")
    Call<Registrar> actualizarContraseña(
            @Header("Authorization") String Authorization,
            @Field("password") String password
    );

    @DELETE("removeAccount")
    Call<Registrar> eliminarCuenta(
            @Header("Authorization") String Authorization
    );

}
