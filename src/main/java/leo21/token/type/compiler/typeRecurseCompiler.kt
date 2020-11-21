package leo21.token.type.compiler

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token
import leo14.error
import leo15.dsl.*
import leo21.token.processor.processor

data class TypeRecurseCompiler(val parentTypeCompiler: TypeCompiler)

fun TypeRecurseCompiler.plus(token: Token): leo21.token.processor.Processor =
	when (token) {
		is LiteralToken -> error { expected { end } }
		is BeginToken -> error { expected { end } }
		is EndToken -> parentTypeCompiler.plusRecurse.processor
	}