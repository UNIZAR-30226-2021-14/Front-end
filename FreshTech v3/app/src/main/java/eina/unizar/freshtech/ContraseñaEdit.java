package eina.unizar.freshtech;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;

public class ContraseñaEdit extends AppCompatActivity {

    private static final int ACTIVITY_PRINCIPAL=0;
    private static final int REQUEST_PERMISSION_CODE=1;
    private static final int REQUEST_IMAGE_GALLERY=2;

    private Spinner SpinnerCategoria;
    private ImageButton BotonAñadirCategoria;
    private ImageButton botonSubirImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contrasenya_edit);
        //Spinner Categorias
        SpinnerCategoria = (Spinner) findViewById(R.id.spinner2);
        String [] CategoriasEjemplo = {"", "Categoria 1", "Categoria 2", "Categoria 3"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.simple_spinner_item, CategoriasEjemplo);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        SpinnerCategoria.setAdapter(adapter);
        //Boton añadir categoria
        BotonAñadirCategoria = (ImageButton) findViewById(R.id.imageButton);
        //Boton subir imagen
        botonSubirImagen = (ImageButton) findViewById(R.id.ImagenId);
    }

    public void onClickGuardar(View view) {
        Intent i = new Intent(this, PrincipalActivity.class);
        startActivityForResult(i, ACTIVITY_PRINCIPAL);
        finish();
    }

    public void onClickAñadirCategoria(View view) {
        openDialogCategoria();
    }

    public void onClickGenerarContraseña(View view) {
        openDialogGenerar();
    }

    public void onClickSubirImagen(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(ContraseñaEdit.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else {
                ActivityCompat.requestPermissions(ContraseñaEdit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);

            }
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Necesita dar permisos", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*, .pdf");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String nombre = "";
        if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri path = data.getData();
                botonSubirImagen.setImageURI(path);
                File file = new File(path.getPath());
                nombre = file.getName();
                Log.i("TAG", "Result: "+ nombre);
            } else {
                Toast.makeText(this, "No ha elegido ningun archivo", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void openDialogCategoria() {
        PopupCategoria popupCategoria = new PopupCategoria();
        popupCategoria.show(getSupportFragmentManager(), "popup categoria");
    }

    public void openDialogGenerar() {
        PopupGenerar popupGenerar = new PopupGenerar();
        popupGenerar.show(getSupportFragmentManager(), "popup generar");
    }
}