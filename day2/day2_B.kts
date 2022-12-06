#!/usr/bin/env kotlin

import java.io.File

if (args.size != 1) {
    println("Please provide the file containing the puzzle input.")
} else {
    runCatching {

        val file = File(args.first())

        val validKeys = listOf("A", "B", "C")
        val validVals = listOf("X", "Y", "Z")

        val rounds = file.readLines().map { if (it.isNullOrBlank()) null else it.uppercase().split(" ") }
            .filterNotNull()
            .filter { it.size == 2 }
            .map { it.zipWithNext() }
            .flatten()
            .filter { it.first in validKeys }
            .filter { it.second in validVals }

        rounds.map(::chooseMove)
            .map(::score)
            .reduce() { tot, it -> Pair(tot.first + it.first, tot.second + it.second) }
            .run {
                val report = "You scored $second and they scored $first.\n"
                when {
                    second > first -> report + "The strategy succeeded!"
                    else -> report + "The strategy failed!"
                }
            }
            .also(::println)

    }.getOrElse {
        println("Something went wrong.")
        println("${it.stackTraceToString()}")
    }
}

fun moveToInt(move: String): Int {
    return when (move) {
        "A", "X" -> 1
        "B", "Y" -> 2
        "C", "Z" -> 3
        else -> 0
    }
}

infix fun Int.beats(that: Int): Boolean {
    return when {
        this == 1 && that == 3 -> true
        this == 2 && that == 1 -> true
        this == 3 && that == 2 -> true
        else -> false
    }
}

fun losingMove(move: String): String {
  return when(move) {
    "A" -> "Z"
    "B" -> "X"
    "C" -> "Y"
    else -> "X"
  }
}

fun winningMove(move: String): String {
  return when(move) {
    "A" -> "Y"
    "B" -> "Z"
    "C" -> "X"
    else -> "X"
  }
}

fun chooseMove(moveAndGoal: Pair<String, String>): Pair<String, String> {
   val move = moveAndGoal.first
   val goal = moveAndGoal.second

   return when(goal) {
     "X" -> Pair(move,losingMove(move))
     "Y" -> Pair(move,move)
     "Z" -> Pair(move,winningMove(move))
     else -> Pair(move,move)
   }
}

fun score(moves: Pair<String, String>): Pair<Int, Int> {
    val (moveOne, moveTwo) = moves.toList().map(::moveToInt).zipWithNext().first()

    return when {
        moveOne == moveTwo -> Pair(moveOne + 3, moveTwo + 3)
        moveOne beats moveTwo -> Pair(moveOne + 6, moveTwo)
        else -> Pair(moveOne, moveTwo + 6)
    }
}
