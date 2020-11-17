package leo21.token.body

import leo14.lambda.arg
import leo21.compiled.Compiled
import leo21.compiled.getOrNull
import leo21.compiled.make
import leo21.type.Type
import leo21.type.onlyNameOrNull

data class Given(val type: Type)

val Type.given get() = Given(this)

fun Given.resolveOrNull(index: Int, param: Compiled): Compiled? =
	param.type.onlyNameOrNull?.let { name ->
		when (name) {
			"given" -> Compiled(arg(index), type).make("given")
			else -> Compiled(arg(index), type).make("given").getOrNull(name)
		}
	}
