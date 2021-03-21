package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.SwitchCompat;

import static android.content.Context.CLIPBOARD_SERVICE;

public class PopupGenerarAvanzado extends AppCompatDialogFragment {

    private EditText editTextContraseñaGenerada;
    private TextView Longitud;
    private SeekBar BarraLongitud;
    private Switch activarMayus;
    private Switch activarMinus;
    private Switch activarNums;
    private Switch activarEsp;
    private PopupGenerar.DialogListener listener;
    private ImageButton GenerarContraseña;
    private ImageButton copiar;

    private boolean mayus = true;
    private boolean minus = true;
    private boolean nums = true;
    private boolean esp = true;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.popup_generar_avanzado, null);
        editTextContraseñaGenerada = view.findViewById(R.id.generar_contraseña_avanzada);
        editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(8));
        editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());

        //Alert dialog
        builder.setView(view)
                .setTitle(Html.fromHtml("<font color='#ffffff'>Generar contraseña</font>"))
                .setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("GUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String contraseñaGenerada = editTextContraseñaGenerada.getText().toString();
                        listener.applyText(contraseñaGenerada);
                    }
                });

        //Barra de longitud
        Longitud = view.findViewById(R.id.LongitudContraseñaAvanzada);
        BarraLongitud = view.findViewById(R.id.BarraLongitudAvanzada);
        BarraLongitud.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Longitud.setText("Longitud de contraseña: " + String.valueOf(progress+8));
                editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(mayus, minus, nums, esp, progress+8));
                editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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

        //Interruptores
        activarMayus = view.findViewById(R.id.switchMayus);
        activarMinus = view.findViewById(R.id.switchMinus);
        activarNums = view.findViewById(R.id.switchNums);
        activarEsp = view.findViewById(R.id.switchEsp);

        activarMayus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mayus = activarMayus.isChecked();
                if(!mayus && !activarMinus.isChecked() && !activarNums.isChecked() && !activarEsp.isChecked()) {
                    activarMinus.setChecked(true);
                    minus = true;
                }
                editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(mayus, minus, nums, esp, BarraLongitud.getProgress()+8));
                editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());
            }
        });

        activarMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minus = activarMinus.isChecked();
                if(!minus && !activarMayus.isChecked() && !activarNums.isChecked() && !activarEsp.isChecked()) {
                    activarMayus.setChecked(true);
                    mayus = true;
                }
                editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(mayus, minus, nums, esp, BarraLongitud.getProgress()+8));
                editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());
            }
        });

        activarNums.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nums = activarNums.isChecked();
                if(!nums && !activarMayus.isChecked() && !activarMinus.isChecked() && !activarEsp.isChecked()) {
                    activarMayus.setChecked(true);
                    mayus = true;
                }
                editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(mayus, minus, nums, esp, BarraLongitud.getProgress()+8));
                editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());
            }
        });

        activarEsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                esp = activarEsp.isChecked();
                if(!esp && !activarMayus.isChecked() && !activarMinus.isChecked() && !activarNums.isChecked()) {
                    activarMayus.setChecked(true);
                    mayus = true;
                }
                editTextContraseñaGenerada.setText(GeneradorContraseñas.getPassword(mayus, minus, nums, esp, BarraLongitud.getProgress()+8));
                editTextContraseñaGenerada.setSelection(editTextContraseñaGenerada.getText().length());
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
            listener = (PopupGenerar.DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Debe implementar DialogListener");
        }
    }

    public interface DialogListener {
        void applyText(String contraseñaGenerada);
    }
}
