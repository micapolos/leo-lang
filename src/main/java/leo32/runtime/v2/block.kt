package leo32.runtime.v2

sealed class Block
data class SimpleBlock(val primitive: Primitive) : Block()
data class ComplexBlock(val complex: Complex) : Block()

fun block(primitive: Primitive) =
	SimpleBlock(primitive) as Block

fun block(complex: Complex) =
	ComplexBlock(complex) as Block

fun Block.plus(field: Field) =
	when (this) {
		is SimpleBlock -> block(primitive.plus(field))
		is ComplexBlock -> block(complex.plus(field))
	}