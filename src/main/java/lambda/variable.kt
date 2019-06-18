package lambda

class Variable {
	override fun toString() = code
	override fun equals(other: Any?) = other is Variable && eq(other)
}

val newVariable get() = Variable()

fun Variable.eq(variable: Variable) = this === variable

val Variable.code get() = "x${hashCode()}"
