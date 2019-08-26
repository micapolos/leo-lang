package leo13

import leo13.compiler.compile
import leo13.script.code
import leo13.script.parse
import leo13.token.tokenize
import leo13.value.evaluate
import leo13.value.scriptOrNull

val String.eval: String?
	get() =
		tokenize?.parse?.compile?.expr?.evaluate?.scriptOrNull?.code