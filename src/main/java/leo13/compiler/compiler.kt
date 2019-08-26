package leo13.compiler

import leo.base.notNullIf
import leo13.script.*
import leo13.token.Token
import leo13.token.unsafeTokens
import leo13.type.Typed
import leo9.fold
import leo9.reverse

data class Compiler(
	val errorOrNull: TokenError?,
	val head: CompiledHead) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "compiler"
	override val scriptableBody get() = script(head.scriptableLine, errorOrNull.orNullAsScriptLine("error"))
}

fun compiler() = Compiler(null, head(compiledOpeners(), metable()))
fun compiler(errorOrNull: TokenError?, head: CompiledHead) = Compiler(errorOrNull, head)
fun compiler(code: String) = compiler().push(code)

fun compiler(typed: Typed) =
	compiler(
		null,
		head(
			compiledOpeners(),
			metable(
				false,
				compiled(
					context(),
					typed))))

val CompiledHead.compiler get() = compiler(null, this)

val Compiler.successHeadOrNull get() = notNullIf(errorOrNull == null) { head }

fun Compiler.push(token: Token): Compiler =
	if (errorOrNull != null) this
	else head.pushCompiler(token)

fun Compiler.push(code: String): Compiler =
	fold(code.unsafeTokens.reverse) { push(it) }

fun Compiler.set(error: TokenError): Compiler =
	compiler(error, head)

fun Compiler.set(head: CompiledHead): Compiler =
	compiler(errorOrNull, head)

fun CompiledHead.pushCompiler(token: Token): Compiler =
	plus(token)
		?.let { compiler(null, it) }
		?: compiler(error(token), this)
