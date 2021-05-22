100
do recursing
  get number
  equals 0
  switch
    true "Recursed 100 times, hopefully without overflowing the stack."
    false
      get number
      subtract 1
      recurse
