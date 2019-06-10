package leo5.compiler

data class Type(val size: Size, val layout: Layout)

fun type(size: Size, layout: Layout) = Type(size, layout)
