package leo14.typed.compiler

import leo13.Index
import leo13.index0
import leo13.next
import leo14.lambda.Evaluator
import leo14.lambda.eval
import leo14.typed.*

data class Compiled<T>(
	val memory: Memory<T>,
	val localMemorySize: Index,
	val typed: Typed<T>) {
	override fun toString() = "$reflectScriptLine"
}

fun <T> compiled(
	typed: Typed<T> = typed(),
	memory: Memory<T> = memory(),
	localMemorySize: Index = index0) =
	Compiled(memory, localMemorySize, typed)

val <T> Compiled<T>.begin: Compiled<T>
	get() =
		copy(typed = typed(), localMemorySize = index0)

fun <T> Compiled<T>.plusGiven(type: Type): Compiled<T> =
	plus(item(key(type("given")), argumentMemoryValue(type("given" lineTo type))))

fun <T> Compiled<T>.updateTyped(fn: Typed<T>.() -> Typed<T>): Compiled<T> =
	copy(typed = typed.fn())

fun <T> Compiled<T>.updateMemory(fn: Memory<T>.() -> Memory<T>): Compiled<T> =
	copy(memory = memory.fn())

fun <T> Compiled<T>.forget(key: TypeKey) =
	updateMemory { forget(key) }

val <T> Compiled<T>.forgetEverything
	get() =
		updateMemory { forgetEverything }

fun <T> Compiled<T>.plus(item: MemoryItem<T>): Compiled<T> =
	updateMemory { plus(item) }.updateLocalIndex { next }

fun <T> Compiled<T>.resolve(line: TypedLine<T>, context: Context<T>): Compiled<T> =
	updateTyped {
		plus(line)
			.run {
				memory.resolve(this) ?: context.resolve(this) ?: resolve ?: this
			}
	}

val <T> Compiled<T>.typedForEnd: Typed<T>
	get() =
		memory.resolveForEnd(typed.term, localMemorySize) of typed.type

val <T> Compiled<T>.typedForEval: Typed<T>
	get() =
		memory.resolveForEval(typed.term) of typed.type

fun <T> Compiled<T>.eval(evaluator: Evaluator<T>) =
	updateTyped { typedForEval.term.eval(evaluator) of typed.type }

val <T> Compiled<T>.plusUsed
	get() =
		plus(
			item(
				key(type("used")),
				value(
					memoryBinding(
						typed("used" lineTo typedForEnd),
						isAction = false))))

fun <T> Compiled<T>.updateLocalIndex(fn: Index.() -> Index) =
	copy(localMemorySize = localMemorySize.fn())