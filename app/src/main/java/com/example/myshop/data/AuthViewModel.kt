package com.example.myshop.data

import androidx.lifecycle.ViewModel
import com.example.myshop.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AuthViewModel : ViewModel() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _currentUser = MutableStateFlow<UserModel?>(null)
    val currentUser: StateFlow<UserModel?> = _currentUser

    private val _userRole = MutableStateFlow("user")
    val userRole: StateFlow<String> = _userRole

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val user = mAuth.currentUser
        if (user != null) {
            fetchUserFromDatabase(user.uid)
        }
    }

    private fun fetchUserFromDatabase(userId: String) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        userRef.get().addOnSuccessListener { snapshot ->
            val user = snapshot.getValue(UserModel::class.java)
            _currentUser.value = user
            _userRole.value = user?.role ?: "user"
        }.addOnFailureListener {
            _errorMessage.value = "Failed to load user data"
        }
    }

    fun signup(
        firstname: String,
        lastname: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (firstname.isBlank() || lastname.isBlank() || email.isBlank() || password.isBlank()) {
            onResult(false, "Please fill all the fields")
            return
        }

        _isLoading.value = true
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid.orEmpty()
                    val user = UserModel(
                        firstname = firstname,
                        lastname = lastname,
                        email = email,
                        password = password,
                        userId = userId,
                        role = "user"
                    )
                    saveUserToDatabase(userId, user, onResult)
                } else {
                    _errorMessage.value = task.exception?.message
                    onResult(false, "Registration failed")
                }
            }
    }

    private fun saveUserToDatabase(
        userId: String,
        user: UserModel,
        onResult: (Boolean, String?) -> Unit
    ) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        userRef.setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _currentUser.value = user
                onResult(true, null)
            } else {
                _errorMessage.value = task.exception?.message
                onResult(false, "Database error")
            }
        }
    }

    fun login(email: String, password: String, onLoginResult: (Boolean, String?) -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            onLoginResult(false, "Email and password required")
            return
        }

        _isLoading.value = true
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid.orEmpty()
                    fetchUserRole(userId, onLoginResult)
                } else {
                    _errorMessage.value = task.exception?.message
                    onLoginResult(false, "Login failed")
                }
            }
    }

    // This function fetches the user role after login
    private fun fetchUserRole(userId: String, onResult: (Boolean, String?) -> Unit) {
        val roleRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("role")
        roleRef.get().addOnSuccessListener { snapshot ->
            val role = snapshot.getValue(String::class.java) ?: "user"
            _userRole.value = role
            onResult(true, role)
        }.addOnFailureListener {
            onResult(false, "Failed to fetch role")
        }
    }

    fun updateUserRoleToAdmin() {
        val userId = getCurrentUserId()
        if (userId.isNotEmpty()) {
            val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
            userRef.child("role").setValue("admin").addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userRole.value = "admin"
                    _currentUser.value = _currentUser.value?.copy(role = "admin")
                } else {
                    _errorMessage.value = "Failed to update role to admin"
                }
            }
        }
    }

    fun logout() {
        mAuth.signOut()
        _currentUser.value = null
    }

    fun isUserLoggedIn(): Boolean = mAuth.currentUser != null

    fun getCurrentUserId(): String {
        return mAuth.currentUser?.uid.orEmpty()
    }

    fun getCurrentUserEmail(): String {
        return mAuth.currentUser?.email.orEmpty()
    }
}
