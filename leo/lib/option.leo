let
  option any
  map doing any
  do
    get option
    switch
      present option
        get map
        get doing
        apply
          get option
          get present
      absent option absent

option present "foo"
map doing
  get present
  get text
  append "bar"
