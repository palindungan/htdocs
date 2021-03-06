package com.example.sicat.activities.category;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sicat.R;
import com.example.sicat.Utils;
import com.example.sicat.activities.detail.DetailActivity;
import com.example.sicat.adapter.RecyclerViewMealByCategory;
import com.example.sicat.model.Meals;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.sicat.activities.daftar_menu.MenuActivity.EXTRA_DETAIL;


public class CategoryFragment extends Fragment implements CategoryView {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.imageCategory)
    ImageView imageCategory;
    @BindView(R.id.imageCategoryBg)
    ImageView imageCategoryBg;
    @BindView(R.id.textCategory)
    TextView textCategory;

    AlertDialog.Builder descDialog;

    private EditText searching;
    private Button btn_cari; // btn login

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);

        btn_cari = view.findViewById(R.id.btn_cari);
        searching = view.findViewById(R.id.searching);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO 12. getArguments with KEY
        //EXTRA_DATA_NAME

        //TODO 13. set Value from argument data to view
        if(getArguments()!=null){
            textCategory.setText(getArguments().getString("EXTRA_DATA_DESC"));
            Picasso.get().load(getArguments().getString("EXTRA_DATA_IMAGE")).into(imageCategory);
            Picasso.get().load(getArguments().getString("EXTRA_DATA_IMAGE")).into(imageCategoryBg);
            descDialog = new AlertDialog.Builder(getActivity()).setTitle(getArguments().getString("EXTRA_DATA_NAME")).setMessage(getArguments().getString("EXTRA_DATA_DESC"));
        }

        // declare presenter
        CategoryPresenter presenter = new CategoryPresenter(this);
        presenter.getMealByCategory(getArguments().getString("EXTRA_DATA_NAME"));

        btn_cari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mSearching = searching.getText().toString().trim();
                presenter.getSearching(mSearching,getArguments().getString("EXTRA_DATA_NAME"));
            }
        });
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setMeals(final List<Meals.Meal> meals) {
        //TODO 16. set RecyclerViewMealByCategory adapter;
        RecyclerViewMealByCategory adapter = new RecyclerViewMealByCategory(getActivity(),meals);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnItemClickListener((view, position) -> {
            //TODO #8.2 make an intent to DetailActivity (get the name of the meal from the edit text view, then send the name of the meal to DetailActivity)
            TextView mealName = view.findViewById(R.id.mealName);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(EXTRA_DETAIL, mealName.getText().toString());
            startActivity(intent);
        });

//        adapter.setOnItemClickListener(new RecyclerViewMealByCategory.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//                Toast.makeText(getActivity(),meals.get(position).getNmMenu() , Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public void onErrorLoading(String message) {
        Utils.showDialogMessage(getActivity(), "Error ", message);
    }

    @OnClick(R.id.cardCategory)
    public void onClick(){
        descDialog.setPositiveButton("CLOSE", (dialog, which) -> dialog.dismiss());

//        descDialog.setPositiveButton("CLOSE", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });

        descDialog.show();
    }

}
