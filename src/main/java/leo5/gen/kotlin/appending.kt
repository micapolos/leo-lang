package leo5.gen.kotlin

import leo.base.empty
import leo.base.write
import leo5.base.Writer
import leo5.script.Path
import leo5.script.Token
import java.io.File

sealed class Appending

data class RootAppending(val root: Root) : Appending()
data class StructAppending(val struct: Struct) : Appending()

fun appending(root: Root): Appending = RootAppending(root)
fun appending(struct: Struct): Appending = StructAppending(struct)

fun Appending.append(token: Token): Appending = when (this) {
	is RootAppending -> file.appendText(token)
	is StructAppending -> struct.appendText(token)
}

fun File.write(path: Path, fn: Writer<Token>.() -> Unit) {
	write {
		val appendable = this
		var appending = root(leo5.script.path(empty))
		leo5.base.writer<Token> { token ->
			appending.append(token)
		}
	}
}
