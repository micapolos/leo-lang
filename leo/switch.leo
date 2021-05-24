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

comment it should give "Hello, world!"
first
  item "Hello, "
  switch
    text plus "world!"
    number plus 5

comment it should give "15"
second
  item 10
  switch
    text plus "world!"
    number plus 5

comment it should throw error
third
  item something else
  switch
    text plus "world!"
    number plus 5
