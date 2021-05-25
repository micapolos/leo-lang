test
  "Hello, "
  append "world!"
  equals "Hello, world!"

test
  "Hello, world!"
  length
  equals length 13

let
  text any
  exclamate
  do
    text
    append "!"

test
  "Hello, world"
  exclamate
  equals "Hello, world!"

let
  text any
  parenthesize
  do
    "("
    append text
    append ")"

test
  "hello"
  parenthesize
  equals "(hello)"
