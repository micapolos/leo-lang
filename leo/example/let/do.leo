use library text

comment
  let do
    defines a function
    matching the given pattern
    executing the given code

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
