let
  text any
  length
  do
    text.object.java
    invoke
      "java.lang.String".class.java
      method "length"
    number.length

test
  "Hello, world!".length
  equals length 13

test
  "Hello, "
  append "world!"
  equals "Hello, world!"

let
  text any
  exclamate
  do
    text
    append "!"

test
  "Hello, world".exclamate
  equals "Hello, world!"

let
  text any
  parenthesize
  do
    "("
    append text
    append ")"

test
  "hello".parenthesize
  equals "(hello)"
