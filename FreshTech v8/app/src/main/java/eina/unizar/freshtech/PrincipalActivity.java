package eina.unizar.freshtech;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity implements PopupOrdenar.onListaOrdenada {

    private static final int ACTIVITY_LOGIN=0;

    private static final int EDIT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //Menu desplegable
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        // Modificar el correo de la cabecera
        View headerView = navigationView.getHeaderView(0);
        TextView correoCabecera = (TextView) headerView.findViewById(R.id.correoCabecera);
        correoCabecera.setText(Preferencias.cargarCorreo(this));
        navigationView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_contraseñas, R.id.nav_documentos, R.id.nav_imagenes, R.id.nav_categorias, R.id.popupContacto, R.id.configuracionFragment, R.id.loginActivity)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        // Popup elementos caducados
        PopupCaducados popupCaducados = new PopupCaducados();
        popupCaducados.show(getSupportFragmentManager(), "popup caducados");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(Gravity.LEFT);
        } else {
            android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(PrincipalActivity.this);
            alerta.setMessage(Html.fromHtml("<font color='#FFFFFF'>¿Desea salir de la aplicación?</font>"))
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            moveTaskToBack(true);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);
            titulo.setTitle(Html.fromHtml("<font color='#008577'>AVISO</font>"));
            titulo.show();
        }
    }

    public void onClickOrdenar(MenuItem item) {
        PopupOrdenar popupOrdenar = new PopupOrdenar();
        popupOrdenar.show(getSupportFragmentManager(), "popup ordenar");
    }

    public void onClickCaducados(MenuItem item) {
        PopupCaducados popupCaducados = new PopupCaducados();
        popupCaducados.show(getSupportFragmentManager(), "popup caducados");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void ordenarListado() {
        Fragment actualFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getPrimaryNavigationFragment();
        if(actualFragment instanceof HomeFragment) {
            HomeFragment fragmentoActual = (HomeFragment) actualFragment;
            fragmentoActual.ordenarListado();
        } else if(actualFragment instanceof ContraseñasFragment) {
            ContraseñasFragment fragmentoActual = (ContraseñasFragment) actualFragment;
            fragmentoActual.ordenarListado();
        } else if(actualFragment instanceof ImagenesFragment) {
            ImagenesFragment fragmentoActual = (ImagenesFragment) actualFragment;
            fragmentoActual.ordenarListado();
        } else if(actualFragment instanceof DocumentosFragment) {
            DocumentosFragment fragmentoActual = (DocumentosFragment) actualFragment;
            fragmentoActual.ordenarListado();
        }
    }
}