package leo5.type

import leo5.Value
import leo5.int

object TypeInt

val int = TypeInt

fun TypeInt.compile(value: Value) = value.int