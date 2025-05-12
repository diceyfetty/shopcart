package com.example.myshop.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CategoryViewModel : ViewModel() {
    private val _categories = mutableStateOf<List<Category>>(listOf())
    val categories: State<List<Category>> = _categories

    init {
        // Static categories for now
        _categories.value = listOf(
            Category("Electronics"),
            Category("Clothing"),
            Category("Home Appliances")
        )
    }
}

data class Category(val name: String)
