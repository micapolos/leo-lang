package leo15.type

import leo.base.iterate
import leo.base.notNullIf
import leo14.ScriptLine
import leo14.invoke
import leo14.lineTo
import leo14.script
import leo15.isName
import leo15.lambda.fn
import leo15.lambda.invoke

data class Library(val scope: Scope, val localBindingCount: Int)

val Library.reflectScriptLine: ScriptLine
	get() =
		"library" lineTo script(scope.reflectScriptLine, localBindingCount.localBindingCountScriptLine)

val Int.localBindingCountScriptLine: ScriptLine
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
		?: compiler(typed.apply)

fun Library.apply(typed: Typed): Library? =
	null
		?: applyIs(typed)

fun Library.applyIs(typed: Typed): Library? =
	typed.matchInfix(isName) { lhs, rhs ->
		notNullIf(lhs.type.isStatic) {
			plus(lhs.type.key bindingTo rhs.value)
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