package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class PopupCategoria extends AppCompatDialogFragment {

    private EditText editTextNombreCategoria;
    private String titulo = "Añadir";
    private AwesomeValidation awesomeValidation;

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
        final AlertDialog alertDialog = builder.create();
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
                            alertDialog.dismiss();
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
}
