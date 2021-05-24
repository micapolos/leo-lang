10
do recursing
  number
  equals 0
  switch
    yes do "Recursed 100 times, hopefully without overflowing the stack."
    no do
      number
      minus 1
      recurse
