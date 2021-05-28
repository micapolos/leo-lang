package leo25.natives

import leo.java.io.file
import leo13.array
import leo13.reverse
import leo13.updateTop
import leo25.Use
import java.nio.file.Path

val Use.path: Path
	get() =
		nameStackLink.updateTop { plus(".leo") }.reverse.run {
			Path.of(value, *stack.reverse.array)
		}

val Use.loadString: String
	get() =
		path.file.readText()
