let
  text any
  length
  do
    text.object.java
    invoke
      "java.lang.String".name.class.object.java.class
      method
        name "length"
        types array.object.java
      args array.object.java
    integer.number.length

test
  "Hello, world!".length
  equals length 13

let
  text any
  append text any
  do
    text.object.java
    invoke
      "java.lang.String".name.class.object.java.class
      method
        name "concat"
        types "java.lang.String".name.class.object.java.element.array.object.java
      args append.text.object.java.element.array.object.java
    text

test
  "Hello, "
  append "world!"
  equals "Hello, world!"
