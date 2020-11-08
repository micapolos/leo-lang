package leo21.type

import leo13.map

data class Recursive(val type: Type)

fun recursive(type: Type) = Recursive(type)
val Type.asRecursive get() = recursive(this)
