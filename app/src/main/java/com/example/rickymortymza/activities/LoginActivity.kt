package com.example.rickymortymza.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rickymortymza.R

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)

        //comprobar si el user inicion sesion
        val isLoggedIn = sharedPreferences.getBoolean("isLoggenIn", false)
        if (isLoggedIn) {
            navigateToMainActivity()
            return
        }

        val usernameEditText: EditText = findViewById(R.id.editTextUsername)
        val loginButton: Button = findViewById(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
                if (username.isNotEmpty()) {
                    saveLoginState(true)
                    Toast.makeText(this, "Bienvenido, $username!", Toast.LENGTH_SHORT)
                    navigateToMainActivity()
                } else {
                    Toast.makeText(this, "por favor,  ingrese un nombre de usuario.", Toast.LENGTH_SHORT).show()

                }
            }
        }


        private fun saveLoginState(isLoggedIn : Boolean) {
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", isLoggedIn)
            editor.apply()
        }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}