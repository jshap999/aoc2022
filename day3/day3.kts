#!/usr/bin/env kotlin

import java.io.File

if (args.size != 1) {
    println("Please provide the file containing the puzzle input.")
} else {
    runCatching {

        readFile(args.first())
            .splitEachInHalf()
            .toPairsOfCharArray()
            .toListOfIntersections()
            .toPriorities()
            .sum()
            .run(::println)

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

fun List<String>.splitEachInHalf() = this.map { it.chunked(it.length / 2) }

fun List<List<String>>.toPairsOfCharArray() = this.map { Pair<CharArray, CharArray>(it[0].toCharArray(), it[1].toCharArray()) }

fun List<Pair<CharArray, CharArray>>.toListOfIntersections() = this.flatMap { it.first.intersect(it.second.toList()) }

fun List<Char>.toPriorities() = this.map { it.toPriority() }

fun Char.toPriority(): Int {
    val lower = "abcdefghijklmnopqrstuvwxyz".toCharArray()
    val upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()

    return if (this in lower) {
        lower.indexOf(this) + 1
    } else {
        upper.indexOf(this) + 27
    }
}
