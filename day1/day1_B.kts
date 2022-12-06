#!/usr/bin/env kotlin

import java.io.File

if (args.size != 1) {
    println("Please provide the file containing each elf's inventory.")
} else {
    runCatching {
        val file = File(args.first())

        val inventory = file.readLines().map { if (it.isNullOrBlank()) null else runCatching { it.toInt() }.getOrElse { 0 } }

        inventory
            .groupedByElf()
            .summedPerElf()
            .elvesWithTotals()
            .findTopElves(3).map { it.second }.sum().run(::println)
    }.getOrElse {
        println("Something went wrong.")
        println("${it.stackTraceToString()}")
    }
}

fun List<Int?>.groupedByElf(): List<List<Int>> {
    return this.fold(mutableListOf(emptyList<Int>())) { tot, item ->
        if (item == null) {
            tot.add(emptyList<Int>())
        } else {
            val last = tot.removeLast()
            tot.add(last.plus(item))
        }
        tot
    }.toList()
}

fun List<List<Int>>.summedPerElf(): List<Int> = this.map { it.sum() }

fun List<Int>.elvesWithTotals(): List<Pair<Int,Int>> = this.map { Pair(this.indexOf(it) + 1, it) }

fun List<Pair<Int,Int>>.findTopElves(num: Int) = this.sortedBy { it.second }.takeLast(num)

fun List<Int>.elfWithMaxCalories(): Pair<Int, Int>? = this.maxOrNull()?.let { Pair(this.indexOf(it) + 1, it) }

fun Pair<Int, Int>?.report() = (this?.let { "Elf $first has the most calories: $second." } ?: "No elf was found.").run(::println)
