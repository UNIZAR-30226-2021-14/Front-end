package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PopupCategoria extends AppCompatDialogFragment {

    private EditText editTextNombreCategoria;
    private String titulo = "Añadir";

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
                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombreCategoria = editTextNombreCategoria.getText().toString();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        return alertDialog;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
