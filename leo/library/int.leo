private
  let
    integer.class
    be "java.lang.Integer".name.class.object.java.class

  test
    integer.class.script
    equals "class java.lang.Integer".native.object.java.class

  let
    kt int
    class
    be "leo16.native.IntKt".name.class.object.java.class

  test
    int.kt.class.script
    equals class java object native "class leo16.native.IntKt"

  let
    int.class
    be
      integer.class
      field name "TYPE"
      get null.object.java
      class

  test
    int.class.script
    equals "int".native.object.java.class

  private
    let
      op text any
      method
      do
        int.kt.class
        method
          name op.text
          types
            element int.class.java
            element int.class.java
            array.object.java

  let
    plus.method
    be "plus".op.method

  test
    plus.method.script
    equals "public static final int leo16.native.IntKt.plus(int,int)".native.object.java.method

  let
    minus.method
    be "minus".op.method

  test
    minus.method.script
    equals "public static final int leo16.native.IntKt.minus(int,int)".native.object.java.method

  let
    times.method
    be "times".op.method

  test
    times.method.script
    equals "public static final int leo16.native.IntKt.times(int,int)".native.object.java.method

let
  number any
  int
  do number.integer.object.java.int

test
  123.int.script
  equals "123".native.object.java.int

let
  int any
  plus int any
  do
    script plus
    method
    invoke
      int.java
      args
        element int.java
        element plus.int.java
        array.object.java
    int

test
  2.int
  plus 3.int
  equals 5.int

let
  int any
  minus int any
  do
    script minus
    method
    invoke
      int.java
      args
        element int.java
        element minus.int.java
        array.object.java
    int

test
  5.int
  minus 3.int
  equals 2.int

let
  int any
  times int any
  do
    script times
    method
    invoke
      int.java
      args
        element int.java
        element times.int.java
        array.object.java
    int

test
  2.int
  times 3.int
  equals 6.int
