package com.example.myshop.data

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class OffersViewModel : ViewModel() {
    private val _offers = mutableStateOf<List<Offer>>(listOf())
    val offers: State<List<Offer>> = _offers

    init {
        // Static offers for now
        _offers.value = listOf(
            Offer("50% Off Electronics", "Huge discounts on electronics"),
            Offer("Buy 1 Get 1 Free", "Limited time offer on selected items")
        )
    }
}

data class Offer(val title: String, val description: String)
