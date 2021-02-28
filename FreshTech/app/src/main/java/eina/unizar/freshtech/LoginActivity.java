package eina.unizar.freshtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    private static final int ACTIVITY_INICIARSESION=0;
    private static final int ACTIVITY_REGISTRARSE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClickIniciarSesion(View view) {
        Intent i = new Intent(this, PrincipalActivity.class);
        startActivityForResult(i, ACTIVITY_INICIARSESION);
        finish();
    }

    public void onClickRegistrarse(View view) {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivityForResult(i, ACTIVITY_REGISTRARSE);
    }
}