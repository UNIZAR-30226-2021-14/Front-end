package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PopupGenerar extends AppCompatDialogFragment {

    private EditText editTextContraseñaGenerada;
    private TextView Longitud;
    private SeekBar BarraLongitud;
    private DialogListener listener;
    private ImageButton GenerarContraseña;
    private ImageButton copiar;
    private ProgressBar progressBar;
    private boolean isAtLeast8, hasMayus, hasMinus, hasNums, hasEsp;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_generar, null);
        editTextContraseñaGenerada = view.findViewById(R.id.generar_contraseña);
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
        //Seguridad contraseña
        progressBar = view.findViewById(R.id.progressBarGenerar);
        editTextContraseñaGenerada.addTextChangedListener(new TextWatcher() {
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
                //progressBar.setProgressTintList(ColorStateList.valueOf(SeguridadContraseña.colorProgressBar(porcentajeSegura)));
                progressBar.getProgressDrawable().setColorFilter(getResources().getColor(SeguridadContraseña.colorProgressBar(porcentajeSegura)), android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });
        editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(8));
        editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());
        //Botones generar contraseña y copiar al portapapeles
        GenerarContraseña = view.findViewById(R.id.regenerarContraseña);
        copiar = view.findViewById(R.id.copiarAlPortapapeles);
        GenerarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(BarraLongitud.getProgress()+8));
                editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());
            }
        });
        copiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData clip = ClipData.newPlainText("contraseña generada", editTextContraseñaGenerada.getText().toString());
                ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getActivity(), "Contraseña copiada al portapapeles", Toast.LENGTH_SHORT).show();
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
