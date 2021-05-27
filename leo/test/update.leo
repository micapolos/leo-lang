test
  point
    x zero
    y one
  update x plus one
  equals point
    x
      zero
      plus one
    y one

test
  point
    x zero
    y one
    z two
  update
    x plus one
    z plus three
  equals point
    x
      zero
      plus one
    y one
    z
      two
      plus three

test
  try 
    point
      x zero
      y one
    update
      z plus three
  equals quote try error no field z
