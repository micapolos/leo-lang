package leo32

import leo.binary.*

data class Runtime(
	val scopeFunction: Function,
	val bitStack: Stack32<Bit>,
	val currentFunction: Function) {
	override fun toString() = code
}

val emptyRuntime =
	Runtime(emptyFunction, emptyStack32(), emptyFunction)

val Function.runtime
	get() =
		Runtime(this, emptyStack32(), this)

fun Runtime.invoke(bit: Bit) =
	currentFunction.invoke(bit, this)

fun Runtime.invoke(log: Log) =
	copy(
		bitStack = bitStack.updateTop {
			apply {
				println("[${log.tag.string}] $this")
			}
		}!!
	)

fun Runtime.push(bit: Bit) =
	copy(bitStack = bitStack.push(bit)!!)

fun Runtime.goto(functionOrNull: Function?) =
	copy(currentFunction = functionOrNull ?: scopeFunction)

val Runtime.pop
	get() =
		copy(bitStack = bitStack.shrink!!)

val Runtime.nand
	get() =
		op2 { nand(it) }

fun Runtime.op2(fn: Bit.(Bit) -> Bit) =
	copy(bitStack = bitStack.pop!!.let { rhs ->
		rhs.stack32.updateTop {
			fn(rhs.value)
		}!!
	})