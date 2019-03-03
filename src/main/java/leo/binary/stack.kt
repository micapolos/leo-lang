package leo.binary

data class Stack(
	val nonEmptyStackOrNull: NonEmptyStack?)

data class NonEmptyStack(
	val topBit: Bit,
	val tailStack: Stack)

val emptyStack
	get() =
		Stack(null)

val Bit.nonEmptyStack
	get() =
		NonEmptyStack(this, emptyStack)

fun Stack.push(bit: Bit) =
	Stack(nonEmptyStackOrNull?.push(bit))

fun NonEmptyStack?.push(bit: Bit) =
	NonEmptyStack(bit, Stack(this))
