test
  "Hello, world!"
  is matching text any
  is equal is yes

test
  "Hello, world!"
  is matching text
  is equal is no

test
  123
  is matching number any
  is equal is yes

test
  123
  is matching number
  is equal is no

test
  point
    x 10
    y 20
  is matching point any
  is equal is yes

test
  point
    x 10
    y 20
  is matching circle any
  is equal is no

test
  point
    x 10
    y 20
  is matching point
    x number any
    y number any
  is equal is yes

test
  point
    x 10
    y 20
  is matching point
    x text any
    y text any
  is equal is no
