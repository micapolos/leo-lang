use library number

comment
  do repeating
    executes the given code
    using tail recursion
  repeat
    repeats the execution
    with the current value

10000
do repeating
  number
  equals 0
  switch
    yes do "Repeated 10000 times"
    no do
      number
      minus 1
      repeat
