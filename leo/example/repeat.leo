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
    no do number.decrement.repeat
    yes do "Repeated 10000 times"
