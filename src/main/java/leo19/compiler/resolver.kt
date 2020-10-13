package leo19.compiler

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo.base.notNullIf
import leo.base.runIf
import leo13.Stack
import leo13.push
import leo13.seq
import leo13.stack
import leo14.Script
import leo19.term.invoke
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.Struct
import leo19.type.StructType
import leo19.type.Type
import leo19.type.fieldTo
import leo19.type.indexedOrNull
import leo19.type.struct
import leo19.typed.Typed
import leo19.typed.nullTyped
import leo19.typed.of

sealed class Binding
data class StructBinding(val struct: Struct) : Binding()
data class ArrowBinding(val arrow: Arrow) : Binding()

data class Resolver(val bindingStack: Stack<Binding>)

fun binding(struct: Struct): Binding = StructBinding(struct)
fun binding(arrow: Arrow): Binding = ArrowBinding(arrow)

val emptyResolver = Resolver(stack())
fun resolver(vararg bindings: Binding) = Resolver(stack(*bindings))
fun Resolver.plus(binding: Binding) = Resolver(bindingStack.push(binding))

fun Resolver.typed(script: Script): Typed =
	Compiler(this, nullTyped).plus(script).typed

fun Resolver.resolveOrNull(typed: Typed): Typed? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.let { binding ->
			binding.resolveOrNull(typed, index)
		}
	}

fun Resolver.resolve(typed: Typed): Typed =
	resolveOrNull(typed) ?: typed

fun Binding.resolveOrNull(typed: Typed, index: Int): Typed? =
	when (this) {
		is StructBinding ->
			notNullIf(typed.type == struct("given")) {
				term(variable(index)).of(struct("given" fieldTo StructType(struct)))
			}
		is ArrowBinding ->
			notNullIf(arrow.lhs == typed.type) {
				term(variable(index)).invoke(typed.term).of(arrow.rhs)
			}
	}
