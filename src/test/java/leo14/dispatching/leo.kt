package leo14.dispatching

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Token

data class Leo(
	val parentOrNull: LeoParent?,
	val resolver: Resolver,
	val endFn: () -> Leo)

data class LeoParent(
	val leo: Leo,
	val name: String)

val emptyLeo =
	Leo(null, emptyResolver) { error("end") }

fun Leo.write(token: Token): Leo =
	when (token) {
		is LiteralToken -> copy(resolver = resolver.literalFn(token.literal))
		is BeginToken -> resolver.beginFn.invoke(this, token.begin)
		is EndToken -> endFn()
	}
