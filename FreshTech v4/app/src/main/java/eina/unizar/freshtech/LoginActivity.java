package eina.unizar.freshtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;

public class LoginActivity extends AppCompatActivity {

    private static final int ACTIVITY_INICIARSESION=0;
    private static final int ACTIVITY_REGISTRARSE=1;

    private EditText correo;
    private EditText contraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        correo = (EditText) findViewById(R.id.correoLogin);
        contraseña = (EditText) findViewById(R.id.contraseñaLogin);
        correo.setText("755787@unizar.es");
        contraseña.setText("12345");
    }

    public void onClickIniciarSesion(View view) {
        //Validar campos
        String nombre = correo.getText().toString();
        String password = contraseña.getText().toString();
        Intent i = new Intent(this, PrincipalActivity.class);
        startActivityForResult(i, ACTIVITY_INICIARSESION);
        finish();
    }

    public void onClickRegistrarse(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivityForResult(i, ACTIVITY_REGISTRARSE);
    }

    @Override public void onBackPressed() { moveTaskToBack(true); }
}