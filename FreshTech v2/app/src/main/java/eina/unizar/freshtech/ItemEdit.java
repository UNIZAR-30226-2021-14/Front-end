package eina.unizar.freshtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

public class ItemEdit extends AppCompatActivity {

    private static final int ACTIVITY_PRINCIPAL=0;

    private Spinner SpinnerCategoria;
    private ImageButton BotonAñadirCategoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_edit);
        //Spinner Categorias
        SpinnerCategoria = (Spinner) findViewById(R.id.spinner2);
        String [] CategoriasEjemplo = {"", "Categoria 1", "Categoria 2", "Categoria 3"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.simple_spinner_item, CategoriasEjemplo);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        SpinnerCategoria.setAdapter(adapter);
        //Boton añadir categoria
        BotonAñadirCategoria = (ImageButton) findViewById(R.id.imageButton);
    }

    public void onClickGuardar(View view) {
        Intent i = new Intent(this, PrincipalActivity.class);
        startActivityForResult(i, ACTIVITY_PRINCIPAL);
        finish();
    }

    public void onClickAñadirCategoria(View view) {
        openDialog();
    }

    public void openDialog() {
        PopupCategoria popupCategoria = new PopupCategoria();
        popupCategoria.show(getSupportFragmentManager(), "popup categoria");
    }
}