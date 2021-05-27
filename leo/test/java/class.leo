test
  "java.lang.String".class.java
  as java any
  quote
  equals java native "class java.lang.String"

test
  try "unknown".class.java
  is try error any
  equals quote is yes

test
  "Hello, world!".java.object.class
  as class java any
  quote
  equals class java native "class java.lang.String"
