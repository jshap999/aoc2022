const fs = require('fs')

const args = process.argv.slice(2)

const input = fs.readFileSync(args[0]).toString().trim().split("\n")

input.forEach( line => 
  console.log(findMarker(line, 4))
)

function findMarker(input, markerLength) {
  let x = 0;
  let y = "";
  do {
    y = input.substring(x++, x + markerLength - 1);
  } while ( new Set(y).size != markerLength);

  return x + markerLength - 1;
}

