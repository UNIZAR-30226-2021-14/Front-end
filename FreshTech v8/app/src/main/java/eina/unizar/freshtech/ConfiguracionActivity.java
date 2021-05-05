package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.gson.Gson;

import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Login;
import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfiguracionActivity extends AppCompatActivity implements Callback<Registrar> {

    private static final int ACTIVITY_LOGIN=0;

    private EditText nuevaContraseña;
    private EditText confirmarNuevaContraseña;
    private Button botonActualizarContraseña;
    private Button botonEliminarCuenta;
    private AwesomeValidation awesomeValidation;
    private ProgressBar progressBar;
    private boolean isAtLeast8, hasMayus, hasMinus, hasNums, hasEsp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        nuevaContraseña = (EditText) findViewById(R.id.NuevaContraseña);
        confirmarNuevaContraseña = (EditText) findViewById(R.id.ConfirmarNuevaContraseña);
        //Botón actualizar contraseña
        botonActualizarContraseña = (Button) findViewById(R.id.BotonActualizarContraseña);
        //Botón eliminar cuenta
        botonEliminarCuenta = (Button) findViewById(R.id.botonEliminarCuenta);
        //Seguridad contraseña
        progressBar = (ProgressBar) findViewById(R.id.progressBarConfiguracion);
        nuevaContraseña.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void afterTextChanged(Editable s) {
                int porcentajeSegura = SeguridadContraseña.calcularSeguridad(s.toString(), isAtLeast8, hasMayus, hasMinus, hasNums, hasEsp);
                progressBar.setProgress(porcentajeSegura);
                progressBar.setVisibility(View.VISIBLE);
                LayerDrawable progressBarDrawable = (LayerDrawable) progressBar.getProgressDrawable();
                Drawable progressDrawable = progressBarDrawable.getDrawable(1);
                progressDrawable.setColorFilter(ContextCompat.getColor(ConfiguracionActivity.this, SeguridadContraseña.colorProgressBar(porcentajeSegura)), PorterDuff.Mode.SRC_IN);
            }
        });
    }

    public void onClickActualizarContraseña(View view) {
        String password = nuevaContraseña.getText().toString();
        String passwordConfirmada = confirmarNuevaContraseña.getText().toString();
        awesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this, R.id.huecoNuevaContraseña,
                "(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#$%*])\\S{8,}$", R.string.invalid_password);
        awesomeValidation.addValidation(this, R.id.huecoConfirmarNuevaContraseña,
                R.id.huecoNuevaContraseña, R.string.invalid_confirm_password);
        if(awesomeValidation.validate()) {
            String token = "Bearer " + Preferencias.cargarToken(this);
            Call<Registrar> call = ApiAdapter.getApiService().actualizarContraseña(token, password);
            call.enqueue(this);
        }
    }

    public void onClickEliminarCuenta(View view) {
        android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(ConfiguracionActivity.this);
        alerta.setMessage(Html.fromHtml("<font color='#FFFFFF'>¿Desea eliminar su cuenta y todos los datos almacenados?</font>"))
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String token = "Bearer " + Preferencias.cargarToken(ConfiguracionActivity.this);
                        Call<Registrar> callEliminar = ApiAdapter.getApiService().eliminarCuenta(token);
                        callEliminar.enqueue(ConfiguracionActivity.this);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        titulo.setTitle(Html.fromHtml("<font color='#008577'>AVISO</font>"));
        titulo.show();
    }

    @Override
    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
        if(response.isSuccessful()) {
            Registrar registrar = response.body();
            if(registrar.getMessage().equals("Password changed correctly")) {
                Log.d("onResponse changepw", "Mensaje => " + registrar.getMessage());
                Toast.makeText(this, "Se ha actualizado la contraseña", Toast.LENGTH_SHORT).show();
            }
            else if(registrar.getMessage().equals("Usuario deleteado correctamente")) {
                Log.d("onResponse removeCuenta", "Mensaje => " + registrar.getMessage());
                Toast.makeText(this, "Se ha eliminado la cuenta", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, LoginActivity.class);
                startActivityForResult(i, ACTIVITY_LOGIN);
                finish();
            }
            else {
                Log.d("onResponse changepw", "Mensaje => " + registrar.getMessage());
                Toast.makeText(this, "No se ha podido actualizar la contraseña", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Registrar registrar = new Gson().fromJson(response.errorBody().charStream(),Registrar.class);
            if(registrar.getMessage().equals("Usuario no existe!!")) {
                Log.d("onResponse removeCuenta", "Mensaje => " + registrar.getMessage());
                Toast.makeText(this, "No se ha podido eliminar la cuenta", Toast.LENGTH_SHORT).show();
            }
            else {
                Log.d("onResponse changepw", "Mensaje => not successful");
                Toast.makeText(this, "No se ha podido actualizar la contraseña", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<Registrar> call, Throwable t) {
        Log.d("onFailure configuracion", "Mensaje => No ha funcionado");
        Toast.makeText(this, "Error onFailure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}