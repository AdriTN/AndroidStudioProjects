package com.example.firebaserealtime

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.firebaserealtime.ui.theme.FirebaseRealtimeTheme

import com.google.firebase.database.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializa Firebase Database
        val database = FirebaseDatabase.getInstance()
        val questionsRef = database.getReference("questions")

        // Escuchar los cambios en la base de datos
        questionsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Inicializar una lista de preguntas
                val questionsList = mutableListOf<String>()

                // Recorre los datos recibidos
                for (questionSnapshot in snapshot.children) {
                    val questionText = questionSnapshot.child("question").getValue(String::class.java)
                    if (questionText != null) {
                        questionsList.add(questionText)
                    }
                }

                // Si la lista de preguntas no está vacía, muestra la primera pregunta
                if (questionsList.isNotEmpty()) {
                    val questionTextView = findViewById<TextView>(R.id.questionTextView)
                    questionTextView.text = questionsList[0] // Mostrar la primera pregunta
                } else {
                    Log.e("Firebase", "No se encontraron preguntas en la base de datos")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar errores
                Log.e("FirebaseError", "Error al leer los datos", error.toException())
            }
        })
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FirebaseRealtimeTheme {
        Greeting("Android")
    }
}