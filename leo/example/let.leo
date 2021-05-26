use library text

comment
  let do
    defines a function
    matching the given pattern
    executing the given code
  any
    matches any value
  special cases
    text any
      matches any text
      like "Hello"
    number any
      matches any number
      like 123

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
