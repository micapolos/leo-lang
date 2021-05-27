private
  let
    string.class
    be "java.lang.String".name.class.object.java.class

  let
    length.method
    be
      string.class
      method
        name "length"
        types array.object.java

  let
    concat.method
    be
      string.class
      method
        name "concat"
        types string.class.java.element.array.object.java

let
  text any
  length
  do
    quote length
    method
    invoke
      text.object.java
      args array.object.java
    integer.number.length

test
  "Hello, world!".length
  equals length 13

let
  text any
  append text any
  do
    concat.method
    invoke
      text.object.java
      args append.text.object.java.element.array.object.java
    text

test
  "Hello, "
  append "world!"
  equals "Hello, world!"
