package eina.unizar.freshtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements Callback<Registrar> {

    private static final int ACTIVITY_LOGIN=0;

    private EditText correo;
    private EditText contraseña;
    private EditText confirmarContraseña;
    private AwesomeValidation awesomeValidation;
    private AwesomeValidation awesomeValidationContraseñas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        correo = (EditText) findViewById(R.id.correoRegister);
        contraseña = (EditText) findViewById(R.id.contraseñaRegister);
        confirmarContraseña = (EditText) findViewById(R.id.confirmarContraseñaRegister);
    }

    public void onClickCrearCuenta(View view) {
        //Validar campos
        String nombre = correo.getText().toString();
        String password = contraseña.getText().toString();
        String passwordConfirmada = confirmarContraseña.getText().toString();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        awesomeValidation.addValidation(this, R.id.correoRegister,
                Patterns.EMAIL_ADDRESS, R.string.invalid_email);

        awesomeValidationContraseñas = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        awesomeValidationContraseñas.addValidation(this, R.id.huecoContraseñaRegister,
                ".{4,}", R.string.invalid_password);
        awesomeValidationContraseñas.addValidation(this, R.id.huecoConfirmarContraseñaRegister,
                R.id.huecoContraseñaRegister, R.string.invalid_confirm_password);
        boolean correoCorrecto = awesomeValidation.validate();
        boolean contraseñasCorrectas = awesomeValidationContraseñas.validate();
        //Conectarse a API
        if (correoCorrecto && contraseñasCorrectas) {
            Call<Registrar> call = ApiAdapter.getApiService().registrarUsuario(nombre, password);
            call.enqueue(this);
        }
        else {
            Toast.makeText(this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
        if (response.isSuccessful()) {
            Registrar registrar = response.body();
            Log.d("onResponse registro", "Mensaje => " + registrar.getMessage());
            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, ACTIVITY_LOGIN);
            finish();
        }
    }

    @Override
    public void onFailure(Call<Registrar> call, Throwable t) {
        Toast.makeText(this, "No se ha podido registrar el usuario", Toast.LENGTH_SHORT).show();
    }
}