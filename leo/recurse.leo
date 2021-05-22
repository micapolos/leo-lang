100
do recursing
  get number
  is 0
  switch
    yes be "Recursed 100 times, hopefully without overflowing the stack."
    no be
      get number
      subtract 1
      recurse
