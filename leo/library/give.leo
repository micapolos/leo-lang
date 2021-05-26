test
  doing text.ok
  give "Hello, world!"
  equals ok "Hello, world!"

test
  try
    blob
    give "Hello, "
  equals script try error
    blob
    not function
