package leo32

import leo.binary.*

data class Scope<T>(
	val runtime: Runtime<T>,
	val scopeFunction: Function<T>,
	val bitStack: Stack32<Bit>,
	val currentFunction: Function<T>,
	val parentScopeOrNull: Scope<T>?) {
	override fun toString() = code
}

val <T> Runtime<T>.scope
	get() =
		scope(emptyFunction())

fun <T> Runtime<T>.scope(function: Function<T>) =
	Scope(this, function, emptyStack32(), function, null)

val <T> Scope<T>.begin
	get() =
		Scope(runtime, scopeFunction, emptyStack32(), scopeFunction, this)

fun <T> Scope<T>.invoke(bit: Bit): Scope<T> =
	currentFunction.invoke(bit, this)

fun <T> Scope<T>.invoke(log: Log) =
	copy(
		bitStack = bitStack.updateTop {
			apply {
				println("[${log.tag.string}] $this")
			}
		}!!
	)

fun <T> Scope<T>.push(bit: Bit) =
	copy(bitStack = bitStack.push(bit)!!)

fun <T> Scope<T>.goto(functionOrNull: Function<T>?) =
	copy(currentFunction = functionOrNull ?: scopeFunction)

val <T> Scope<T>.pop
	get() =
		copy(bitStack = bitStack.shrink!!)

val <T> Scope<T>.nand
	get() =
		op2 { nand(it) }

val <T> Scope<T>.out
	get() =
		bitStack.pop!!.let { poppedBit ->
			copy(
				runtime = runtime.push(poppedBit.value),
				bitStack = poppedBit.stack32)
		}

fun <T> Scope<T>.op2(fn: Bit.(Bit) -> Bit) =
	copy(bitStack = bitStack.pop!!.let { rhs ->
		rhs.stack32.updateTop {
			fn(rhs.value)
		}!!
	})

