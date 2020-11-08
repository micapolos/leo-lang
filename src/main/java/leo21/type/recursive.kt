package leo21.type

data class Recursive(val type: Type)

fun recursive(type: Type) = Recursive(type)
val Type.asRecursive get() = recursive(this)