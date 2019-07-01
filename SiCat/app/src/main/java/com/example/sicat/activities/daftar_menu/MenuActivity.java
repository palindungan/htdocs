package com.example.sicat.activities.daftar_menu;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sicat.R;
import com.example.sicat.Utils;
import com.example.sicat.activities.category.CategoryActivity;
import com.example.sicat.activities.detail.DetailActivity;
import com.example.sicat.adapter.RecyclerViewMenuAdapter;
import com.example.sicat.adapter.ViewPagerHeaderAdapter;
import com.example.sicat.model.Categories;
import com.example.sicat.model.Meals;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


// TODO 31 implement the MenuView interface at the end
public class MenuActivity extends AppCompatActivity implements MenuView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DETAIL = "detail";

    /*
     * TODO 32 Add @BindView Annotation (1)
     *
     * 1. viewPagerHeader
     * 2. recyclerCategory
     *
     */
    @BindView(R.id.viewPagerHeader) ViewPager viewPagerMeal;
    @BindView(R.id.recycleCategory) RecyclerView recyclerViewCategory;

    /*
     *  TODO 33 Create variable for presenter;
     */
    MenuPresenter presenter;

    private EditText searching;
    private Button btn_cari; // btn login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btn_cari = findViewById(R.id.btn_cari);
        searching = findViewById(R.id.searching);

        /*
         *  TODO 34 bind the ButterKnife (2)
         */
        ButterKnife.bind(this);

        /*
         *  TODO 35 Declare the presenter
         *  new HomePresenter(this)
         */
        presenter = new MenuPresenter(this);
        presenter.getMeals();
        presenter.getCategories();

        initActionBar();

        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mSearching = searching.getText().toString().trim();
                presenter.getSearching(mSearching);
            }
        });
    }

    private void initActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    // TODO 36 Overriding the interface

    @Override
    public void showLoading() {

        findViewById(R.id.shimmerMeal).setVisibility(View.VISIBLE);
        findViewById(R.id.shimmerCategory).setVisibility(View.VISIBLE);

    }

    @Override
    public void hideLoading() {
        findViewById(R.id.shimmerMeal).setVisibility(View.GONE);
        findViewById(R.id.shimmerCategory).setVisibility(View.GONE);
    }

    @Override
    public void setMeal(List<Meals.Meal> meal) {
        ViewPagerHeaderAdapter headerAdapter = new ViewPagerHeaderAdapter(meal,this);
        viewPagerMeal.setAdapter(headerAdapter);
        viewPagerMeal.setPadding(20,0,185,0);
        headerAdapter.notifyDataSetChanged();

        headerAdapter.setOnItemClickListener((view, position) -> {
            //TODO #8.1 make an intent to DetailActivity (get the name of the meal from the edit text view, then send the name of the meal to DetailActivity)
            TextView mealName = view.findViewById(R.id.mealName);
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            intent.putExtra(EXTRA_DETAIL, mealName.getText().toString());
            startActivity(intent);
        });
    }

    @Override
    public void setCategory(List<Categories.Category> category) {
        RecyclerViewMenuAdapter menuAdapter = new RecyclerViewMenuAdapter(category,this);
        recyclerViewCategory.setAdapter(menuAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(this,2, GridLayoutManager.VERTICAL,false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setNestedScrollingEnabled(true);
        menuAdapter.notifyDataSetChanged();

        menuAdapter.setOnItemClickListener((view, position) -> {
            Intent intent = new Intent(MenuActivity.this, CategoryActivity.class);

            // TODO 8. add extra data(put intent)
            intent.putExtra(EXTRA_CATEGORY,(Serializable) category);
            intent.putExtra(EXTRA_POSITION,position);
            startActivity(intent);
        });

    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(this,"Tittle", message);
    }

}
