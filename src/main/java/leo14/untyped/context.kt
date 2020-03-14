package leo14.untyped

import leo14.Script
import leo14.ScriptLink
import leo14.script

sealed class Context
object EmptyContext : Context()
data class NonEmptyContext(val parentContext: Context, val lastRule: Rule) : Context()

fun context() = EmptyContext as Context

fun Context.push(rule: Rule): Context = NonEmptyContext(this, rule)

fun Context.resolve(scriptLink: ScriptLink): Script? =
	when (this) {
		EmptyContext -> scriptLink.resolve
		is NonEmptyContext -> lastRule.resolve(scriptLink) ?: parentContext.resolve(scriptLink)
	}

fun Context.apply(scriptLink: ScriptLink) =
	resolve(scriptLink) ?: script(scriptLink)

fun Context.eval(script: Script) =
	liner().tokenizer().append(script).liner.script