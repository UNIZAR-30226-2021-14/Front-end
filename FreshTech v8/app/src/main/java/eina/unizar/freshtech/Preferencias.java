package eina.unizar.freshtech;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class Preferencias {

    public static void guardarToken(Context ctx, String correo, String token) {
        SharedPreferences preferences = ctx.getSharedPreferences("token", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Correo", correo);
        editor.putString("Token",token);
        editor.commit();
    }

    public static String cargarCorreo(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("token", Context.MODE_PRIVATE);
        return preferences.getString("Correo", "No existe correo");
    }

    public static String cargarToken(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("token", Context.MODE_PRIVATE);
        return preferences.getString("Token", "No existe token");
    }

    public static String cargarOrdenPor(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("ordenar", Context.MODE_PRIVATE);
        String ordenarPor = preferences.getString("OrdenarPor", "No existe la información");
        Log.d("Ordenar por: ", ordenarPor);
        if(ordenarPor.equals("Nombre")) ordenarPor = "nombre";
        else if(ordenarPor.equals("Categoría")) ordenarPor = "categoria";
        else if(ordenarPor.equals("Fecha de creación")) ordenarPor = "fechacreacion";
        else if(ordenarPor.equals("Fecha de expiración")) ordenarPor = "fechacaducidad";
        else Toast.makeText(ctx, "Error al ordenar por", Toast.LENGTH_SHORT).show();
        return ordenarPor;
    }

    public static String cargarOrdenDe(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences("ordenar", Context.MODE_PRIVATE);
        String ordenarDe = preferences.getString("OrdenarDe", "No existe la informacion");
        Log.d("Ordenar de: ", ordenarDe);
        if(ordenarDe.equals("Menor a mayor")) ordenarDe = "ASC";
        else if(ordenarDe.equals("Mayor a menor")) ordenarDe = "DESC";
        else Toast.makeText(ctx, "Error al ordenar de", Toast.LENGTH_SHORT).show();
        return ordenarDe;
    }
}
