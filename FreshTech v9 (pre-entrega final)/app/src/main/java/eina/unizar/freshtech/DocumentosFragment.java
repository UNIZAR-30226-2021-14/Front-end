package eina.unizar.freshtech;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import eina.unizar.freshtech.adapter.ElementoAdapter;
import eina.unizar.freshtech.adapter.ElementoItem;
import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Elemento;
import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentosFragment extends Fragment implements Callback<ArrayList<Elemento>> {

    private static final int ACTIVITY_EDIT=0;

    private static final int EDIT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private ListView listView;
    private ImageView icono;
    private ArrayList<String> names;
    private FloatingActionsMenu grupoBotones;
    private com.getbase.floatingactionbutton.FloatingActionButton fabContraseña, fabDocumento, fabImagen, fabCategoria;

    ArrayList<Elemento> listElementos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documentos, container, false);
        //Lista de items
        listView = (ListView) view.findViewById(R.id.listViewDocumentos);
        cargarListado();
        registerForContextMenu(listView);

        //Botones flotantes
        grupoBotones = (FloatingActionsMenu) view.findViewById(R.id.menuFlotante);
        fabContraseña = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.botonFlotanteAñadirContraseña);
        fabDocumento = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.botonFlotanteAñadirDocumento);
        fabImagen = (com.getbase.floatingactionbutton.FloatingActionButton) view.findViewById(R.id.botonFlotanteAñadirImagen);
        fabCategoria = (FloatingActionButton) view.findViewById(R.id.botonFlotanteAñadirCategoria);

        fabContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        ContraseñaEdit.class);
                startActivityForResult(intent, ACTIVITY_EDIT);
            }
        });

        fabDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        DocumentoEdit.class);
                startActivityForResult(intent, ACTIVITY_EDIT);
            }
        });

        fabImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        ImagenEdit.class);
                startActivityForResult(intent, ACTIVITY_EDIT);
            }
        });

        fabCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCategoria();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ElementoItem elementoItem = (ElementoItem) (listView.getItemAtPosition(position));
                editItem(elementoItem.getNombre(), true);
            }
        });

        return view;
    }

    private void cargarListado() {
        String token = "Bearer " + Preferencias.cargarToken(getActivity());
        String ordenarPor = Preferencias.cargarOrdenPor(getActivity());
        String ordenarDe = Preferencias.cargarOrdenDe(getActivity());
        Call<ArrayList<Elemento>> call = ApiAdapter.getApiService().obtenerElementos(token, ordenarPor, ordenarDe);
        call.enqueue(this);
    }

    private void fillData() {
        ArrayList<ElementoItem> ElementosEjemplo = new ArrayList<>();
        for (int i = 0; i < listElementos.size(); i++) {
            int resIcono = 0;
            if(listElementos.get(i).getTipo().equals("usuario-passwd")) {
                resIcono = R.drawable.ic_candado;
            } else if(listElementos.get(i).getTipo().equals("imagen")) {
                resIcono = R.drawable.ic_imagen;
            } else {
                resIcono = R.drawable.ic_documento;
                ElementosEjemplo.add(new ElementoItem(resIcono, listElementos.get(i).getNombre(), listElementos.get(i).getCategoria()));
            }
        }
        ElementoAdapter elementoAdapter = new ElementoAdapter(getActivity(), R.layout.list_item, ElementosEjemplo);

        listView.setAdapter(elementoAdapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit_product);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete_product);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case EDIT_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                ElementoItem elementoItem = (ElementoItem) (listView.getItemAtPosition(info.position));
                editItem(elementoItem.getNombre(), false);
                return true;
            case DELETE_ID:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                elementoItem = (ElementoItem) (listView.getItemAtPosition(info.position));
                String token = "Bearer " + Preferencias.cargarToken(getActivity());
                Call<Registrar> call = ApiAdapter.getApiService().eliminarContrasenya(token, elementoItem.getNombre());
                call.enqueue(new Callback<Registrar>() {
                    @Override
                    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
                        if(response.isSuccessful()) {
                            Registrar registrar = response.body();
                            if(registrar.getMessage().equals("Contraseña eliminada correctamente")) {
                                cargarListado();
                            }
                            else {
                                Toast.makeText(getActivity(), "Ha ocurrido un error al eliminar el documento", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Registrar> call, Throwable t) {
                        Log.d("onFailure deletePasswd", "Mensaje => No ha funcionado");
                        Toast.makeText(getActivity(), "Error onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
        }
        return super.onContextItemSelected(item);
    }

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

    protected void editItem(String nombrecategoria, boolean verDocumento) {
        Intent i = new Intent(getActivity(), DocumentoEdit.class);
        i.putExtra("nombreCategoria", nombrecategoria);
        if(verDocumento) {
            i.putExtra("verDocumento", "noEdit");
        }
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    public void openDialogCategoria() {
        PopupCategoria popupCategoria = new PopupCategoria();
        popupCategoria.show(getFragmentManager(), "popup generar");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cargarListado();
    }

    public void ordenarListado() {
        cargarListado();
    }
}