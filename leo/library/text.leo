let
  text any
  length
  do
    text.object.java
    invoke
      "java.lang.String".class.java
      method 
        name "length"
        parameter types array.object.java
      args array.object.java
    number.length

test
  "Hello, world!".length
  equals length 13

let
  text any
  add text any
  do
    text.object.java
    invoke
      "java.lang.String".class.java
      method
        name "concat"
        parameter types "java.lang.String".class.java.element.array.object.java
      args element add.text.object.java
    text

test
  "Hello, "
  add "world!"
  equals "Hello, world!"

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
