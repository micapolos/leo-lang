package leo32.vm

const val noOp = 0

const val i32invOp = 1
const val i32andOp = 2
const val i32orOp = 3
const val i32xorOp = 4

const val i32negOp = 5
const val i32incOp = 6
const val i32decOp = 7
const val i32plusOp = 8
const val i32minusOp = 9
const val i32timesOp = 10
const val i32divOp = 11
const val i32remOp = 12

const val i32constOp = 13
const val i32loadOp = 14
const val i32storeOp = 15
const val i32popOp = 16

const val jumpOp = 17
const val branchOp = 18
const val callOp = 19
const val jumpZOp = 20
const val retOp = 21

const val i32shlOp = 22
const val i32shrOp = 23
const val i32ushrOp = 24

const val f32negOp = 25
const val f32plusOp = 26
const val f32minusOp = 27
const val f32timesOp = 28
const val f32divOp = 29
const val f32remOp = 30
