comment
  try 
    catches value thrown with fail
    and gives one of
      try success value
      try error value

example
  try "Hello, "
  switch
    success
      text
      append "world!"
    error
      text
      append "hell!"

example
  try fail "Hello, "
  switch
    success
      text
      append "world!"
    error
      text
      append "hell!"
