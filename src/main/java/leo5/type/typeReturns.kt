package leo5.type

data class TypeReturns(val type: Type)

fun returns(type: Type) = TypeReturns(type)