test
  "Hello, world!"
  is "Hello, world!"
  equals script is yes

test
  "Hello, world!"
  is text any
  equals script is yes

test
  "Hello, world!"
  is text
  equals script is no

test
  123
  is 123
  equals script is yes

test
  123
  is number any
  equals script is yes

test
  123
  is number
  equals script is no

test
  doing foo
  is doing any
  equals script is yes

test
  comment functions are not comparable
  doing foo
  is doing foo
  equals script is no

test
  doing foo
  is doing
  equals script is no

test
  point
    x 10
    y 20
  is point any
  equals script is yes

test
  point
    x 10
    y 20
  is point 
    x any
    y any
  equals script is yes

test
  point
    x 10
    y 20
  is point 
    x number any
    y number any
  equals script is yes

test
  point
    x 10
    y 20
  is point 
    x text any
    y text any
  equals script is no

test
  point
    x 10
    y 20
  is point 
    x 10
    y 20
  equals script is yes

test
  point
    x 10
    y 20
  is point 
    x 20
    y 10
  equals script is no
