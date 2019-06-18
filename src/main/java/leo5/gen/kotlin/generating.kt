package leo5.gen.kotlin

import leo.base.empty
import leo5.base.Writer
import leo5.base.writer
import leo5.script.*

sealed class Generating

data class PackageGenerating(val package_: Package) : Generating()
data class AppendingGenerating(val appending: Appending) : Generating()

fun generating(package_: Package): Generating = PackageGenerating(package_)
fun generating(appending: Appending): Generating = AppendingGenerating(appending)

fun Generating.write(token: Token) = when (this) {
	is PackageGenerating -> when (token) {
		is BeginToken -> when (token.begin.string) {
			"struct" -> generating(appending(struct(package_.path)))
			else -> generating(package_(package_.path + token.begin.string))
		}
		is EndToken -> TODO()
	}
	is AppendingGenerating -> generating(appending.append(token))
}

fun generate(fn: Writer<Token>.() -> Unit) {
	val generating = generating(package_(path(empty)))
	writer<Token> { token ->
		generating.write(token)
	}.fn()
}

leo
word contains super super kotlin string
script
contains one of
nothing
it
super super script
and super super line
line contains
super super word
and super super super script
path
switch
empty
non empty super path
