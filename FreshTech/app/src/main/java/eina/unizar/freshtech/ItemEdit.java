package eina.unizar.freshtech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ItemEdit extends AppCompatActivity {

    private static final int ACTIVITY_PRINCIPAL=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_edit);
    }

    public void onClickGuardar(View view) {
        Intent i = new Intent(this, PrincipalActivity.class);
        startActivityForResult(i, ACTIVITY_PRINCIPAL);
        finish();
    }
}