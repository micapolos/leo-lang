test
  "Hello, "
  plus "world!"
  equals "Hello, world!"

let
  text any
  exclamate
  do
    text
    plus "!"

test
  "Hello, world"
  exclamate
  equals "Hello, world!"

let
  text any
  parenthesize
  do
    "("
    plus text
    plus ")"

test
  "hello"
  parenthesize
  equals "(hello)"
