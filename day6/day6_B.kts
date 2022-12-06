#!/usr/bin/env kotlin

import java.io.File

data class Move(val count: Int, val fromIndex: Int, val toIndex: Int)

if (args.size != 1) {
    println("Please provide the file containing the puzzle input.")
} else {
    runCatching {

        val puzzleInput = readFile(args.first())

        puzzleInput.forEach {
            println(findMarker(it, 14))
        }

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

fun findMarker(input: String, markerLength: Int = 4): Int {
    val win = input.windowed(markerLength, 1).map { it.toSet().size }
    return win.indexOf(markerLength) + markerLength
}
