package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

public class PopupOrdenar extends AppCompatDialogFragment {

    private RadioGroup OrdenarPor;
    private RadioGroup OrdenarDe;
    private RadioButton Seleccionado1;
    private RadioButton Seleccionado2;

    String ordenarPor;
    String ordenarDe;
    int idOrdenarPor;
    int idOrdenarDe;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_ordenar, null);
        //Cargar los botones que deben estar activados
        cargarPreferencias();
        OrdenarPor = view.findViewById(R.id.radioGroup1);
        OrdenarPor.check(idOrdenarPor);
        OrdenarDe = view.findViewById(R.id.radioGroup2);
        OrdenarDe.check(idOrdenarDe);
        //Alert dialog
        builder.setView(view)
                .setTitle(Html.fromHtml("<font color='#008577'>Ordenar</font>"))
                .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Seleccionado1 = view.findViewById(OrdenarPor.getCheckedRadioButtonId());
                        Seleccionado2 = view.findViewById(OrdenarDe.getCheckedRadioButtonId());
                        guardarPreferencias();
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        return alertDialog;
    }

    private void guardarPreferencias() {
        SharedPreferences preferences = getActivity().getSharedPreferences("ordenar", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("OrdenarPor",Seleccionado1.getText().toString());
        editor.putString("OrdenarDe", Seleccionado2.getText().toString());
        editor.commit();
    }

    private void cargarPreferencias() {
        SharedPreferences preferences = getActivity().getSharedPreferences("ordenar", Context.MODE_PRIVATE);
        ordenarPor = preferences.getString("OrdenarPor", "No existe la información");
        ordenarDe = preferences.getString("OrdenarDe", "No existe la informacion");
        if(ordenarPor.equals("Nombre")) idOrdenarPor = R.id.radio_nombre;
        else if(ordenarPor.equals("Categoría")) idOrdenarPor = R.id.radio_categoria;
        else if(ordenarPor.equals("Fecha de creación")) idOrdenarPor = R.id.radio_fechaCreacion;
        else if(ordenarPor.equals("Fecha de expiración")) idOrdenarPor = R.id.radio_fechaExpiracion;
        else Toast.makeText(getActivity(), "Error al ordenar por", Toast.LENGTH_SHORT).show();

        if(ordenarDe.equals("Menor a mayor")) idOrdenarDe = R.id.radio_menor_mayor;
        else if(ordenarDe.equals("Mayor a menor")) idOrdenarDe = R.id.radio_mayor_menor;
        else Toast.makeText(getActivity(), "Error al ordenar de", Toast.LENGTH_SHORT).show();
    }
}
