package com.example.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var emailEditText:  EditText? = null
    var passwordEditText:  EditText? = null

    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if(auth.currentUser != null){
            login()

        }
    }

    fun goClicked(view: View){
        //check if we can login the user
        auth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener( this) { task ->
                if (task.isSuccessful) {
                    login()
                } else {
                    //if not Sign up
                    auth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful){
                    //add to database
                                FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user!!.uid).child("email").setValue(emailEditText?.text.toString())
                                login()
                }else{
                    Toast.makeText(this,"Login failed. Try again",Toast.LENGTH_SHORT).show()
                                Log.i("Info", task.exception.toString())
                            }
            }
                }
            }

    }

    fun login(){
        //Move to next activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }

}