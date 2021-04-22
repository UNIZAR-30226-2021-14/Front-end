package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;

import androidx.appcompat.app.AppCompatActivity;

public class PopupContacto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.popup_contacto);
        super.onCreate(savedInstanceState);
        android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(PopupContacto.this);
        alerta.setMessage(Html.fromHtml("<font color='#FFFFFF'>FreshTech@gmail.com</font>"))
                .setCancelable(false)
                .setPositiveButton(Html.fromHtml("<font color='#008577'>CERRAR</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        AlertDialog titulo = alerta.create();
        titulo.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        titulo.setTitle(Html.fromHtml("<font color='#008577'>Contacto</font>"));
        titulo.show();
    }
}
