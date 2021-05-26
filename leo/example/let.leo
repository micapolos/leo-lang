use library text

comment
  let be
    defines a constant
    matching the given pattern
    with evaluated value

example 
  let
    greeting
    be
      "Hello, "
      append "world!"

  greeting

comment
  let do
    defines a function
    matching the given pattern
    executing the given code

example
  let
    first text any
    second text any
    join
    do
      first.text
      append second.text

  first "Hello, "
  second "world!"
  join
