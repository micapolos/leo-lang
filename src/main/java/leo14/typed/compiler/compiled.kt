package leo14.typed.compiler

import leo13.Index
import leo13.index0
import leo13.next
import leo14.lambda.Evaluator
import leo14.lambda.arg0
import leo14.lambda.eval
import leo14.typed.*
import leo14.typed.compiler.Definition.Kind.ACTION
import leo14.typed.compiler.Definition.Kind.VALUE

data class Compiled<T>(
	val memory: Memory<T>,
	val localIndex: Index,
	val typed: Typed<T>) {
	override fun toString() = "$reflectScriptLine"
}

fun <T> compiled(typed: Typed<T> = typed(), memory: Memory<T> = memory()) = Compiled(memory, index0, typed)

val <T> Compiled<T>.begin: Compiled<T>
	get() =
		copy(typed = typed(), localIndex = index0)

fun <T> Compiled<T>.beginGives(type: Type): Compiled<T> =
	begin
		.updateMemory {
			plus(
				item(
					definition(
						type("given") does typed(arg0(), type("given" lineTo type)),
						ACTION)))
		}
		.updateLocalIndex { next }

fun <T> Compiled<T>.updateTyped(fn: Typed<T>.() -> Typed<T>): Compiled<T> =
	copy(typed = typed.fn())

fun <T> Compiled<T>.updateMemory(fn: Memory<T>.() -> Memory<T>): Compiled<T> =
	copy(memory = memory.fn())

fun <T> Compiled<T>.resolve(line: TypedLine<T>, context: Context<T>): Compiled<T> =
	updateTyped {
		plus(line)
			.run {
				memory.resolve(this) ?: context.resolve(this) ?: resolve ?: this
			}
	}

val <T> Compiled<T>.resolveForEnd: Compiled<T>
	get() =
		compiled(memory.ret(typed))

fun <T> Compiled<T>.eval(evaluator: Evaluator<T>) =
	updateTyped { resolveForEnd.typed.term.eval(evaluator) of typed.type }

val <T> Compiled<T>.use
	get() =
		begin
			.updateMemory {
				plus(
					item(
						definition(
							type("used") does typed("used" lineTo resolveForEnd.typed),
							VALUE)))
			}
			.updateLocalIndex { next }

fun <T> Compiled<T>.updateLocalIndex(fn: Index.() -> Index) =
	copy(localIndex = localIndex.fn())