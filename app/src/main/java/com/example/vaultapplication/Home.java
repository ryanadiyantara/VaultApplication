package com.example.vaultapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;

import adapter.ItemAdapter;
import db.DbHelper;
import model.Item;

public class Home extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private ArrayList<Item> orderArrayList;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new ItemAdapter(this);

        dbHelper = new DbHelper(this);
        orderArrayList = dbHelper.readItem();
        adapter.setListItem(orderArrayList);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Home.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderArrayList = dbHelper.readItem();
        adapter.setListItem(orderArrayList);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionmenu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.createnewitem){
            startActivity(new Intent(this, Create.class));
        } else if (item.getItemId() == R.id.logout) {
            startActivity(new Intent(this, MainActivity.class));
        }
        return true;
    }
}