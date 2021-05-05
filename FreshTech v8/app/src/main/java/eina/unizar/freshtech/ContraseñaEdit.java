package eina.unizar.freshtech;

import android.app.DatePickerDialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Categoria;
import eina.unizar.freshtech.model.ElementoDetallado;
import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContraseñaEdit extends AppCompatActivity implements PopupCategoria.OnCategoriaCreadaEdit, PopupGenerar.DialogListener, Callback<Registrar> {

    private Spinner SpinnerCategoria;

    private EditText nombreContraseña;
    private EditText url;
    private EditText nombreUsuario;
    private EditText contraseña;
    private EditText fechaCreacion;
    private EditText fechaExpiracion;
    private TextInputLayout huecoFechaExpiracion;
    private ImageButton añadirCategoria;
    private ImageButton generar;
    private Button botonGuardar;
    private AwesomeValidation awesomeValidation;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private boolean isAtLeast8, hasMayus, hasMinus, hasNums, hasEsp;

    private String nombreEditar = null;
    private String nuevaCategoria = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contrasenya_edit);
        nombreContraseña = (EditText) findViewById(R.id.nombreContraseña);
        url = (EditText) findViewById(R.id.urlContraseña);
        nombreUsuario = (EditText) findViewById(R.id.usuarioContraseña);
        //Spinner Categorias
        SpinnerCategoria = (Spinner) findViewById(R.id.spinner2);
        //Contraseña
        contraseña = (EditText) findViewById(R.id.contraseñaEdit);
        //Fecha creacion
        fechaCreacion = (EditText) findViewById(R.id.fechaCreacionContraseña);
        fechaCreacion.setEnabled(false);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fechaCreacion.setText(dateFormat.format(date));
        //Fecha expiración y datepicker
        fechaExpiracion = (EditText) findViewById(R.id.fechaExpiracionContraseña);
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
                        ContraseñaEdit.this, new DatePickerDialog.OnDateSetListener() {
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

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            nombreEditar = extras.getString("nombreCategoria");
            if(extras.getString("verContraseña") != null) {
                // NO PERMITIR EDITAR
                huecoFechaExpiracion = findViewById(R.id.huecoFechaExpiracionContraseña);
                añadirCategoria = findViewById(R.id.imageButton);
                generar = findViewById(R.id.imageButton2);
                botonGuardar = findViewById(R.id.button4);
                nombreContraseña.setEnabled(false);
                SpinnerCategoria.setEnabled(false);
                url.setEnabled(false);
                nombreUsuario.setEnabled(false);
                contraseña.setEnabled(false);
                fechaExpiracion.setEnabled(false);
                huecoFechaExpiracion.setEndIconMode(TextInputLayout.END_ICON_NONE);
                añadirCategoria.setEnabled(false);
                añadirCategoria.setVisibility(View.INVISIBLE);
                generar.setEnabled(false);
                generar.setVisibility(View.INVISIBLE);
                botonGuardar.setEnabled(false);
                botonGuardar.setVisibility(View.INVISIBLE);

                nombreContraseña.setTextColor(getResources().getColor(R.color.grisOscuro));;
                url.setTextColor(getResources().getColor(R.color.grisOscuro));
                nombreUsuario.setTextColor(getResources().getColor(R.color.grisOscuro));
                contraseña.setTextColor(getResources().getColor(R.color.grisOscuro));
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ContraseñaEdit.this, R.layout.simple_spinner_item, CategoriasSpinner);
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
                    Toast.makeText(ContraseñaEdit.this, "No se han podido obtener los elementos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Categoria>> call, Throwable t) {
                Log.d("onFailure getcat", "Mensaje => No ha funcionado");
                Toast.makeText(ContraseñaEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillData() {
        if(nombreEditar != null) {
            String token = "Bearer " + Preferencias.cargarToken(this);
            Call<ElementoDetallado> call = ApiAdapter.getApiService().obtenerElemento(token, nombreEditar);
            call.enqueue(new Callback<ElementoDetallado>() {
                @Override
                public void onResponse(Call<ElementoDetallado> call, Response<ElementoDetallado> response) {
                    if(response.isSuccessful()) {
                        ElementoDetallado elementoDetallado = response.body();
                        nombreContraseña.setText(nombreEditar);
                        url.setText(elementoDetallado.getDominio());
                        if(elementoDetallado.getCategoria() == null) {
                            SpinnerCategoria.setSelection(((ArrayAdapter<String>)SpinnerCategoria.getAdapter()).getPosition(""));
                        }
                        else {
                            SpinnerCategoria.setSelection(((ArrayAdapter<String>)SpinnerCategoria.getAdapter()).getPosition(elementoDetallado.getCategoria()));
                        }
                        nombreUsuario.setText(elementoDetallado.getConcreteuser());
                        contraseña.setText(elementoDetallado.getConcretpasswd());
                        fechaCreacion.setText(elementoDetallado.getFechacreacion());
                        fechaExpiracion.setText(elementoDetallado.getFechacaducidad());
                    }
                    else {
                        Log.d("onResponse getDetailPwd", "Mensaje => not successful");
                        Toast.makeText(ContraseñaEdit.this, "No se ha podido obtener el elemento", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ElementoDetallado> call, Throwable t) {
                    Log.d("onFailure getDetailPwd", "Mensaje => No ha funcionado");
                    Toast.makeText(ContraseñaEdit.this, "Error onFailure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void onClickGuardar(View view) {
        awesomeValidation = new AwesomeValidation(ValidationStyle.TEXT_INPUT_LAYOUT);
        awesomeValidation.addValidation(this, R.id.huecoNombreContraseña, RegexTemplate.NOT_EMPTY, R.string.invalid_name);
        //awesomeValidation.addValidation(this, R.id.huecoUrlContraseña, Patterns.WEB_URL, R.string.invalid_url);
        if (awesomeValidation.validate()) {
            String categoria = SpinnerCategoria.getSelectedItem().toString();
            if(categoria.equals("")) {
                categoria = null;
            }
            if(nombreEditar != null) {
                String token = "Bearer " + Preferencias.cargarToken(this);
                Call<Registrar> callEditarContraseña = ApiAdapter.getApiService().editarContrasenya(token, nombreEditar, nombreUsuario.getText().toString(), contraseña.getText().toString(),
                        url.getText().toString(), categoria, fechaCreacion.getText().toString(), fechaExpiracion.getText().toString(), nombreContraseña.getText().toString());
                callEditarContraseña.enqueue(this);
            }
            else {
                String token = "Bearer " + Preferencias.cargarToken(this);
                Call<Registrar> callAñadirContraseña = ApiAdapter.getApiService().anyadirContrasenya(token, nombreUsuario.getText().toString(), contraseña.getText().toString(),
                        url.getText().toString(), fechaCreacion.getText().toString(), fechaExpiracion.getText().toString(), nombreContraseña.getText().toString(), categoria);
                callAñadirContraseña.enqueue(this);
            }
        }
        else {
            Toast.makeText(this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
            scrollView.fullScroll(View.FOCUS_UP);
        }
    }

    @Override
    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
        if(response.isSuccessful()) {
            Registrar registrar = response.body();
            if(registrar.getMessage().equals("Contraseña introducida correctamente")) {
                onBackPressed();
            }
            else if(registrar.getMessage().equals("Contraseña editada correctamente!!")) {
                onBackPressed();
            }
            else {
                Toast.makeText(this, "Ha ocurrido un error al almacenar la contraseña", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Registrar registrar = new Gson().fromJson(response.errorBody().charStream(),Registrar.class);
            if(registrar.getMessage().equals("Ya tiene una contraseña con ese nombre")) {
                Toast.makeText(this, "Revise la validación de los campos", Toast.LENGTH_SHORT).show();
                TextInputLayout huecoNombre = (TextInputLayout) findViewById(R.id.huecoNombreContraseña);
                huecoNombre.setError("Ya existe un elemento con ese nombre");
            }
            else {
                Log.d("onResponse addpw", "Mensaje => not successful");
                Toast.makeText(this, "No se ha podido almacenar la contraseña", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<Registrar> call, Throwable t) {
        Log.d("onFailure editPasswd", "Mensaje => No ha funcionado");
        Toast.makeText(this, "Error onFailure", Toast.LENGTH_SHORT).show();
    }

    public void onClickAñadirCategoria(View view) {
        openDialogCategoria();
    }

    public void onClickGenerarContraseña(View view) {
        openDialogGenerar();
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
}