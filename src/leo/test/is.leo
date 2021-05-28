test
  "Hello, world!"
  is text any
  equals quote is yes

test
  "Hello, world!"
  is text
  equals quote is no

test
  123
  is number any
  equals quote is yes

test
  123
  is number
  equals quote is no

test
  point
    x 10
    y 20
  is point any
  equals quote is yes

test
  point
    x 10
    y 20
  is circle any
  equals quote is no

test
  point
    x 10
    y 20
  is point
    x number any
    y number any
  equals quote is yes

test
  point
    x 10
    y 20
  is point
    x text any
    y text any
  equals quote is no
