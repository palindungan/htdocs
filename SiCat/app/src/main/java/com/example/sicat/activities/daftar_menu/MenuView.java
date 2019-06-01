package com.example.sicat.activities.daftar_menu;

import com.example.sicat.model.Categories;
import com.example.sicat.model.Meals;

import java.util.List;

public interface MenuView {
    /*
     * TODO 13 Create void method
     *
     * The interface of this method will behave bridging between presenters to activities
     * then activity Overriding the interface
     *
     * 1. behavior, when loading must appear || showLoading();
     * 2. Loading must be removed || hideLoading()
     * 3. Display Meal data (with List <Meal> meals) parameters || setMeals();
     * 4. Displays Category data (with List parameters <Category> category) setCategories();
     * 5. and, Behavior when an error occurs when requesting data to the API
     */
    // TODO 14 showLoading()

    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> meal);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}
