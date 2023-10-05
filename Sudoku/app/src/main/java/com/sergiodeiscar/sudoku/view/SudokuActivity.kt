package com.sergiodeiscar.sudoku.view

import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.sergiodeiscar.sudoku.R
import com.sergiodeiscar.sudoku.constanst.SIZE_EXTRA
import com.sergiodeiscar.sudoku.databinding.ActivitySudokuBinding
import com.sergiodeiscar.sudoku.game.Cell
import com.sergiodeiscar.sudoku.view.custom.SudokuBoardView
import com.sergiodeiscar.sudoku.viewmodel.PlaySudokuViewModel


class SudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel : PlaySudokuViewModel
    private lateinit var sudokuBoardView: SudokuBoardView

    private lateinit var numberButtons: List<Button>

    private var size: Int = 9

    private lateinit var binding: ActivitySudokuBinding
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySudokuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        size = intent.getIntExtra(SIZE_EXTRA, 9)

        numberButtons = listOf(binding.oneButton, binding.twoButton, binding.threeButton, binding.fourButton, binding.fiveButton, binding.sixButton, binding.sevenButton, binding.eightButton, binding.nineButton)

        sudokuBoardView = findViewById(R.id.sudokuBoardView)
        sudokuBoardView.setSize(size)

        sudokuBoardView.registerListener(this)

        viewModel = androidx.lifecycle.ViewModelProvider(this)[PlaySudokuViewModel::class.java]

        viewModel.sudokuGame.setSize(size)

        viewModel.sudokuGame.selectedCellLiveData.observe(this) { updateSelectedCellUI(it) }
        viewModel.sudokuGame.cellsLiveData.observe(this) { updateCells(it) }
        viewModel.sudokuGame.isTakingNotesLiveData.observe(this) { updateNoteTakingUI(it) }
        viewModel.sudokuGame.highlightedKeysLiveData.observe(this) { updateHighlightedKeys(it) }

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
            }
        }

        binding.notesButton.setOnClickListener{ viewModel.sudokuGame.changeNoteTakingState() }
        binding.deleteButton.setOnClickListener{ viewModel.sudokuGame.delete() }
        binding.buttonValidate.setOnClickListener { validate() }
        binding.buttonReroll.setOnClickListener { viewModel.sudokuGame.reroll() }
        binding.buttonReset.setOnClickListener { viewModel.sudokuGame.reset() }
    }

    private fun validate() {
        val result = if(viewModel.sudokuGame.checkBoard()) "¡Has ganado!" else "No es correcto, sigue intentándolo"
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
    }

    private fun updateHighlightedKeys(set: Set<Int>?) {
        numberButtons.forEachIndexed { index, button ->
            button.setBackgroundColor(
                if (set?.contains(index + 1) == true)
                    getColor(R.color.success)
                else
                    getColor(R.color.info)
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun updateNoteTakingUI(isNotTaking: Boolean?) = isNotTaking?.let {
        val color = if (it) getColor(R.color.secondary) else getColor(R.color.info)
        binding.notesButton.colorFilter = BlendModeColorFilter(color, BlendMode.MULTIPLY)
    }


    private fun updateCells(cells: List<Cell>?) = cells?.let {
        sudokuBoardView.updateCells(cells)
    }

    private fun updateSelectedCellUI(cell: Pair<Int, Int>?) = cell?.let {
        sudokuBoardView.updateSelectedCellUI(cell.first, cell.second)
    }

    override fun onCellTouched(row: Int, col: Int) {
        viewModel.sudokuGame.updateSelectedCell(row, col)
    }
}