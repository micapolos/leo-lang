test
  point
    x 10
    y 20
  value.text
  equals "point\n  x 10\n  y 20\n"

test
  "point\n  x 10\n  y 20\n"
  value.point
  equals point
    x 10
    y 20
