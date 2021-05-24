10
do recursing
  number
  equals 0
  switch
    yes become "Recursed 100 times, hopefully without overflowing the stack."
    no become
      number
      minus 1
      recurse
