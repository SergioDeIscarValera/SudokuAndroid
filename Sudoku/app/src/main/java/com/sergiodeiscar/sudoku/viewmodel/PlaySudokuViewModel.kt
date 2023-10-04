package com.sergiodeiscar.sudoku.viewmodel

import androidx.lifecycle.ViewModel
import com.sergiodeiscar.sudoku.game.SudokuGame

class PlaySudokuViewModel: ViewModel() {
    val sudokuGame = SudokuGame()
}