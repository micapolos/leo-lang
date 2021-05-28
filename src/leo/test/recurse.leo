use library number

comment
  recursive fibonacci

test
  16
  do recursing
    number
    equals 0
    switch
      yes do 0
      no do
        number
        equals 1
        switch
          yes do 1
          no do
            number
            minus 2
            recurse
            plus
              number
              minus 1
              recurse
  equals 987
