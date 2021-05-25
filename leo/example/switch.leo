comment
  switch
    switches on the only field in a structure
    and
      continues execution
      in the matching branch
      starting from the matching field
    or
      throws error
      if no branch is matching

example
  comment it should give "Hello, world!"
  item "Hello, "
  switch
    text append "world!"
    number plus 5

example
  comment it should give "15"
  item 10
  switch
    text append "world!"
    number plus 5
