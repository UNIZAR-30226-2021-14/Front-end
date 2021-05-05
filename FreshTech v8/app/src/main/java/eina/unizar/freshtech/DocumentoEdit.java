package eina.unizar.freshtech;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.content.CursorLoader;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Categoria;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentoEdit extends AppCompatActivity implements PopupCategoria.OnCategoriaCreadaEdit {

    private static final int ACTIVITY_PRINCIPAL=0;
    private static final int REQUEST_PERMISSION_CODE=1;
    private static final int REQUEST_DOCUMENT=2;

    private Spinner SpinnerCategoria;
    private ImageButton BotonAñadirCategoria;
    private ImageButton botonSubirDocumento;
    private EditText fechaCreacion;
    private EditText fechaExpiracion;
    private AwesomeValidation awesomeValidation;

    private String nombreEditar = null;
    private String nuevaCategoria = "";

    private String realPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documento_edit);
        //Spinner Categorias
        SpinnerCategoria = (Spinner) findViewById(R.id.spinner2);
        String [] CategoriasEjemplo = {"", "Categoria 1", "Categoria 2", "Categoria 3"};
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this, R.layout.simple_spinner_item, CategoriasEjemplo);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        SpinnerCategoria.setAdapter(adapter);
        //Boton añadir categoria
        //Boton subir imagen
        botonSubirDocumento = (ImageButton) findViewById(R.id.ImagenId);
        //Fecha creacion
        fechaCreacion = (EditText) findViewById(R.id.fechaCreacionDocumento);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fechaCreacion.setText(dateFormat.format(date));
        //Fecha expiración
        fechaExpiracion = (EditText) findViewById(R.id.fechaExpiracionDocumento);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        fechaExpiracion.setText(dateFormat.format(calendar.getTime()));
        fechaExpiracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DocumentoEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = year+"-"+month+"-"+day;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        Date caducidadFecha = null;
                        try {
                            caducidadFecha = dateFormat.parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        date = dateFormat.format(caducidadFecha);
                        fechaExpiracion.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        populateFields();
    }

    private void populateFields() {
        fillSpinner(false);
    }

    private void fillSpinner(final boolean despuesDePopUp) {
        String token = "Bearer " + Preferencias.cargarToken(this);
        Call<ArrayList<Categoria>> call = ApiAdapter.getApiService().obtenerCategorias(token);
        call.enqueue(new Callback<ArrayList<Categoria>>() {
            @Override
            public void onResponse(Call<ArrayList<Categoria>> call, Response<ArrayList<Categoria>> response) {
                if(response.isSuccessful()) {
                    ArrayList<Categoria> listCategorias = response.body();
                    Log.d("onResponse SpinnerCat", "Size of elements =>" + listCategorias.size());
                    ArrayList<String> CategoriasSpinner = new ArrayList<>();
                    CategoriasSpinner.add("");
                    for (int i = 0; i < listCategorias.size(); i++) {
                        CategoriasSpinner.add(listCategorias.get(i).getNombre());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(DocumentoEdit.this, R.layout.simple_spinner_item, CategoriasSpinner);
                    adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                    SpinnerCategoria.setAdapter(adapter);
                    if(!despuesDePopUp) {
                        //fillData();
                    }
                    else {
                        SpinnerCategoria.setSelection(((ArrayAdapter<String>)SpinnerCategoria.getAdapter()).getPosition(nuevaCategoria));
                    }
                }
                else {
                    Log.d("onResponse getPasswd", "Mensaje => not successful");
                    Toast.makeText(DocumentoEdit.this, "No se han podido obtener los elementos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Categoria>> call, Throwable t) {
                Log.d("onFailure getcat", "Mensaje => No ha funcionado");
                Toast.makeText(DocumentoEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickGuardar(View view) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this, R.id.huecoNombreDocumento, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        if (awesomeValidation.validate()) {
            finish();
        }
        else {
            Toast.makeText(this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickAñadirCategoria(View view) {
        openDialogCategoria();
    }

    public void onClickGenerarContraseña(View view) {
        openDialogGenerar();
    }

    public void onClickSubirDocumento(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(DocumentoEdit.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                selectPDFFile();
            }
            else {
                ActivityCompat.requestPermissions(DocumentoEdit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);

            }
        } else {
            selectPDFFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (permissions.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPDFFile();
            } else {
                Toast.makeText(this, "Necesita dar permisos", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void selectPDFFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent.createChooser(intent, "Seleccione un PDF"), REQUEST_DOCUMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String nombre = "";
        if (requestCode == REQUEST_DOCUMENT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri path = data.getData();
                realPath = getRealPathFromUri(path);
                botonSubirDocumento.setImageResource(R.drawable.ic_pdf);
            } else {
                Toast.makeText(this, "No ha elegido ningun archivo", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null,null,null);
        Cursor cursor = loader.loadInBackground();
        int column_idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_idx);
        cursor.close();
        return result;
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
    public void recargarSpinner(String nombreNuevaCategoria) {
        nuevaCategoria = nombreNuevaCategoria;
        fillSpinner(true);
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

    public void onClickDescargarDocumento(View view) {

    }
}