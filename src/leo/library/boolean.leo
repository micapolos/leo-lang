let
  boolean any
  negate
  do
    boolean
    switch
      false do true.boolean
      true do false.boolean

test
  false.boolean.negate
  equals true.boolean

test
  true.boolean.negate
  equals false.boolean

let
  boolean any
  and boolean any
  do
    boolean
    switch
      true do and.boolean
      false do false.boolean

test
  false.boolean
  and false.boolean
  equals false.boolean

test
  false.boolean
  and true.boolean
  equals false.boolean

test
  true.boolean
  and false.boolean
  equals false.boolean

test
  true.boolean
  and true.boolean
  equals true.boolean

let
  boolean any
  or boolean any
  do
    boolean
    switch
      false do or.boolean
      true do true.boolean

test
  false.boolean
  or false.boolean
  equals false.boolean

test
  false.boolean
  or true.boolean
  equals true.boolean

test
  true.boolean
  or false.boolean
  equals true.boolean

test
  true.boolean
  and true.boolean
  equals true.boolean
