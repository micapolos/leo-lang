package leo5.type

import leo5.Value

data class TypeFunction(val parameter: TypeParameter, val returnType: Type)

fun function(parameter: TypeParameter, returnType: Type) = TypeFunction(parameter, returnType)

fun TypeFunction.compile(value: Value): Any = TODO()