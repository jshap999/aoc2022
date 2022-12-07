import strutils
import std/strbasics
import sequtils

proc findMarker(input: string, markerLength: int): int =
  var x: int = 0;
  var y: string = "";

  while deduplicate(toSeq(y.items)).len != markerLength:
    x = x + 1
    y = input.substr(x, x + markerLength - 1)

  return x + markerLength

var contents = readFile("day6.txt")

strip(contents)

let lines = contents.splitLines()

for line in lines:
  echo findMarker(line, 4)

