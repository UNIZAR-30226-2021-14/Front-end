package eina.unizar.freshtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private static final int ACTIVITY_EDIT=0;

    private static final int EDIT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private ListView listView;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        listView = (ListView) findViewById(R.id.listView);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, names);
        listView.setAdapter(adapter);
        MyAdapter myAdapter = new MyAdapter(this, R.layout.list_item, names);
        listView.setAdapter(myAdapter);
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

    @Override public void onBackPressed() { moveTaskToBack(true); }

    protected void editItem() {
        Intent i = new Intent(this, ItemEdit.class);
        startActivityForResult(i, ACTIVITY_EDIT);
    }

    public void onClickBotonFlotanteAñadir(View view) {
        Intent i = new Intent(this, ItemEdit.class);
        startActivityForResult(i, ACTIVITY_EDIT);
    }
}