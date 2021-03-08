package eina.unizar.freshtech;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ConfiguracionFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracion, container, false);
        //Botón actualizar correo
        Button botonActualizarCorreo = (Button) view.findViewById(R.id.BotonActualizarCorreo);
        botonActualizarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Se ha actualizado el correo electrónico", Toast.LENGTH_SHORT).show();
            }
        });

        //Botón actualizar contraseña
        Button botonActualizarContraseña = (Button) view.findViewById(R.id.BotonActualizarContraseña);
        botonActualizarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Se ha actualizado la contraseña", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}