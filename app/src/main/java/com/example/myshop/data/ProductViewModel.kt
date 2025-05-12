package com.example.myshop.data

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.findables.models.ProductModel
import com.example.myshop.network.RetrofitInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.UUID

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val dbRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")
    private val storageRef: FirebaseStorage = FirebaseStorage.getInstance()
    private val contentResolver = application.contentResolver

    private val _productList = MutableStateFlow<List<ProductModel>>(emptyList())
    val productList: StateFlow<List<ProductModel>> = _productList

    private val _todaysDeals = MutableStateFlow<List<ProductModel>>(emptyList())
    val todaysDeals: StateFlow<List<ProductModel>> = _todaysDeals

    private val _thumbsUpItems = MutableStateFlow<List<ProductModel>>(emptyList())
    val thumbsUpItems: StateFlow<List<ProductModel>> = _thumbsUpItems

    private val _selectedProduct = MutableStateFlow<ProductModel?>(null)
    val selectedProduct: StateFlow<ProductModel?> = _selectedProduct

    init {
        fetchProducts()
    }

    /**
     * Fetch all products from Firebase and classify them into categories.
     */
    private fun fetchProducts() {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = mutableListOf<ProductModel>()
                val todaysDealsList = mutableListOf<ProductModel>()
                val thumbsUpList = mutableListOf<ProductModel>()

                for (productSnap in snapshot.children) {
                    val product = productSnap.getValue(ProductModel::class.java)
                    product?.productId = productSnap.key.orEmpty()
                    product?.let {
                        productList.add(it)
                        when (it.category) {
                            "Today's Deal" -> todaysDealsList.add(it)
                            "Thumbs Up" -> thumbsUpList.add(it)
                            else -> {
                                Log.d("ProductViewModel", "Product with other category: ${it.category}")
                            }
                        }
                    }
                }

                // Updating UI state with categorized products
                _productList.value = productList
                _todaysDeals.value = todaysDealsList
                _thumbsUpItems.value = thumbsUpList
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProductViewModel", "Firebase Error: ${error.message}")
            }
        })
    }

    /**
     * Add a new product, possibly with an image.
     */
    fun addProduct(product: ProductModel, imageUri: Uri? = null, onComplete: (Boolean) -> Unit) {
        val productId = dbRef.push().key.orEmpty()
        product.productId = productId

        if (imageUri != null) {
            uploadImageToImgur(imageUri) { imageUrl ->
                product.imageUrl = imageUrl
                saveProductToDatabase(product, onComplete)
            }
        } else {
            saveProductToDatabase(product, onComplete)
        }
    }

    /**
     * Save the product to the Firebase database.
     */
    private fun saveProductToDatabase(product: ProductModel, onComplete: (Boolean) -> Unit) {
        dbRef.child(product.productId).setValue(product)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("ProductViewModel", "Product added successfully: ${product.productId}")
                    onComplete(true)
                } else {
                    Log.e("ProductViewModel", "Error adding product: ${task.exception}")
                    onComplete(false)
                }
            }
    }

    /**
     * Upload product image to Imgur (or any other service).
     */
    private fun uploadImageToImgur(imageUri: Uri, onComplete: (String) -> Unit) {
        val imageFileBytes = contentResolver.openInputStream(imageUri)?.readBytes()
        if (imageFileBytes == null) {
            Log.e("ProductViewModel", "Failed to read image bytes.")
            onComplete("")
            return
        }

        val requestBody = imageFileBytes.toRequestBody("image/jpeg".toMediaType())
        val imagePart = MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
        val authHeader = "Client-ID YOUR_CLIENT_ID"

        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.uploadImage(imagePart, authHeader)
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.data?.link.orEmpty()
                    Log.d("ProductViewModel", "Image uploaded to Imgur: $imageUrl")
                    onComplete(imageUrl)
                } else {
                    Log.e("ProductViewModel", "Imgur Upload Failed: ${response.errorBody()}")
                    onComplete("")
                }
            } catch (e: Exception) {
                Log.e("ProductViewModel", "Error uploading image to Imgur: ${e.message}")
                onComplete("")
            }
        }
    }

    /**
     * Fetch a product by its ID.
     */
    fun getProductById(productId: String) {
        dbRef.child(productId).get().addOnSuccessListener { snapshot ->
            val product = snapshot.getValue(ProductModel::class.java)
            _selectedProduct.value = product ?: run {
                Log.e("ProductViewModel", "Product not found for ID: $productId")
                null
            }
        }.addOnFailureListener { exception ->
            Log.e("ProductViewModel", "Error fetching product by ID: ${exception.message}")
        }
    }

    /**
     * Update an existing product.
     */
    fun updateProduct(product: ProductModel, onResult: (Boolean) -> Unit) {
        dbRef.child(product.productId).setValue(product)
            .addOnSuccessListener {
                Log.d("ProductViewModel", "Product updated successfully: ${product.productId}")
                onResult(true)
            }
            .addOnFailureListener { exception ->
                Log.e("ProductViewModel", "Error updating product: ${exception.message}")
                onResult(false)
            }
    }

    /**
     * Delete a product by its ID.
     */
    fun deleteProduct(productId: String, onResult: (Boolean) -> Unit) {
        dbRef.child(productId).removeValue()
            .addOnSuccessListener {
                Log.d("ProductViewModel", "Product deleted: $productId")
                onResult(true)
            }
            .addOnFailureListener { e ->
                Log.e("ProductViewModel", "Error deleting product: ${e.message}")
                onResult(false)
            }
    }
    fun uploadImageToFirebase(imageUri: Uri, onResult: (String) -> Unit) {
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("product_images/${UUID.randomUUID()}.jpg")

        imageRef.putFile(imageUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(task.result.toString())
                } else {
                    onResult("") // Failed to upload
                }
            }
    }
    }
