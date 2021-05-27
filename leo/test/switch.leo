test
  shape circle radius
  switch
    circle red
    rectangle blue
  equals
    red circle radius

test
  shape rectangle side
  switch
    circle red
    rectangle blue
  equals
    blue rectangle side

test
  try
    shape triangle height
    switch
      circle red
      rectangle blue
  equals quote try error nonexaustive
