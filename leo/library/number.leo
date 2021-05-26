let
  number any
  plus number any
  do
    "leo25.natives.NumberKt".name.class.object.java.class
    method
      name "plus"
      types
        element "leo14.Number".name.class.object.java
        element "leo14.Number".name.class.object.java
        array.object.java
    do
      null.object.java
      invoke
        method
        args
          element number.object.java
          element plus.number.object.java
          array.object.java
      number

test
  2
  plus 3
  equals 5

let
  number any
  minus number any
  do
    "leo25.natives.NumberKt".name.class.object.java.class
    method
      name "minus"
      types
        element "leo14.Number".name.class.object.java
        element "leo14.Number".name.class.object.java
        array.object.java
    do
      null.object.java
      invoke
        method
        args
          element number.object.java
          element minus.number.object.java
          array.object.java
      number

test
  5
  minus 3
  equals 2

let
  number any
  times number any
  do
    "leo25.natives.NumberKt".name.class.object.java.class
    method
      name "times"
      types
        element "leo14.Number".name.class.object.java
        element "leo14.Number".name.class.object.java
        array.object.java
    do
      null.object.java
      invoke
        method
        args
          element number.object.java
          element times.number.object.java
          array.object.java
      number

test
  2
  times 3
  equals 6

let
  number any
  increment
  do
    number
    plus 1

test
  2.increment
  equals 3

let
  number any
  decrement
  do
    number
    minus 1

test
  2.decrement
  equals 1
