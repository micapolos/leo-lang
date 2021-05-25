let
  option any
  update present doing any
  do
    option
    switch
      present do
        update
        present
        doing
        give present
      absent option

test
  option present "Hello, "
  update present doing
    present
    text
    append "world!"
  equals "Hello, world!"

test
  option absent
  update present doing
    present
    text
    append "world!"
  equals option absent
