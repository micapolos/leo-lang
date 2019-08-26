package leo13.type

data class FunctionType(val parameter: Type, val result: Type)

fun functionType(parameter: Type, result: Type) = FunctionType(parameter, result)
