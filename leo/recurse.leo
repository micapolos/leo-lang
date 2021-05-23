100
do recursing
  get number
  is 0
  switch
    yes become "Recursed 100 times, hopefully without overflowing the stack."
    no become
      get number
      subtract 1
      recurse
