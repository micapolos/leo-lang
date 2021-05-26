comment native int type

private
  let
    int.type
    do
      "java.lang.Integer".name.class.object.java.class
      method
        name "TYPE"

let
  number any
  int
  do number.integer.object.java.int

let
  int any
  plus int any
  do
    "leo16.native.IntKt".name.class.object.java.class
    method
      name "plus"
      types
        element 