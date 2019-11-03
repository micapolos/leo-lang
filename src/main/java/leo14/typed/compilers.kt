package leo14.typed

import leo14.*
import leo14.lambda.Term

fun <T> compiler(compileLambda: Compile<Term<T>>, ret: Ret<Typed<T>>): Compiler =
	compiler { token ->
		when (token) {
			is BeginToken -> TODO()
			is EndToken -> TODO()
			is StringToken -> TODO()
			is NumberToken -> TODO()
		}
	}
