package com.example.individualproject

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.DocumentsContract.Root
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.individualproject.models.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.toObject

class LoginActivity : AppCompatActivity() {
    // [START declare_auth]
    private lateinit var auth: FirebaseAuth

    // [END declare_auth]
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var loginB: Button
    private lateinit var progressBar: ProgressBar
    private var um: UserModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        email = findViewById(R.id.email)
        loginB = findViewById(R.id.loginButton)
        password = findViewById(R.id.password)
        progressBar = findViewById(R.id.progress)
        // [START initialize_auth]
        // Initialize Firebase Auth
        auth = Firebase.auth
        // [END initialize_auth]
//        getUsername()
        loginB.setOnClickListener {
            signIn(email.text.toString(), password.text.toString())
//            setUsername(email.text.toString(),password.text.toString())
        }
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        // [END sign_in_with_email]
    }

    private fun getUsername() {
        utils.currentUserDetails().get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                um = task.result.toObject(UserModel::class.java)
                if (um != null) {
                    email.setText(um!!.username)
                }
            }
        }
    }

    private fun setUsername(email: String, password: String) {
        val username: String = email

        if (um != null) {
            um!!.username = username
        } else {
            um = UserModel(username, password, Timestamp.now())
        }
        utils.currentUserDetails().set(um!!).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }
}