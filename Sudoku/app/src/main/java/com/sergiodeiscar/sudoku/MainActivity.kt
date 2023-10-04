package com.sergiodeiscar.sudoku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sergiodeiscar.sudoku.databinding.ActivityMainBinding
import com.sergiodeiscar.sudoku.view.SudokuActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            /*val fila = binding.editTextNumber.text.toString().toInt()
            val columna = binding.editTextNumber2.text.toString().toInt()*/
            val intent = Intent(this, SudokuActivity::class.java)/*.apply {
                putExtra(FILA_EXTRA, fila)
                putExtra(COLUMNA_EXTRA, columna)
            }*/
            startActivity(intent)
        }
    }
}