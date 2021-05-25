test
  item "Hello, "
  switch
    text append "world!"
    number plus 3
  equals "Hello, world!"

test
  item 2
  switch
    text append "world!"
    number plus 3
  equals 5
