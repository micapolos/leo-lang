test
  "Hello, world!"
  as text any
  equals "Hello, world!"

test
  try
    "Hello, world!"
    as text
  equals quote try error
    "Hello, world!"
    as text

test
  try 
    "Hello, world!"
    as number any
  equals quote try error
    "Hello, world!"
    as number any
