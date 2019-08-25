package leo13.script

import leo.base.notNullIf
import leo13.*
import leo13.script.parser.TokenError
import leo13.script.parser.error
import leo9.fold
import leo9.reverse

data class Compiler(
	val head: CompiledHead,
	val errorOrNull: TokenError?) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "compiler" lineTo script(
		head.asScriptLine,
		errorOrNull.orNullAsScriptLine("error"))
}

fun compiler() = Compiler(head(compiledOpeners(), metable()), null)
fun compiler(head: CompiledHead, errorOrNull: TokenError?) = Compiler(head, errorOrNull)
fun compiler(code: String) = compiler().push(code)

val CompiledHead.compiler get() = compiler(this, null)

val Compiler.successHeadOrNull get() = notNullIf(errorOrNull == null) { head }

fun Compiler.push(token: Token): Compiler =
	if (errorOrNull != null) this
	else head.pushCompiler(token)

fun Compiler.push(code: String): Compiler =
	fold(code.unsafeTokens.reverse) { push(it) }

fun Compiler.set(error: TokenError): Compiler =
	compiler(head, error)

fun Compiler.set(head: CompiledHead): Compiler =
	compiler(head, errorOrNull)

fun CompiledHead.pushCompiler(token: Token): Compiler =
	plus(token)
		?.let { compiler(it, null) }
		?: compiler(this, error(token))
