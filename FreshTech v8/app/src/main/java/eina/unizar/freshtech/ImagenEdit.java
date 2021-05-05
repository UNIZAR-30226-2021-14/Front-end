package eina.unizar.freshtech;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Categoria;
import eina.unizar.freshtech.model.ElementoDetallado;
import eina.unizar.freshtech.model.ElementoDetalladoImagen;
import eina.unizar.freshtech.model.Registrar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ImagenEdit extends AppCompatActivity implements PopupCategoria.OnCategoriaCreadaEdit {

    private static final int ACTIVITY_PRINCIPAL=0;
    private static final int REQUEST_PERMISSION_CODE=1;
    private static final int REQUEST_IMAGE_GALLERY=2;
    private static final int MY_PERMISSIONS_REQUEST=100;

    private Spinner SpinnerCategoria;
    private ImageButton BotonAñadirCategoria;
    private ImageButton botonSubirImagen;
    private ImageButton botonDescargarImagen;
    private EditText nombreImagen;
    private EditText fechaCreacion;
    private EditText fechaExpiracion;
    private AwesomeValidation awesomeValidation;

    private String nombreEditar = null;
    private String nuevaCategoria = "";

    private String realPath = null;
    private String nombreImagenRecibida = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.imagen_edit);
        nombreImagen = (EditText) findViewById(R.id.nombreImagen);
        //Spinner Categorias
        SpinnerCategoria = (Spinner) findViewById(R.id.spinner2);
        //Boton añadir categoria
        //Boton subir imagen
        botonSubirImagen = (ImageButton) findViewById(R.id.ImagenId);
        //Boton descargar imagen
        botonDescargarImagen = (ImageButton) findViewById(R.id.imageButton3);
        //Fecha creacion
        fechaCreacion = (EditText) findViewById(R.id.fechaCreacionImagen);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fechaCreacion.setText(dateFormat.format(date));
        //Fecha expiración
        fechaExpiracion = (EditText) findViewById(R.id.fechaExpiracionImagen);
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
                        ImagenEdit.this, new DatePickerDialog.OnDateSetListener() {
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ImagenEdit.this, R.layout.simple_spinner_item, CategoriasSpinner);
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
                    Toast.makeText(ImagenEdit.this, "No se han podido obtener los elementos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Categoria>> call, Throwable t) {
                Log.d("onFailure getcat", "Mensaje => No ha funcionado");
                Toast.makeText(ImagenEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillData() {
        if(nombreEditar != null) {
            String token = "Bearer " + Preferencias.cargarToken(this);
            Call<ElementoDetalladoImagen> call = ApiAdapter.getApiService().obtenerElementoImagen(token, nombreEditar);
            call.enqueue(new Callback<ElementoDetalladoImagen>() {
                @Override
                public void onResponse(Call<ElementoDetalladoImagen> call, Response<ElementoDetalladoImagen> response) {
                    if(response.isSuccessful()) {
                        final ElementoDetalladoImagen elementoDetalladoImagen = response.body();
                        nombreImagen.setText(nombreEditar);
                        if(elementoDetalladoImagen.getCategoria() == null) {
                            SpinnerCategoria.setSelection(((ArrayAdapter<String>)SpinnerCategoria.getAdapter()).getPosition(""));
                        }
                        else {
                            SpinnerCategoria.setSelection(((ArrayAdapter<String>)SpinnerCategoria.getAdapter()).getPosition(elementoDetalladoImagen.getCategoria()));
                        }
                        fechaCreacion.setText(elementoDetalladoImagen.getFechacreacion());
                        fechaExpiracion.setText(elementoDetalladoImagen.getFechacaducidad());
                        Call<ResponseBody> callImagen = ApiAdapter.getApiService().descargarImagen(elementoDetalladoImagen.getNombreImagen());
                        callImagen.enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if(response.isSuccessful()) {
                                    nombreImagenRecibida = elementoDetalladoImagen.getNombreImagen();
                                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                                    botonSubirImagen.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                    else {
                        Log.d("onResponse getDetailPwd", "Mensaje => not successful");
                        Toast.makeText(ImagenEdit.this, "No se ha podido obtener el elemento", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ElementoDetalladoImagen> call, Throwable t) {
                    Log.d("onFailure getDetailPwd", "Mensaje => No ha funcionado");
                    Toast.makeText(ImagenEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onClickGuardar(View view) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this, R.id.huecoNombreImagen, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        if (awesomeValidation.validate()) {
            if(nombreEditar != null) {
                String nombre = nombreImagen.getText().toString();
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
                RequestBody actualizaImagen = null;
                if(realPath != null) {
                    File file = new File(realPath);
                    fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    actualizaImagen = RequestBody.create(MediaType.parse("text/plain"), "si");
                } else {
                    actualizaImagen = RequestBody.create(MediaType.parse("text/plain"), "no");
                }
                String creacion = fechaCreacion.getText().toString();
                RequestBody fechacreacion = RequestBody.create(MediaType.parse("text/plain"), creacion);
                String caducidad = fechaExpiracion.getText().toString();
                RequestBody fechacaducidad = RequestBody.create(MediaType.parse("text/plain"), caducidad);
                String token = "Bearer " + Preferencias.cargarToken(this);
                Call<Registrar> callEditarImagen = ApiAdapter.getApiService().editarImagen(token, nombreNuevo, categoria, fbody,fechacreacion, fechacaducidad, nombreViejo, actualizaImagen);
                callEditarImagen.enqueue(new Callback<Registrar>() {
                    @Override
                    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
                        if (response.isSuccessful()) {
                            Registrar registrar = response.body();
                            if (registrar.getMessage().equals("ok")) {
                                onBackPressed();
                            } else if (registrar.getMessage().equals("no ok")) {
                                Toast.makeText(ImagenEdit.this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
                                TextInputLayout huecoNombre = (TextInputLayout) findViewById(R.id.huecoNombreImagen);
                                huecoNombre.setError("Ya existe un elemento con ese nombre");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Registrar> call, Throwable t) {
                        Log.d("onFailure editImagen", "Mensaje => No ha funcionado");
                        Toast.makeText(ImagenEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                if(realPath == null) {
                    Toast.makeText(ImagenEdit.this, "No ha elegido ninguna imagen", Toast.LENGTH_SHORT).show();
                    return;
                }
                String nombre = nombreImagen.getText().toString();
                RequestBody nombreImagen = RequestBody.create(MediaType.parse("text/plain"), nombre);
                String Categoria = SpinnerCategoria.getSelectedItem().toString();
                RequestBody categoria = null;
                if (Categoria.equals("")) {
                    Categoria = null;
                } else {
                    categoria = RequestBody.create(MediaType.parse("text/plain"), Categoria);
                }
                File file = new File(realPath);
                RequestBody fbody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                String creacion = fechaCreacion.getText().toString();
                RequestBody fechacreacion = RequestBody.create(MediaType.parse("text/plain"), creacion);
                String caducidad = fechaExpiracion.getText().toString();
                RequestBody fechacaducidad = RequestBody.create(MediaType.parse("text/plain"), caducidad);
                String token = "Bearer " + Preferencias.cargarToken(this);
                Call<Registrar> callAñadirImagen = ApiAdapter.getApiService().anyadirImagen(token, nombreImagen, categoria, fbody, fechacreacion, fechacaducidad);
                callAñadirImagen.enqueue(new Callback<Registrar>() {
                    @Override
                    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
                        if (response.isSuccessful()) {
                            Registrar registrar = response.body();
                            if (registrar.getMessage().equals("ok")) {
                                onBackPressed();
                            } else if (registrar.getMessage().equals("no ok")) {
                                Toast.makeText(ImagenEdit.this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
                                TextInputLayout huecoNombre = (TextInputLayout) findViewById(R.id.huecoNombreImagen);
                                huecoNombre.setError("Ya existe un elemento con ese nombre");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Registrar> call, Throwable t) {
                        Log.d("onFailure editImagen", "Mensaje => No ha funcionado");
                        Toast.makeText(ImagenEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
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

    public void onClickSubirImagen(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(ImagenEdit.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            }
            else {
                ActivityCompat.requestPermissions(ImagenEdit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_PERMISSION_CODE);

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
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String nombre = "";
        if (requestCode == REQUEST_IMAGE_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri path = data.getData();
                realPath = getRealPathFromUri(path);
                botonSubirImagen.setImageURI(path);
                Log.i("TAG", "Result: "+ data.getData());
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

    public void onClickDescargarImagen(View view) {
        if (ContextCompat.checkSelfPermission(ImagenEdit.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImagenEdit.this,new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);
        }
        Call<ResponseBody> callImagen = ApiAdapter.getApiService().descargarImagen(nombreImagenRecibida);
        callImagen.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean writtenToDisk = writeResponseBodyToDisk(response.body());
                if(writtenToDisk) {
                    Toast.makeText(ImagenEdit.this, "Descargado: " + nombreImagenRecibida, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImagenEdit.this, "Error al descargar", Toast.LENGTH_SHORT).show();
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
            File futureStudioIconFile = new File(getExternalFilesDir(null) + File.separator + nombreEditar + ".jpg");

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
