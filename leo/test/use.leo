test
  use test used
  ping
  equals pong

test
  use test used
  foo
  equals foo

test
  try use test missing
  equals quote try error "test/missing.leo (No such file or directory)"
