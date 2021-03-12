package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

public class PopupGenerar extends AppCompatDialogFragment {

    private EditText editTextContraseñaGenerada;
    private TextView Longitud;
    private SeekBar BarraLongitud;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_generar, null);
        editTextContraseñaGenerada = view.findViewById(R.id.generar_contraseña);
        editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(8));
        editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());

        //Barra de longitud
        Longitud = view.findViewById(R.id.LongitudContraseña);
        BarraLongitud = view.findViewById(R.id.BarraLongitud);
        BarraLongitud.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Longitud.setText("Longitud de contraseña: " + String.valueOf(progress+8));
                editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(progress+8));
                editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Alert dialog
        builder.setView(view)
                .setTitle(Html.fromHtml("<font color='#ffffff'>Generar contraseña</font>"))
                .setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("AVANZADO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PopupGenerarAvanzado popupGenerar = new PopupGenerarAvanzado();
                        popupGenerar.show(getFragmentManager(), "popup generar avanzado");
                    }
                })
                .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String contraseñaGenerada = editTextContraseñaGenerada.getText().toString();
                        listener.applyText(contraseñaGenerada);
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        return alertDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Debe implementar DialogListener");
        }
    }

    public interface DialogListener {
        void applyText(String contraseñaGenerada);
    }
}
