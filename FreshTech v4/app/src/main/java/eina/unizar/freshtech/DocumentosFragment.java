package eina.unizar.freshtech;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class DocumentosFragment extends Fragment {

    private static final int ACTIVITY_EDIT=0;

    private static final int EDIT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private ListView listView;
    private ImageView icono;
    private ArrayList<String> names;
    private FloatingActionsMenu grupoBotones;
    private com.getbase.floatingactionbutton.FloatingActionButton fabContraseña, fabDocumento, fabImagen, fabCategoria;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_documentos, container, false);
        //Lista de items
        listView = (ListView) view.findViewById(R.id.listViewDocumentos);
        names = new ArrayList<>();
        names.add("Documento 1");
        names.add("Documento 2");
        names.add("Documento 3");
        names.add("Documento 4");
        names.add("Documento 5");
        names.add("Documento 6");
        names.add("Documento 7");
        names.add("Documento 8");
        names.add("Documento 9");
        names.add("Documento 10");
        registerForContextMenu(listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, names);
        listView.setAdapter(adapter);
        MyAdapter myAdapter = new MyAdapter(getActivity(), R.layout.list_item, names);
        listView.setAdapter(myAdapter);

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
                startActivity(intent);
            }
        });

        fabDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        DocumentoEdit.class);
                startActivity(intent);
            }
        });

        fabImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        ImagenEdit.class);
                startActivity(intent);
            }
        });

        fabCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogCategoria();
            }
        });

        return view;
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
                editItem();
                return true;
            case DELETE_ID:
                return true;
        }
        return super.onContextItemSelected(item);
    }

    protected void editItem() {
        Intent i = new Intent(getActivity(), DocumentoEdit.class);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    public void openDialogCategoria() {
        PopupCategoria popupCategoria = new PopupCategoria();
        popupCategoria.show(getFragmentManager(), "popup generar");
    }
}