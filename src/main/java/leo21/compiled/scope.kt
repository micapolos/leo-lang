package leo21.compiled

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo.base.notNullOrError
import leo13.Stack
import leo13.push
import leo13.seq
import leo13.stack
import leo14.Script
import leo14.fieldOrNull
import leo14.lambda.arg0
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.linkOrNull
import leo21.prim.Prim
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.type
import leo21.typed.ArrowTyped
import leo21.typed.Typed
import leo21.typed.of
import leo21.typed.reference
import leo21.typed.resolve
import leo21.typed.resolveOrNull

data class Scope(val bindingStack: Stack<Binding>)

val emptyScope = Scope(stack())
fun Scope.push(binding: Binding) = Scope(bindingStack.push(binding))
fun Scope.push(type: Type) = push(TypeBinding(type))

fun Scope.resolveOrNull(name: String): Typed? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, name)
	}

fun Scope.resolveOrNull(typed: Typed): Typed? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, typed)
	}

fun Scope.resolve(typed: Typed): Typed =
	null
		?: resolveOrNull(typed)
		?: arg0<Prim>().of(typed.type).resolveOrNull?.let { fn(it.term).invoke(typed.term).of(it.type) }
		?: typed

fun Scope.arrowTyped(script: Script): ArrowTyped =
	script.linkOrNull.notNullOrError("function syntax error").let { link ->
		link.line.fieldOrNull.notNullOrError("function syntax error").let { field ->
			if (field.string != "doing") error("function syntax error")
			else link.lhs.type.let { lhsType ->
				push(lhsType).typed(field.rhs).let { bodyTyped ->
					ArrowTyped(fn(bodyTyped.term), lhsType arrowTo bodyTyped.type)
				}
			}
		}
	}
