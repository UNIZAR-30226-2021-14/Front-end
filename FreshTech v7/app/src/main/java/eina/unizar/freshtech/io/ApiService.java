package eina.unizar.freshtech.io;

import java.util.ArrayList;

import eina.unizar.freshtech.model.Categoria;
import eina.unizar.freshtech.model.Elemento;
import eina.unizar.freshtech.model.ElementoDetallado;
import eina.unizar.freshtech.model.Login;
import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
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

    @FormUrlEncoded
    @POST("passwd")
    Call<Registrar> anyadirContrasenya(
            @Header("Authorization") String Authorization,
            @Field("concreteuser") String nombreUsuario,
            @Field("concretepasswd") String contrasenya,
            @Field("dominio") String URL,
            @Field("fechacreacion") String fechaCreacion,
            @Field("fechacaducidad") String fechaCaducidad,
            @Field("nombre") String nombreContrasenya,
            @Field("categoria") String categoriaContrasenya
    );

    @GET("passwdUser")
    Call<ArrayList<Elemento>> obtenerElementos(
            @Header("Authorization") String Authorization,
            @Query("ordenarPor") String ordenarPor,
            @Query("ordenarDe") String ordenarDe
    );

    @GET("detailspasswd")
    Call<ElementoDetallado> obtenerElemento(
            @Header("Authorization") String Authorization,
            @Query("nombre") String nombre
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "deletepasswd", hasBody = true)
    Call<Registrar> eliminarContrasenya(
            @Header("Authorization") String Authorization,
            @Field("nombre") String nombreContrasenya
    );

    @FormUrlEncoded
    @POST("editpasswd")
    Call<Registrar> editarContrasenya(
            @Header("Authorization") String Authorization,
            @Field("nombrePassword") String nombreViejo,
            @Field("concreteuser") String nombreUsuario,
            @Field("concretepasswd") String contrasenya,
            @Field("dominio") String URL,
            @Field("categoria") String categoriaContrasenya,
            @Field("fechacreacion") String fechaCreacion,
            @Field("fechacaducidad") String fechaCaducidad,
            @Field("nombre") String nombreContrasenya
    );

    @FormUrlEncoded
    @POST("addcat")
    Call<Registrar> anyadirCategoria(
            @Header("Authorization") String Authorization,
            @Field("nombrecategoria") String nombreCategoria
    );

    @GET("getcat")
    Call<ArrayList<Categoria>> obtenerCategorias(
            @Header("Authorization") String Authorization
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "deletecat", hasBody = true)
    Call<Registrar> eliminarCategoria(
            @Header("Authorization") String Authorization,
            @Field("nombrecategoria") String nombreCategoria
    );
}
