test
  foo
  equals foo
  equals quote equals yes

test
  foo
  equals bar
  equals quote equals no

test
  "Hello"
  equals "Hello"
  equals quote equals yes

test
  "Hello"
  equals "world"
  equals quote equals no

test
  "Hello"
  equals 123
  equals quote equals no

test
  2
  equals 2
  equals quote equals yes

test
  2
  equals 3
  equals quote equals no
