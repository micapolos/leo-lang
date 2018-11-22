package leo.java.nio.file

import leo.Selector
import leo.Word
import leo.base.*
import java.nio.file.Path
import java.nio.file.Paths

val emptyPath = Paths.get("")

val Word.path: Path
	get() =
		Paths.get(string)

val Selector.path: Path
	get() =
		emptyPath.fold(wordStackOrNull?.stream?.reverse?.map(Word::path), Path::resolve)

val Selector.leoPath: Path
	get() =
		path.let { path ->
			if (path == emptyPath) Paths.get("leo")
			else path.resolveSibling("${path.last()}.leo")
		}
