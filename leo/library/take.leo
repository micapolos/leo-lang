test
  "Hello, "
  take doing
    text
    append "world!"
  equals "Hello, world!"

test
  try
    "Hello, "
    take blob
  equals script try error
    blob
    not function
