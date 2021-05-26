let
  option any
  update present doing any
  do
    option
    switch
      present take
        update
        present
        doing
      absent option

test
  use library text
  option present "Hello, "
  update present doing
    present
    text
    append "world!"
  equals "Hello, world!"

test
  use library text
  option absent
  update present doing
    present
    text
    append "world!"
  equals option absent
