test
  try "Hello, "
  switch
    success
      text
      append "world!"
    error
      text
      append "hell!"
  equals "Hello, world!"

test
  try fail "Hello, "
  switch
    success
      text
      append "world!"
    error
      text
      append "hell!"
  equals "Hello, hell!"
