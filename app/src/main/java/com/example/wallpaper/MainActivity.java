package com.example.wallpaper;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper.adapter.HitsAdapter;
import com.example.wallpaper.client.RetrofitAPI;
import com.example.wallpaper.client.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    HitsAdapter hitsAdapter;
    RetrofitAPI retrofitAPI;
    RecyclerView recyclerView;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    DrawerLayout drawer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Retrofit retrofit = RetrofitClient.getInstance();
        retrofitAPI = retrofit.create(RetrofitAPI.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        fetchData();
    }

    private void fetchData() {
        compositeDisposable.add(retrofitAPI.getHits("13946879-b442cfdf09d6196f64128c146")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hitsList -> {
                    hitsAdapter = new HitsAdapter(this, hitsList);
                    recyclerView.setAdapter(hitsAdapter);
                    hitsAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Log.e("TAG", "Error is: " + throwable.getMessage());
                    Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
                }));
    }

    private void fetchCategory(String category) {
        compositeDisposable.add(retrofitAPI.getCategoryHits("13946879-b442cfdf09d6196f64128c146", category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hitsList -> {
                    hitsAdapter = new HitsAdapter(this, hitsList);
                    recyclerView.setAdapter(hitsAdapter);
                    hitsAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Log.e("TAG", "Error is: " + throwable.getMessage());
                    Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
                }));
    }

    private void fetchSearch(String search) {
        compositeDisposable.add(retrofitAPI.getSearchHits("13946879-b442cfdf09d6196f64128c146", search)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(hitsList -> {
                    hitsAdapter = new HitsAdapter(this, hitsList);
                    recyclerView.setAdapter(hitsAdapter);
                    hitsAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Log.e("TAG", "Error is: " + throwable.getMessage());
                    Toast.makeText(this, "Проверьте подключение к интернету", Toast.LENGTH_SHORT).show();
                }));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_business:
                fetchCategory("business");
                closeDrawer();
                break;
            case R.id.nav_education:
                fetchCategory("education");
                closeDrawer();
                break;
            case R.id.nav_nature:
                fetchCategory("nature");
                closeDrawer();
                break;
            case R.id.nav_people:
                fetchCategory("people");
                closeDrawer();
                break;
            case R.id.nav_science:
                fetchCategory("science");
                closeDrawer();
                break;
            case R.id.action_search:
                SearchView searchView = (SearchView) item.getActionView();
                searchView.setOnQueryTextListener(this);
                closeDrawer();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void closeDrawer() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        fetchSearch(query);
        closeDrawer();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}
