exact
  match
    "Hello, world!"
    matching "Hello, world!"
  mismatch
    "Hello, world!"
    matching "Hello, universe!"
not exact
  match
    "Hello, world!"
    matching text any
  mismatch
    "Hello, world!"
    matching number any
