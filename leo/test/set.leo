test
  point
    x zero
    y one
  set x three
  equals point
    x three
    y one

test
  point
    x zero
    y one
    z two
  set
    x three
    z four
  equals point
    x three
    y one
    z four

test
  try
    point
      x zero
      y one
    set z two
  equals quote try error no field z
