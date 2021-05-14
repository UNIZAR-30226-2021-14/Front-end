package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import eina.unizar.freshtech.adapter.CaducadoAdapter;
import eina.unizar.freshtech.adapter.CaducadoItem;
import eina.unizar.freshtech.adapter.ElementoAdapter;
import eina.unizar.freshtech.adapter.ElementoItem;
import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Elemento;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopupCaducados extends AppCompatDialogFragment {

    private TextView tituloCaducados;
    private ListView listView;
    ArrayList<Elemento> listElementos;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.popup_caducados, null);
        tituloCaducados = (TextView) view.findViewById(R.id.tituloCaducados);
        //Lista de items
        listView = (ListView) view.findViewById(R.id.listViewCaducados);
        cargarListado();
        //Alert dialog
        builder.setView(view)
                .setTitle(Html.fromHtml("<font color='#008577'>Notificaciones</font>"))
                .setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
        return alertDialog;
    }

    private void cargarListado() {
        String token = "Bearer " + Preferencias.cargarToken(getActivity());
        String ordenarPor = Preferencias.cargarOrdenPor(getActivity());
        String ordenarDe = Preferencias.cargarOrdenDe(getActivity());
        Call<ArrayList<Elemento>> call = ApiAdapter.getApiService().obtenerElementos(token, ordenarPor, ordenarDe);
        call.enqueue(new Callback<ArrayList<Elemento>>() {
            @Override
            public void onResponse(Call<ArrayList<Elemento>> call, Response<ArrayList<Elemento>> response) {
                if(response.isSuccessful()) {
                    listElementos = response.body();
                    Log.d("onResponse elements", "Size of elements =>" + listElementos.size());
                    fillData();
                }
                else {
                    Log.d("onResponse getPasswd", "Mensaje => not successful");
                    Toast.makeText(getActivity(), "No se han podido obtener los elementos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Elemento>> call, Throwable t) {
                Log.d("onFailure getPasswd", "Mensaje => No ha funcionado");
                Toast.makeText(getActivity(), "Error onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fillData() {
        ArrayList<CaducadoItem> CaducadosEjemplo = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String fechaActual = dateFormat.format(date);
        for (int i = 0; i < listElementos.size(); i++) {
            int resIcono = 0;
            if(listElementos.get(i).getTipo().equals("usuario-passwd")) {
                resIcono = R.drawable.ic_candado;
            } else if(listElementos.get(i).getTipo().equals("imagen")) {
                resIcono = R.drawable.ic_imagen;
            } else {
                resIcono = R.drawable.ic_documento;
            }
            if(listElementos.get(i).getFechacaducidad().compareTo(fechaActual) < 0 && !listElementos.get(i).getFechacaducidad().isEmpty()) {
                CaducadosEjemplo.add(new CaducadoItem(resIcono, listElementos.get(i).getNombre(), listElementos.get(i).getFechacaducidad()));
            }
        }
        if(CaducadosEjemplo.size() == 0) {
            tituloCaducados.setText("No hay ningún elemento caducado");
        }
        tituloCaducados.setVisibility(View.VISIBLE);
        CaducadoAdapter caducadoAdapter = new CaducadoAdapter(getActivity(), R.layout.caducados_item, CaducadosEjemplo);

        listView.setAdapter(caducadoAdapter);
    }
}
