package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopupCategoria extends AppCompatDialogFragment implements Callback<Registrar> {

    private EditText editTextNombreCategoria;
    private String titulo = "Añadir";
    private AwesomeValidation awesomeValidation;
    private AlertDialog alertDialog;

    public interface OnCategoriaCreada {
        void recargarListado();
    }
    public OnCategoriaCreada mOnCategoriaCreada;

    public interface OnCategoriaCreadaEdit {
        void recargarSpinner(String nombreNuevaCategoria);
    }
    public OnCategoriaCreadaEdit mOnCategoriaCreadaEdit;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_categoria, null);
        editTextNombreCategoria = view.findViewById(R.id.añadir_categoria);

        if (titulo == "Añadir") {
            builder.setTitle(Html.fromHtml("<font color='#008577'>Añadir categoría</font>"));
        } else {
            builder.setTitle(Html.fromHtml("<font color='#008577'>Editar categoría</font>"));
            editTextNombreCategoria.setHint("Nuevo nombre de la categoría");
        }

        builder.setView(view)
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ACEPTAR", null);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String nombreCategoria = editTextNombreCategoria.getText().toString();
                        if (!nombreCategoria.isEmpty()) {
                            String token = "Bearer " + Preferencias.cargarToken(getActivity());
                            Call<Registrar> callAñadirCategoria = ApiAdapter.getApiService().anyadirCategoria(token, nombreCategoria);
                            callAñadirCategoria.enqueue(PopupCategoria.this);
                        }
                        else {
                            editTextNombreCategoria.setError("Introduzca un nombre válido");
                        }
                    }
                });
            }
        });
        return alertDialog;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public AlertDialog getDialogo() {
        return alertDialog;
    }

    @Override
    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
        if(response.isSuccessful()) {
            Registrar registrar = response.body();
            if(registrar.getMessage().equals("Category created")) {
                if(getActivity() instanceof ContraseñaEdit) {
                    mOnCategoriaCreadaEdit.recargarSpinner(editTextNombreCategoria.getText().toString());
                }
                if(getTargetFragment() != null) {
                    mOnCategoriaCreada.recargarListado();
                }
                alertDialog.dismiss();
            }
            else {
                Toast.makeText(getActivity(), "Ha ocurrido un error al almacenar la categoría", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Registrar registrar = new Gson().fromJson(response.errorBody().charStream(),Registrar.class);
            if(registrar.getMessage().equals("Already a category with that name")) {
                editTextNombreCategoria.setError("Ya existe una categoría con ese nombre");
            }
            else {
                Log.d("onResponse addpw", "Mensaje => not successful");
                Toast.makeText(getActivity(), "No se ha podido almacenar la categoría", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<Registrar> call, Throwable t) {
        Log.d("onFailure editcat", "Mensaje => No ha funcionado");
        Toast.makeText(getActivity(), "Error onFailure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if(getActivity() instanceof ContraseñaEdit) {
                mOnCategoriaCreadaEdit = (OnCategoriaCreadaEdit) getActivity();
            } else {
                mOnCategoriaCreada = (OnCategoriaCreada) getTargetFragment();
            }
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "OnCategoriaCreada");
        }
    }
}
