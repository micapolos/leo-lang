comment native int type

private
  let
    integer.class
    be "java.lang.Integer".name.class.object.java.class

  test
    integer.class.script
    equals "class java.lang.Integer".native.object.java.class

  let
    leo int
    class
    be "leo16.native.IntKt".name.class.object.java.class

  test
    int.leo.class.script
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

  let
    leo int plus
    method
    be
      int.leo.class
      method
        name "plus"
        types
          element int.class.java
          element int.class.java
          array.object.java

  test
    plus.int.leo.method.script
    equals "public static final int leo16.native.IntKt.plus(int,int)".native.object.java.method

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
    int.java
    invoke
      script plus
      int.leo.method
      args
        element int.java
        element plus.int.java
        array.object.java
    int

test
  2.int
  plus 3.int
  equals 5.int
