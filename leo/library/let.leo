test
  let
    ping
    do pong
  ping
  equals pong

test
  let
    text any
    exclamate
    do
      text
      append "!"
  "Hello, world"
  exclamate
  equals "Hello, world!"

test
  let
    first text any
    second text any
    join
    do
      first
      text
      append
        second
        text
  first "Hello, "
  second "world!"
  join
  equals "Hello, world!"
