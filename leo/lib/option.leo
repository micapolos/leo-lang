let
  option any
  map present doing any
  do
    option
    switch
      present do
        map
        present
        doing
        apply present
      absent

option present "Hello, "
map present doing
  present
  text
  plus "world!"
