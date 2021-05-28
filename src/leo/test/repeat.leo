use library number

comment
  tail recursive fibonacci

test
  16
  first 0
  second 1
  do repeating
    number
    equals 0
    switch
      yes do first.number
      no do
        number
        equals 1
        switch
          yes do second.number
          no do
            number
            minus 1
            first second.number
            second
              first.number
              plus second.number
            repeat
  equals 987
