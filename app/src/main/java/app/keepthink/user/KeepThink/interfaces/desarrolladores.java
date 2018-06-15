package app.keepthink.user.KeepThink.interfaces;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.wer.KeepThink.R;

public class desarrolladores extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desarrolladores);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public void llamar_luis(View view){
        Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100025735510889/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void llamar_werner(View view){
        Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100001137381776/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void llamar_gerber(View view){
        Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100001269786211/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void llamar_willy(View view){
        Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100001312398888/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void llamar_leo(View view){
        Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100003854704216/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void llamar_ing(View view){
        Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100001260453851/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
    public void llamar_yef(View view){
        Uri uri = Uri.parse("https://www.facebook.com/profile.php?id=100008390237710/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
