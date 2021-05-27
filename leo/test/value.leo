test
  point
    x zero
    y one
  value.text
  equals "point\n  x zero\n  y one\n"

test
  "point\n  x zero\n  y one\n"
  value.point
  equals point
    x zero
    y one
