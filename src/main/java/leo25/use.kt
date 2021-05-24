package leo25

import leo13.*
import leo14.Script
import leo14.nameStackOrNull
import java.nio.file.Path

data class Use(val nameStackLink: StackLink<String>)

fun use(name: String, vararg names: String) = Use(stackLink(name, *names))

val Script.useOrNull: Use?
	get() =
		nameStackOrNull?.reverse?.linkOrNull?.let { nameStackLink ->
			Use(nameStackLink)
		}

val Use.path: Path
	get() =
		nameStackLink.updateTop { plus(".leo") }.reverse.run {
			Path.of(value, *stack.reverse.array)
		}