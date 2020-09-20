package com.sbs.jhs.be.and;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menuItemGoToNoticeList) {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("boardId", 1);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuItemGoToFreeList) {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra("boardId", 2);
            startActivity(intent);
        } else if (item.getItemId() == R.id.menuItemGoToLogin) {
            startActivity(new Intent(this, LoginActivity.class));
        } else if (item.getItemId() == R.id.menuItemGoToJoin) {
            startActivity(new Intent(this, JoinActivity.class));
        } else if (item.getItemId() == R.id.menuItemDoLogout) {
            App.logout();
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
