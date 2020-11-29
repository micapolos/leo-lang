package leo23.term.type

sealed class Type
object NilType : Type()
object BooleanType : Type()
object TextType : Type()
object NumberType : Type()
data class TupleType(val itemTypes: List<Type>) : Type()
data class ChoiceType(val itemTypes: List<Type>) : Type()
data class ArrowType(val paramTypes: List<Type>, val returnType: Type, val isRecursive: Boolean) : Type()

fun Type.checkEqual(type: Type): Type =
	if (this != type) null!!
	else type

val List<Type>.checkEqual: Type
	get() =
		if (any { it != this[0] }) null!!
		else this[0]

fun Type.indexIn(type: Type): Int =
	(type as ChoiceType).itemTypes.lastIndexOf(this).also {
		if (it == -1) null!!
		else it
	}
