(module
  (import "host" "print" (func (param i32)))
  (func (param i32) (param i32) (result i32)
    get_local 0
    get_local 1
    i32.add)
  (func (result i32)
    i32.const 128
    call 0
    i32.const 2
    i32.const 3
    call 1)
  (export "main" (func 2)))
