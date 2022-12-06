#!/usr/bin/env kotlin

import java.io.File

data class Move(val count: Int, val fromIndex: Int, val toIndex: Int)

if (args.size != 1) {
    println("Please provide the file containing the puzzle input.")
} else {
    runCatching {

        val puzzleInput = readFile(args.first())

        puzzleInput.forEach {
            println(findMarker1(it, 4))
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

fun findMarker1(input: String, markerLength: Int = 4): Int {
    var x = 0
    var y: String
    do {
        y = input.substring(x++, x + markerLength - 1)
    } while (y.toSet().size != markerLength)

    return x + markerLength - 1
}

fun findMarker(input: String, markerLength: Int = 4): Int {
    val win = input.windowed(markerLength, 1).map { it.toSet().size }
    return win.indexOf(markerLength) + markerLength
}
