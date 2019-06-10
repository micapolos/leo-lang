package leo5.compiler

data class Struct(val fieldList: List<Field>)

fun struct(fieldList: List<Field>) = Struct(fieldList)
