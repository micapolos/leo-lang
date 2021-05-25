test
  "Hello, "
  and "world!"
  equals "Hello, world!"

let
  text any
  exclamate
  do
    text
    and "!"

test
  "Hello, world"
  exclamate
  equals "Hello, world!"

let
  text any
  parenthesize
  do
    "("
    and text
    and ")"

test
  "hello"
  parenthesize
  equals "(hello)"
