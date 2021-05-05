package eina.unizar.freshtech;

import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import eina.unizar.freshtech.adapter.ElementoAdapter;
import eina.unizar.freshtech.adapter.ElementoItem;
import eina.unizar.freshtech.io.ApiAdapter;
import eina.unizar.freshtech.model.Categoria;
import eina.unizar.freshtech.model.Elemento;
import eina.unizar.freshtech.model.Registrar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriasFragment extends Fragment implements PopupCategoria.OnCategoriaCreada, Callback<ArrayList<Categoria>> {

    private static final int ACTIVITY_EDIT=0;

    private static final int EDIT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private ListView listView;
    private ArrayList<String> names;
    private FloatingActionsMenu grupoBotones;
    private com.getbase.floatingactionbutton.FloatingActionButton fabContraseña, fabDocumento, fabImagen, fabCategoria;

    ArrayList<Categoria> listCategorias;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorias, container, false);
        //Lista de items
        listView = (ListView) view.findViewById(R.id.listViewCategorias);
        cargarListado();
        /*names = new ArrayList<>();
        names.add("Categoría 1");
        names.add("Categoría 2");
        names.add("Categoría 3");*/
        registerForContextMenu(listView);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, names);
        listView.setAdapter(adapter);
        MyAdapter myAdapter = new MyAdapter(getActivity(), R.layout.list_item, names);
        listView.setAdapter(myAdapter);*/

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
                        ContraseñaEdit.class);
                startActivityForResult(intent, ACTIVITY_EDIT);
            }
        });

        fabImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        ContraseñaEdit.class);
                startActivityForResult(intent, ACTIVITY_EDIT);
            }
        });

        fabCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCategoria("Añadir");
            }
        });

        return view;
    }

    private void cargarListado() {
        String token = "Bearer " + Preferencias.cargarToken(getActivity());
        Call<ArrayList<Categoria>> call = ApiAdapter.getApiService().obtenerCategorias(token);
        call.enqueue(this);
    }

    private void fillData() {
        ArrayList<String> CategoriasEjemplo = new ArrayList<>();
        for (int i = 0; i < listCategorias.size(); i++) {
            CategoriasEjemplo.add(listCategorias.get(i).getNombre());
        }
        ArrayAdapter<String> categoriaAdapter = new ArrayAdapter<>(getActivity(), R.layout.categoria_item, R.id.text_nombre_categoria, CategoriasEjemplo);
        listView.setAdapter(categoriaAdapter);
    }

    @Override
    public void onResponse(Call<ArrayList<Categoria>> call, Response<ArrayList<Categoria>> response) {
        if(response.isSuccessful()) {
            listCategorias = response.body();
            Log.d("onResponse elements", "Size of elements =>" + listCategorias.size());
            fillData();
        }
        else {
            Log.d("onResponse getPasswd", "Mensaje => not successful");
            Toast.makeText(getActivity(), "No se han podido obtener los elementos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFailure(Call<ArrayList<Categoria>> call, Throwable t) {
        Log.d("onFailure getcat", "Mensaje => No ha funcionado");
        Toast.makeText(getActivity(), "Error onFailure", Toast.LENGTH_SHORT).show();
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
                openDialogCategoria("Editar");
                return true;
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                String name = (String) (listView.getItemAtPosition(info.position));
                String token = "Bearer " + Preferencias.cargarToken(getActivity());
                Call<Registrar> call = ApiAdapter.getApiService().eliminarCategoria(token, name);
                call.enqueue(new Callback<Registrar>() {
                    @Override
                    public void onResponse(Call<Registrar> call, Response<Registrar> response) {
                        if(response.isSuccessful()) {
                            Registrar registrar = response.body();
                            if(registrar.getMessage().equals("Category deleted")) {
                                cargarListado();
                            }
                            else {
                                Toast.makeText(getActivity(), "Ha ocurrido un error al eliminar la categoria", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Registrar> call, Throwable t) {
                        Log.d("onFailure deleteCat", "Mensaje => No ha funcionado");
                        Toast.makeText(getActivity(), "Error onFailure", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
        }
        return super.onContextItemSelected(item);
    }

    protected void editItem() {
        Intent i = new Intent(getActivity(), ContraseñaEdit.class);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    public void openDialogCategoria(String titulo) {
        final PopupCategoria popupCategoria = new PopupCategoria();
        popupCategoria.setTitulo(titulo);
        popupCategoria.setTargetFragment(CategoriasFragment.this, 1);
        popupCategoria.show(getFragmentManager(), "popup generar");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        cargarListado();
    }

    @Override
    public void recargarListado() {
        cargarListado();
    }
}