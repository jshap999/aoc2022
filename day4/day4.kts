#!/usr/bin/env kotlin

import java.io.File

if (args.size != 1) {
    println("Please provide the file containing the puzzle input.")
} else {
    runCatching {

        val puzzleInput = readFile(args.first())
            .splitIntoElves()
            .splitSectionsStartEnd()
            .spreadSections()

        val message = puzzleInput.check(::isEncompassing)
            .count { it == true }

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

fun List<String>.splitIntoElves(): List<List<String>> {
    return this.map { it.split(",") }
}

fun List<List<String>>.splitSectionsStartEnd(): List<List<List<String>>> {
    return this.map { elves -> elves.map { it.split("-") } }
}

fun List<List<List<String>>>.spreadSections(): List<List<List<Int>>> {
    return this.map { elves -> elves.map { (it[0].toInt()..it[1].toInt()).toList() } }
}

fun List<List<List<Int>>>.check(fn: (List<List<Int>>) -> Boolean): List<Boolean> {
    return this.map { fn(it) }
}

fun isEncompassing(lists: List<List<Int>>): Boolean = lists[0].containsAll(lists[1]) || lists[1].containsAll(lists[0])
