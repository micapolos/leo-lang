package leo13.type

data class TypeTrace(val lhsOrNull: TypeTrace?, val type: Type)

val Type.trace get() = TypeTrace(null, this)
fun TypeTrace?.plus(type: Type) = TypeTrace(this, type)
