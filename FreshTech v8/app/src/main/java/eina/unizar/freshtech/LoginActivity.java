package eina.unizar.freshtech;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Login;
import eina.unizar.freshtech.model.Registrar;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Callback<Login> {

    private static final int ACTIVITY_INICIARSESION=0;
    private static final int ACTIVITY_REGISTRARSE=1;

    private EditText correo;
    private EditText contraseña;
    private TextInputLayout huecoCorreo;
    private TextInputLayout huecoContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        huecoCorreo = (TextInputLayout) findViewById(R.id.huecoCorreoLogin);
        huecoContraseña =(TextInputLayout) findViewById(R.id.huecoContraseñaLogin);
        correo = (EditText) findViewById(R.id.correoLogin);
        contraseña = (EditText) findViewById(R.id.contraseñaLogin);
        //correo.setText("prueba@gmail.com");
        //contraseña.setText("Prueba1999!");
    }

    public void onClickIniciarSesion(View view) {
        huecoCorreo.setErrorEnabled(false);
        huecoContraseña.setErrorEnabled(false);
        String nombre = correo.getText().toString();
        String password = contraseña.getText().toString();
        Call<Login> call = ApiAdapter.getApiService().IniciarSesion(nombre, password);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Login> call, Response<Login> response) {
        if (response.isSuccessful()) {
            Login login = response.body();
            Log.d("onResponse login", "Mensaje => " + login.getMessage());
            if(login.getMessage().equals("User OK")) {
                guardarPreferencias();
                Preferencias.guardarToken(this, correo.getText().toString(),login.getToken());
                //Intent i = new Intent(this, VerificacionActivity.class);
                Intent i = new Intent(this, PrincipalActivity.class);
                startActivityForResult(i, ACTIVITY_INICIARSESION);
                //finish();
            }
            else {
                Log.d("onResponse login", "Mensaje => " + login.getMessage());
                Toast.makeText(this, "No se ha podido iniciar sesión", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Login login = new Gson().fromJson(response.errorBody().charStream(),Login.class);
            if(login.getMessage().equals("User does not exist")) {
                huecoCorreo.setError("No existe ningún usuario con este correo");
            }
            else {
                huecoContraseña.setError("Contraseña incorrecta");
            }
            Log.d("onResponse login", "Mensaje => " + login.getMessage());
        }
    }

    @Override
    public void onFailure(Call<Login> call, Throwable t) {
        Log.d("onFailure login", "Mensaje => No ha funcionado");
        Toast.makeText(this, "Error onFailure", Toast.LENGTH_SHORT).show();
    }

    public void onClickRegistrarse(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivityForResult(i, ACTIVITY_REGISTRARSE);
    }

    @Override public void onBackPressed() {
        android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(LoginActivity.this);
        alerta.setMessage(Html.fromHtml("<font color='#FFFFFF'>¿Desea salir de la aplicación?</font>"))
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        moveTaskToBack(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        titulo.setTitle(Html.fromHtml("<font color='#008577'>AVISO</font>"));
        titulo.show();
    }

    private void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("ordenar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OrdenarPor","Fecha de creación");
        editor.putString("OrdenarDe", "Menor a mayor");
        editor.commit();
    }
}