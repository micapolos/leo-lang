10000
do repeating
  get number
  is 0
  switch
    yes become "Repeated 10000 times"
    no become
      get number
      subtract 1
      repeat
