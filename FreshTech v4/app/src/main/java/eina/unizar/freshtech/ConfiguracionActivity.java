package eina.unizar.freshtech;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ConfiguracionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        //Botón actualizar correo
        Button botonActualizarCorreo = (Button) findViewById(R.id.BotonActualizarCorreo);
        //Botón actualizar contraseña
        Button botonActualizarContraseña = (Button) findViewById(R.id.BotonActualizarContraseña);
    }

    public void onClickActualizarCorreo(View view) {
        Toast.makeText(this, "Se ha actualizado el correo electrónico", Toast.LENGTH_SHORT).show();
    }

    public void onClickActualizarContraseña(View view) {
        Toast.makeText(this, "Se ha actualizado la contraseña", Toast.LENGTH_SHORT).show();
    }
}