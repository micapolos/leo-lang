100
do recursing
  get number
  is 0
  switch
    yes "Recursed 100 times, hopefully without overflowing the stack."
    no
      get number
      subtract 1
      recurse
