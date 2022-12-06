
#!/bin/bash

trap 'rm $tmp' 0
tmp=d1.$$
touch $tmp

if [ "X$1" == "X" ]; then
  echo "Must supply an input file"
  exit 2
fi

cat $1 | sed 's/^$/:/g' | xargs | tr ":" "\n" > $tmp

getItems() {
  take=$1
  tailCmd="tail -$take"

  cat $tmp | while read line
  do
    y=0
    for x in $line
    do
      y=`expr $y + $x`
    done
    echo $y
  done | sort -n | eval $tailCmd
}

for item in `getItems ${2:-1}`
do
  z=`expr ${z:-0} + $item`
done

echo $z
