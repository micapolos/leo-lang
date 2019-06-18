package leo5.gen.kotlin

import leo5.script.*

data class Root(val path: Path)

fun root(path: Path) = Root(path)

fun Root.append(appendable: Appendable, token: Token): Appending? = when (token) {
	is BeginToken -> when (token.begin.string) {
		"struct" -> appending(struct(path))
		else -> appending(root(path + token.begin.string))
	}
	is EndToken -> null
}

