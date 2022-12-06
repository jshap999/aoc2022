#!/usr/bin/env kotlin

import java.io.File

data class Move(val count: Int, val fromIndex: Int, val toIndex: Int)

if (args.size != 1) {
    println("Please provide the file containing the puzzle input.")
} else {    
    runCatching {

        val puzzleInput = readFile(args.first())
            .splitInput()

        val boardInput = puzzleInput.first
            .normalizeSpaces()
            .splitRowsIntoColumns()

        val board = setupBoard(boardInput)

        val moves = puzzleInput.second
            .toMoves()

        moves.forEach { move ->
            val movingValues = (0 until move.count).map { board[move.fromIndex].removeFirst() }
            board[move.toIndex].addAll(0,movingValues)
        }

        val message = board.map { it.first()}.joinToString("")
        println(message)

    }.getOrElse {
        println("Something went wrong.")
        println("${it.stackTraceToString()}")
    }
}

fun readFile(fileName: String): List<String> {
    val file = File(fileName)

    return file.readLines()
        .map { if (it.isNullOrBlank()) null else it }
        .filterNotNull()
}

fun List<String>.splitInput(): Pair<List<String>, List<String>> {
    return this.fold(Pair(listOf(), listOf())) { end, item ->
        when {
            "^[ 0-9]+$".toRegex().matches(item) -> end
            "^move .*".toRegex().matches(item) -> Pair(end.first, end.second.plus(item))
            else -> Pair(end.first.plus(item), end.second)
        }
    }
}

fun List<String>.normalizeSpaces(): List<String> {
    return this.map {
        it.replace("    ".toRegex(), " [_] ")
            .trim()
            .replace("  +".toRegex(), " ")
            .replace("\\[|\\]".toRegex(), "")
    }
}

fun List<String>.splitRowsIntoColumns(): List<List<String>> {
    return this.map {
        it.split(" ")
    }
}

fun setupBoard(input: List<List<String>>): List<MutableList<String>> {
    val longestRow = input.fold(0) { longest, row ->
        if (row.size > longest) row.size else longest
    }

    val board = List<MutableList<String>>(longestRow) { mutableListOf() }

    input.forEach { row ->
        row.forEachIndexed { col, item ->
            if (item != "_") board[col].add(item)
        }
    }

    return board
}

fun List<String>.toMoves(): List<Move> {
    val regex = "move ([0-9]+) from ([0-9]+) to ([0-9]+)".toRegex()
    return this.map {
        val (_, count, from, to) = regex.matchEntire(it)?.groupValues ?: listOf(0, 0, 0, 0)
        Move("$count".toInt(), "$from".toInt() - 1, "$to".toInt() - 1)
    }
}
