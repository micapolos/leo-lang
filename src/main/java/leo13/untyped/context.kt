package leo13.untyped

import leo.base.fold
import leo13.base.stackReader
import leo13.base.stackWriter
import leo13.script.*
import leo9.Stack
import leo9.mapFirst
import leo9.push
import leo9.stack

const val contextName = "context"

val contextReader: Reader<Context>
	get() =
	reader(
		contextName,
		stackReader(functionReader),
		stackReader(bindingReader),
		::Context)

val contextWriter: Writer<Context>
	get() =
	writer(
		contextName,
		field(stackWriter(functionWriter)) { functionStack },
		field(stackWriter(bindingWriter)) { bindingStack })

data class Context(
	val functionStack: Stack<Function>,
	val bindingStack: Stack<Binding>) {
	override fun toString() = super.toString()
}

fun context() = Context(stack(), stack())
fun Context.plus(function: Function) = Context(functionStack.push(function), bindingStack)
fun Context.plusFunction(fn: (Context) -> Function) = plus(fn(this))
fun Context.plus(binding: Binding) = Context(functionStack, bindingStack.push(binding))
fun Context.evaluate(script: Script) =
	evaluator(this).fold(script.lineSeq) { plus(it) }.evaluated.script

fun Context.bodyOrNull(script: Script) = functionStack.mapFirst { bodyOrNull(script) }
fun Context.valueOrNull(script: Script) = bindingStack.mapFirst { valueOrNull(script) }

fun Context.resolveDefineOrNull(script: Script): Context? =
	script
		.linkOrNull
		?.let { link ->
			when (link.line.name) {
				"gives" -> plus(function(link.lhs.normalize.unsafeBodyPattern, body(this, link.line.rhs)))
				else -> null
			}
		}
