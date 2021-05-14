package eina.unizar.freshtech;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PathPermission;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PathUtils;
import androidx.loader.content.CursorLoader;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Categoria;
import eina.unizar.freshtech.model.ElementoDetalladoImagen;
import eina.unizar.freshtech.model.Registrar;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sun.mail.util.ASCIIUtility.getBytes;

public class DocumentoEdit extends AppCompatActivity implements PopupCategoria.OnCategoriaCreadaEdit {

    private static final int ACTIVITY_PRINCIPAL=0;
    private static final int REQUEST_PERMISSION_CODE=1;
    private static final int REQUEST_DOCUMENT=2;
    private static final int MY_PERMISSIONS_REQUEST=100;

    private Spinner SpinnerCategoria;
    private ImageButton BotonAñadirCategoria;
    private ImageButton botonSubirDocumento;
    private EditText nombreDocumento;
    private EditText fechaCreacion;
    private EditText fechaExpiracion;
    private TextInputLayout huecoFechaExpiracion;
    private ImageButton añadirCategoria;
    private Button botonGuardar;
    private AwesomeValidation awesomeValidation;

    private String nombreEditar = null;
    private String nuevaCategoria = "";

    private String realPath = null;
    private String nombreDocumentoRecibido = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documento_edit);
        if (ContextCompat.checkSelfPermission(DocumentoEdit.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DocumentoEdit.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
        }
        nombreDocumento = (EditText) findViewById(R.id.nombreDocumento);
        //Spinner Categorias
        SpinnerCategoria = (Spinner) findViewById(R.id.spinner2);
        //Boton añadir categoria
        //Boton subir documento
        botonSubirDocumento = (ImageButton) findViewById(R.id.ImagenId);
        //Fecha creacion
        fechaCreacion = (EditText) findViewById(R.id.fechaCreacionDocumento);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fechaCreacion.setText(dateFormat.format(date));
        //Fecha expiración
        huecoFechaExpiracion = findViewById(R.id.huecoFechaExpiracionDocumento);
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

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            nombreEditar = extras.getString("nombreCategoria");
            if(extras.getString("verDocumento") != null) {
                // NO PERMITIR EDITAR
                setTitle("Ver documento");
                huecoFechaExpiracion = findViewById(R.id.huecoFechaExpiracionDocumento);
                añadirCategoria = findViewById(R.id.imageButton);
                botonGuardar = findViewById(R.id.guardarDocumento);
                nombreDocumento.setEnabled(false);
                SpinnerCategoria.setEnabled(false);
                fechaExpiracion.setEnabled(false);
                botonSubirDocumento.setEnabled(false);
                huecoFechaExpiracion.setEndIconMode(TextInputLayout.END_ICON_NONE);
                añadirCategoria.setEnabled(false);
                añadirCategoria.setVisibility(View.INVISIBLE);
                botonGuardar.setEnabled(false);
                botonGuardar.setVisibility(View.INVISIBLE);

                nombreDocumento.setTextColor(getResources().getColor(R.color.grisOscuro));;
                fechaExpiracion.setTextColor(getResources().getColor(R.color.grisOscuro));
            }
        }

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
                        fillData();
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

    private void fillData() {
        if(nombreEditar != null) {
            String token = "Bearer " + Preferencias.cargarToken(this);
            Call<ElementoDetalladoImagen> call = ApiAdapter.getApiService().obtenerElementoDocumento(token, nombreEditar);
            call.enqueue(new Callback<ElementoDetalladoImagen>() {
                @Override
                public void onResponse(Call<ElementoDetalladoImagen> call, Response<ElementoDetalladoImagen> response) {
                    if(response.isSuccessful()) {
                        final ElementoDetalladoImagen elementoDetalladoImagen = response.body();
                        nombreDocumento.setText(nombreEditar);
                        if(elementoDetalladoImagen.getCategoria() == null) {
                            SpinnerCategoria.setSelection(((ArrayAdapter<String>)SpinnerCategoria.getAdapter()).getPosition(""));
                        }
                        else {
                            SpinnerCategoria.setSelection(((ArrayAdapter<String>)SpinnerCategoria.getAdapter()).getPosition(elementoDetalladoImagen.getCategoria()));
                        }
                        fechaCreacion.setText(elementoDetalladoImagen.getFechacreacion());
                        fechaExpiracion.setText(elementoDetalladoImagen.getFechacaducidad());
                        Call<ResponseBody> callDocumento = ApiAdapter.getApiService().descargarDocumento(elementoDetalladoImagen.getNombreImagen());
                        callDocumento.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()) {
                                    nombreDocumentoRecibido = elementoDetalladoImagen.getNombreImagen();
                                    botonSubirDocumento.setImageResource(R.drawable.ic_pdf);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                    else {
                        Log.d("onResponse getFile", "Mensaje => not successful");
                        Toast.makeText(DocumentoEdit.this, "No se ha podido obtener el elemento", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ElementoDetalladoImagen> call, Throwable t) {
                    Log.d("onFailure getFile", "Mensaje => No ha funcionado");
                    Toast.makeText(DocumentoEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onClickGuardar(View view) {
        huecoFechaExpiracion.setErrorEnabled(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if(fechaExpiracion.getText().toString().compareTo(dateFormat.format(new Date())) < 0 && !fechaExpiracion.getText().toString().isEmpty()) {
            huecoFechaExpiracion.setError("Introduzca una fecha de expiración válida");
            return;
        }
        awesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this, R.id.huecoNombreDocumento, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        if (awesomeValidation.validate()) {
            if(nombreEditar != null) {
                String nombre = nombreDocumento.getText().toString();
                RequestBody nombreNuevo = RequestBody.create(MediaType.parse("text/plain"), nombre);
                RequestBody nombreViejo = RequestBody.create(MediaType.parse("text/plain"), nombreEditar);
                String Categoria = SpinnerCategoria.getSelectedItem().toString();
                RequestBody categoria = null;
                if (Categoria.equals("")) {
                    Categoria = null;
                } else {
                    categoria = RequestBody.create(MediaType.parse("text/plain"), Categoria);
                }
                RequestBody fbody = null;
                RequestBody actualizaDocumento = null;
                if(realPath != null) {
                    File file = new File(realPath);
                    fbody = RequestBody.create(MediaType.parse("application/pdf"), file);
                    actualizaDocumento = RequestBody.create(MediaType.parse("text/plain"), "si");
                } else {
                    actualizaDocumento = RequestBody.create(MediaType.parse("text/plain"), "no");
                }
                String creacion = fechaCreacion.getText().toString();
                RequestBody fechacreacion = RequestBody.create(MediaType.parse("text/plain"), creacion);
                String caducidad = fechaExpiracion.getText().toString();
                RequestBody fechacaducidad = RequestBody.create(MediaType.parse("text/plain"), caducidad);
                String token = "Bearer " + Preferencias.cargarToken(this);
                Call<Registrar> callEditarDocumento = ApiAdapter.getApiService().editarFile(token, nombreNuevo, categoria, fbody,fechacreacion, fechacaducidad, nombreViejo, actualizaDocumento);
                callEditarDocumento.enqueue(new Callback<Registrar>() {
                    @Override
                    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
                        if (response.isSuccessful()) {
                            Registrar registrar = response.body();
                            if (registrar.getMessage().equals("ok")) {
                                onBackPressed();
                            } else if (registrar.getMessage().equals("no ok")) {
                                Toast.makeText(DocumentoEdit.this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
                                TextInputLayout huecoNombre = (TextInputLayout) findViewById(R.id.huecoNombreDocumento);
                                huecoNombre.setError("Ya existe un elemento con ese nombre");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Registrar> call, Throwable t) {
                        Log.d("onFailure editDocumento", "Mensaje => No ha funcionado");
                        Toast.makeText(DocumentoEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                if(realPath == null) {
                    Toast.makeText(DocumentoEdit.this, "No ha elegido ningún documento", Toast.LENGTH_SHORT).show();
                    return;
                }
                String nombre = nombreDocumento.getText().toString();
                RequestBody nombreDocumento = RequestBody.create(MediaType.parse("text/plain"), nombre);
                String Categoria = SpinnerCategoria.getSelectedItem().toString();
                RequestBody categoria = null;
                if (Categoria.equals("")) {
                    Categoria = null;
                } else {
                    categoria = RequestBody.create(MediaType.parse("text/plain"), Categoria);
                }
                Log.d("SubriDocumento: ", "Mensaje => Entro aqui");
                File file = new File(realPath);
                Log.d("SubriDocumento: ", "Mensaje => " + file.exists());
                RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                String creacion = fechaCreacion.getText().toString();
                RequestBody fechacreacion = RequestBody.create(MediaType.parse("text/plain"), creacion);
                String caducidad = fechaExpiracion.getText().toString();
                RequestBody fechacaducidad = RequestBody.create(MediaType.parse("text/plain"), caducidad);
                String token = "Bearer " + Preferencias.cargarToken(this);
                Call<Registrar> callAñadirDocumento = ApiAdapter.getApiService().anyadirDocumento(token, nombreDocumento, categoria, fbody, fechacreacion, fechacaducidad);
                callAñadirDocumento.enqueue(new Callback<Registrar>() {
                    @Override
                    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
                        if (response.isSuccessful()) {
                            Registrar registrar = response.body();
                            if (registrar.getMessage().equals("ok")) {
                                onBackPressed();
                            } else if (registrar.getMessage().equals("no ok")) {
                                Toast.makeText(DocumentoEdit.this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
                                TextInputLayout huecoNombre = (TextInputLayout) findViewById(R.id.huecoNombreDocumento);
                                huecoNombre.setError("Ya existe un elemento con ese nombre");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Registrar> call, Throwable t) {
                        Log.d("onFailure editDocumento", "Mensaje => No ha funcionado");
                        Toast.makeText(DocumentoEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
        if (requestCode == REQUEST_DOCUMENT) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri path = data.getData();
                Log.d("SubriDocumento: ", "Mensaje => " + path);
                Log.d("SubriDocumento: ", "Mensaje => " + path.getPath());
                realPath = getRealPathFromUri(path);
                Log.d("SubriDocumentoReal: ", "Mensaje => " + realPath);
                botonSubirDocumento.setImageResource(R.drawable.ic_pdf);
            } else {
                Toast.makeText(this, "No ha elegido ningun archivo", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {"_display_name"};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null,null,null);
        Cursor cursor = loader.loadInBackground();
        String[] prueba = cursor.getColumnNames();
        for (int i = 0; i<prueba.length;i++) {
            Log.d("SubriDocumento: ", "Mensaje => " + prueba[i]);
        }
        int column_idx = cursor.getColumnIndexOrThrow("_display_name");
        cursor.moveToFirst();
        String result = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + cursor.getString(column_idx);
        Log.d("SubriDocumento: ", "Mensaje => " + result);
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
        Call<ResponseBody> callDocumento = ApiAdapter.getApiService().descargarDocumento(nombreDocumentoRecibido);
        callDocumento.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                if(writtenToDisk) {
                    Toast.makeText(DocumentoEdit.this, "Descargado: " + nombreDocumentoRecibido, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DocumentoEdit.this, "Error al descargar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            // todo change the file location/name according to your needs
            //File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + nombreEditar + ".pdf");
            File futureStudioIconFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/Download/" + nombreEditar + ".pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        Log.d("writeResponseBodyToDisk", "file download: " + fileSizeDownloaded + " of " + fileSize);
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}