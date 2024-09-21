package connectfour

data class Player(var name: String = "", val symbol: Char, var score: Int = 0)
val playerOne = Player(symbol = 'o')
val playerTwo = Player(symbol = '*')
var player = playerTwo

val range = 5..9
const val defaultRows = 6
const val defaultColumns = 7
var board = listOf(mutableListOf<Char>())

var games = 1
var game = 1

fun main() {
    println("Connect Four\nFirst player's name:")
    playerOne.name = readln()
    println("Second player's name:")
    playerTwo.name = readln()
    while (true) {
        println("Set the board dimensions (Rows x Columns)\nPress Enter for default ($defaultRows x $defaultColumns)")
        val dimensions = readln().filterNot { it.isWhitespace() }.lowercase()
        if (dimensions.isEmpty()) { board = List(defaultRows) { MutableList(defaultColumns) { ' ' } }; break }
        if (!Regex("\\d+x\\d+").matches(dimensions)) { println("Invalid input"); continue }
        val (rows, columns) = dimensions.split("x").map { it.toInt() }
        if (rows !in range || columns !in range) {
            println("Board ${if (rows !in range) "rows" else "columns"} should be from ${range.first} to ${range.last}")
            continue
        }
        board = List(rows) { MutableList(columns) { ' ' } }
        break
    }
    var input = ""
    while (true) {
        println("Do you want to play single or multiple games?\nFor a single game, input 1 or press Enter\nInput a number of games:")
        input = readln()
        if (input.isEmpty() || (input.toIntOrNull() != null && input.toInt() > 0)) break
        println("Invalid input")
    }
    println("${playerOne.name} VS ${playerTwo.name}\n${board.size} X ${board.first().size} board")
    if (input.isEmpty() || input.toInt() == 1) { println("Single game") }
    else { games = input.toInt(); println("Total $games games") }
    while (game <= games) {
        if (games > 1) { println("Game #$game") }
        game@ while (true) {
            println(" ${board.first().indices.map { it + 1 }.joinToString(" ")}")
            board.forEach { row -> println("║${row.joinToString("║")}║") }
            println("╚${"═╩".repeat(board.first().lastIndex)}═╝")
            if (board.all { !it.contains(' ') }) {
                println("It is a draw")
                playerOne.score++
                playerTwo.score++
                break
            }
            for (i in board.first().indices) {
                if (board.joinToString("") { it[i].toString() }.contains(player.symbol.toString().repeat(4))) {
                    println("Player ${player.name} won")
                    player.score += 2
                    break@game
                }
            }
            if (board.any { it.joinToString("").contains(player.symbol.toString().repeat(4)) } ||
                diagonals(board).contains(player.symbol.toString().repeat(4)) ||
                diagonals(board.map { it.reversed().toMutableList() }).contains(player.symbol.toString().repeat(4))
            ) {
                println("Player ${player.name} won")
                player.score += 2
                break
            }
            player = if (player == playerOne) playerTwo else playerOne
            input@ while (true) {
                println("${player.name}'s turn:")
                val column = when (val input = readln()) {
                    "end" -> { println("Game over!"); return }
                    else -> when (val column = input.toIntOrNull()) {
                        null -> { println("Incorrect column number"); continue }
                        else -> when (column) {
                            !in 1..board[0].size -> { println("The column number is out of range (1 - ${board[0].size})"); continue }
                            else -> column - 1
                        }
                    }
                }
                for (row in board.lastIndex downTo 0) {
                    if (board[row][column] == ' ') { board[row][column] = player.symbol; break }
                    if (row == 0) { println("Column ${column + 1} is full"); continue@input }
                }
                break
            }
        }
        if (games > 1) {
            println("Score\n${playerOne.name}: ${playerOne.score} ${playerTwo.name}: ${playerTwo.score}")
            board = List(board.size) { MutableList(board.first().size) { ' ' } }
            game++
        }
    }
    println("Game over!")
}

fun diagonals(board: List<MutableList<Char>>): String {
    val diagonals = StringBuilder()
    for (i in 0..(board.first().size + board.size - 2)) {
        for (column in 0..i) {
            val row = i - column
            if (row < board.size && column < board.first().size) {
                diagonals.append(board[row][column])
            }
        }
        diagonals.append(" ")
    }
    return diagonals.toString()
}