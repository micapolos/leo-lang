use library number

comment
  recursive fibonacci

test
  16
  do recursing
    number
    less than 2
    switch
      yes do number
      no do
        number
        minus 2
        recurse
        plus
          number
          minus 1
          recurse
  equals 987
