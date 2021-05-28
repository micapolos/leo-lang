test
  use test used
  ping
  is equal pong

test
  use test used
  foo
  is equal foo

test
  try use test missing
  is equal quote try error "test/missing.leo (No such file or directory)"
