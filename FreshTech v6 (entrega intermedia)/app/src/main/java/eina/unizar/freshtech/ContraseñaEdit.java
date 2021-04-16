package eina.unizar.freshtech;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ContraseñaEdit extends AppCompatActivity implements PopupGenerar.DialogListener {

    private static final int ACTIVITY_PRINCIPAL=0;
    private static final int REQUEST_PERMISSION_CODE=1;
    private static final int REQUEST_IMAGE_GALLERY=2;

    private Spinner SpinnerCategoria;
    private ImageButton BotonAñadirCategoria;
    private ImageButton botonSubirImagen;
    private EditText contraseña;
    private EditText fechaCreacion;
    private EditText fechaExpiracion;
    private AwesomeValidation awesomeValidation;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private boolean isAtLeast8, hasMayus, hasMinus, hasNums, hasEsp;

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
        //Contraseña
        contraseña = (EditText) findViewById(R.id.contraseñaEdit);
        //Fecha creacion
        fechaCreacion = (EditText) findViewById(R.id.fechaCreacionContraseña);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fechaCreacion.setText(dateFormat.format(date));
        //Fecha expiración
        fechaExpiracion = (EditText) findViewById(R.id.fechaExpiracionContraseña);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        fechaExpiracion.setText(dateFormat.format(calendar.getTime()));
        scrollView = findViewById(R.id.scrollview);
        //Seguridad contraseña
        progressBar = (ProgressBar) findViewById(R.id.progressBarContraseña);
        contraseña.addTextChangedListener(new TextWatcher() {
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
                progressBar.getProgressDrawable().setColorFilter(getResources().getColor(SeguridadContraseña.colorProgressBar(porcentajeSegura)), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });
    }

    public void onClickGuardar(View view) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this, R.id.huecoNombreContraseña, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        //awesomeValidation.addValidation(this, R.id.huecoUrlContraseña, Patterns.WEB_URL, R.string.invalid_url);
        if (awesomeValidation.validate()) {
            finish();
        }
        else {
            Toast.makeText(this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
            scrollView.fullScroll(View.FOCUS_UP);
        }
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

    @Override
    public void applyText(String contraseñaGenerada) {
        contraseña.setText(contraseñaGenerada);
    }
}