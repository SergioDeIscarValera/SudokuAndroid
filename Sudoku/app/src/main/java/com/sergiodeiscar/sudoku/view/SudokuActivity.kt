package com.sergiodeiscar.sudoku.view

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import com.sergiodeiscar.sudoku.R
import com.sergiodeiscar.sudoku.databinding.ActivitySudokuBinding
import com.sergiodeiscar.sudoku.game.Cell
import com.sergiodeiscar.sudoku.view.custom.SudokuBoardView
import com.sergiodeiscar.sudoku.viewmodel.PlaySudokuViewModel


class SudokuActivity : AppCompatActivity(), SudokuBoardView.OnTouchListener {

    private lateinit var viewModel : PlaySudokuViewModel
    private lateinit var sudokuBoardView: SudokuBoardView

    private lateinit var numberButtons: List<Button>

    private lateinit var binding: ActivitySudokuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySudokuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        numberButtons = listOf(binding.oneButton, binding.twoButton, binding.threeButton, binding.fourButton, binding.fiveButton, binding.sixButton, binding.sevenButton, binding.eightButton, binding.nineButton)

        sudokuBoardView = findViewById(R.id.sudokuBoardView)

        sudokuBoardView.registerListener(this)

        viewModel = androidx.lifecycle.ViewModelProvider(this)[PlaySudokuViewModel::class.java]
        viewModel.sudokuGame.selectedCellLiveData.observe(this, Observer { updateSelectedCellUI(it) })
        viewModel.sudokuGame.cellsLiveData.observe(this, Observer { updateCells(it) })
        viewModel.sudokuGame.isTakingNotesLiveData.observe(this, Observer { updateNoteTakingUI(it) })
        viewModel.sudokuGame.highlightedKeysLiveData.observe(this, Observer { updateHighlightedKeys(it) })

        numberButtons.forEachIndexed { index, button ->
            button.setOnClickListener {
                viewModel.sudokuGame.handleInput(index + 1)
            }
        }

        binding.notesButton.setOnClickListener{ viewModel.sudokuGame.changeNoteTakingState() }
        binding.deleteButton.setOnClickListener{ viewModel.sudokuGame.delete() }
    }

    private fun updateHighlightedKeys(set: Set<Int>?) {
        numberButtons.forEachIndexed { index, button ->
            button.setBackgroundColor(
                if (set?.contains(index + 1) == true) {
                    getColor(R.color.secondary)
                }
                else {
                    getColor(R.color.info)
                }
            )
        }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun updateNoteTakingUI(isNotTaking: Boolean?) = isNotTaking?.let {
        val color = if (it) getColor(R.color.primary) else getColor(R.color.light)
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