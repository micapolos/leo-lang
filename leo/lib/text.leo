let
  text any
  exclamate
  do
    text
    plus "!"

example
  "Hello, world"
  exclamate

let
  text any
  parenthesize
  do
    "("
    plus text
    plus ")"

example
  "hello"
  parenthesize
