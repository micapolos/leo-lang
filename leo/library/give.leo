test
  doing
    text
    append "world!"
  give "Hello, "
  equals "Hello, world!"

test
  try
    blob
    give "Hello, "
  equals script try error
    blob
    not function
