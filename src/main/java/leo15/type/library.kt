package leo15.type

import leo.base.iterate
import leo.base.notNullIf
import leo14.ScriptLine
import leo14.invoke
import leo15.givenName
import leo15.givesName
import leo15.isName
import leo15.lambda.at
import leo15.lambda.fn
import leo15.lambda.invoke
import leo15.string

data class Library(val scope: Scope, val localBindingCount: Int) {
	override fun toString() = reflectScriptLine.string
}

val Library.reflectScriptLine: ScriptLine
	get() =
		"library"(
			scope.reflectScriptLine,
			localBindingCount.localBindingCountReflectScriptLine)

val Int.localBindingCountReflectScriptLine: ScriptLine
	get() =
		"local"("binding"("count"(this)))

val Library.clearLocal: Library get() = copy(localBindingCount = 0)
fun Scope.library(localBindingCount: Int) = Library(this, localBindingCount)
val emptyLibrary = emptyScope.library(localBindingCount = 0)

fun Library.plus(binding: Binding) =
	scope.plus(binding).library(localBindingCount = localBindingCount.inc())

fun Library.applyCompiler(typed: Typed): Compiler =
	null
		?: apply(typed)?.compiler(emptyTyped)
		?: scope.apply(typed)?.let { compiler(it) }
		?: compiler(typed.apply)

fun Library.apply(typed: Typed): Library? =
	null
		?: applyIs(typed)
		?: applyGives(typed)

fun Library.applyIs(typed: Typed): Library? =
	typed.matchInfix(isName) { lhs, rhs ->
		notNullIf(lhs.type.isStatic) {
			plus(lhs.type.key bindingTo rhs.value)
		}
	}

fun Library.applyGives(typed: Typed): Library? =
	typed.matchInfix(givesName) { lhs, rhs ->
		lhs.staticScriptOrNull?.let { lhsScript ->
			rhs.staticScriptOrNull?.let { rhsScript ->
				plus(
					type(givenName).key bindingTo
						typed(givenName lineTo at(0).dynamicExpression.of(lhsScript.type)).value)
					.clearLocal
					.compiler(emptyTyped)
					.compile(rhsScript)
					.scoped
					.let { scoped ->
						plus(lhsScript.type.key bindingTo scope.with(scoped.typed.withFnTerm).value)
					}
			}
		}
	}

fun Library.scoped(typed: Typed): Scoped =
	scope.with(typed).iterate(localBindingCount) {
		scope.linkOrNull!!.let { link ->
			link.lhs.with(
				typed.updateExpression {
					updateTerm {
						fn(this).invoke(link.binding.typed.expression.term)
					}
				})
		}
	}