test
  "Hello, world!"
  as text any
  is equal "Hello, world!"

test
  try 
    "Hello, world!"
    as number any
  is equal quote try error
    "Hello, world!"
    as number any

test
  try
    "Hello, world!"
    as text
  is equal quote try error
    "Hello, world!"
    as text

test
  point
    x 10
    y 20
  as point any
  is equal point
    x 10
    y 20
