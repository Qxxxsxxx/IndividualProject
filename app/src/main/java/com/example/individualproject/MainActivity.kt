package com.example.individualproject
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.individualproject.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var group: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance().reference
        if (auth.currentUser == null) {
            sendUserToLogin()
        } else {
            verifyUserExistance()
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        group = findViewById(R.id.create_group)
        group.setOnClickListener {
            requestNewGroup()
        }
    }

    private fun sendUserToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun verifyUserExistance() {
        val currentUserID: String = auth.currentUser?.uid.toString()
        database.child("Users").child(currentUserID)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.child("name").exists()) {
                        Toast.makeText(baseContext, "Welcome", Toast.LENGTH_SHORT).show()
                    } else {
                        val username: HashMap<String, String> = HashMap()
                        username.put("name", currentUserID)
                        database.child("Users").child(currentUserID).setValue(username)

                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })

    }

    private fun requestNewGroup() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Enter group name")
        val groupName = EditText(this)
        builder.setView(groupName)

        builder.setPositiveButton("Create") { dialog, which ->

            val gName: String = groupName.text.toString()

            if (TextUtils.isEmpty(gName)) {
                Toast.makeText(this, "Please enter group name", Toast.LENGTH_SHORT).show()
            } else {
                createNewGroup(groupName.text.toString())
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()



    }

    private fun createNewGroup(name: String) {
        database.child("Groups").child(name).setValue("").addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
Toast.makeText(this, "$name has been created", Toast.LENGTH_SHORT).show()
            }
        }
    }
}