test
  "Hello, world!"
  as text any
  equals "Hello, world!"

test
  try 
    "Hello, world!"
    as number any
  equals quote try error
    "Hello, world!"
    as number any

test
  try
    "Hello, world!"
    as text
  equals quote try error
    "Hello, world!"
    as text

test
  point
    x 10
    y 20
  as point any
  equals point
    x 10
    y 20
