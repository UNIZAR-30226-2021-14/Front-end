package eina.unizar.freshtech.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import eina.unizar.freshtech.ItemEdit;
import eina.unizar.freshtech.MyAdapter;
import eina.unizar.freshtech.R;

public class HomeFragment extends Fragment {

    private static final int ACTIVITY_EDIT=0;

    private static final int EDIT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private ListView listView;
    private ArrayList<String> names;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //Lista de items
        listView = (ListView) view.findViewById(R.id.listView);
        names = new ArrayList<>();
        names.add("Usuario 1");
        names.add("Usuario 2");
        names.add("Usuario 3");
        names.add("Usuario 4");
        names.add("Usuario 5");
        names.add("Usuario 6");
        names.add("Usuario 7");
        names.add("Usuario 8");
        names.add("Usuario 9");
        names.add("Usuario 10");
        registerForContextMenu(listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, names);
        listView.setAdapter(adapter);
        MyAdapter myAdapter = new MyAdapter(getActivity(), R.layout.list_item, names);
        listView.setAdapter(myAdapter);

        //Boton flotante
        FloatingActionButton botonFlotanteAñadir = (FloatingActionButton) view.findViewById(R.id.botonFlotanteAñadir);
        botonFlotanteAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getBaseContext(),
                        ItemEdit.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, R.string.menu_delete_product);
        menu.add(Menu.NONE, EDIT_ID, Menu.NONE, R.string.menu_edit_product);
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
        Intent i = new Intent(getActivity(), ItemEdit.class);
        startActivityForResult(i, ACTIVITY_EDIT);
    }
}