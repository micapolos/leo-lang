let
  number any
  plus number any
  do
    "leo25.natives.NumberKt".class.java.class
    method
      "plus"
      args
        element "leo14.Number".class.java
        element "leo14.Number".class.java
        array.java
    invoke
      null.java
      args
        element number.java
        element plus.number.java
        array.java
    number

test
  2
  plus 3
  is equal 5

let
  number any
  minus number any
  do
    "leo25.natives.NumberKt".class.java.class
    method
      "minus"
      args
        element "leo14.Number".class.java
        element "leo14.Number".class.java
        array.java
    invoke
      null.java
      args
        element number.java
        element minus.number.java
        array.java
    number

test
  5
  minus 3
  is equal 2

let
  number any
  times number any
  do
    "leo25.natives.NumberKt".class.java.class
    method
      "times"
      args
        element "leo14.Number".class.java
        element "leo14.Number".class.java
        array.java
    invoke
      null.java
      args
        element number.java
        element times.number.java
        array.java
    number

test
  2
  times 3
  is equal 6

let
  number any
  less than number any
  do
    "leo25.natives.NumberKt".class.java.class
    method
      "lessThan"
      args
        element "leo14.Number".class.java
        element "leo14.Number".class.java
        array.java
    invoke
      null.java
      args
        element number.java
        element less.than.number.java
        array.java
    do
      java
      is equal true.java
      switch
        yes less
        no less

test
  5
  less than 6
  is equal less yes

test
  5
  less than 5
  is equal less no

test
  5
  less than 4
  is equal less no

let
  number any
  increment
  do
    number
    plus 1

test
  2.increment
  is equal 3

let
  number any
  decrement
  do
    number
    minus 1

test
  2.decrement
  is equal 1
