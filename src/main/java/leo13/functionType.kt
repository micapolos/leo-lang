package leo13

import leo13.type.Type

data class FunctionType(val parameter: Type, val result: Type)

fun functionType(parameter: Type, result: Type) = FunctionType(parameter, result)
