package leo21.type.value

import leo13.push
import leo21.type.Type

data class TypeValue(val context: TypeContext, val type: Type)

fun TypeContext.value(type: Type) = TypeValue(this, type)
val Type.value: TypeValue get() = emptyTypeContext.value(this)
fun TypeContext.plus(type: Type) = TypeContext(typeStack.push(type))