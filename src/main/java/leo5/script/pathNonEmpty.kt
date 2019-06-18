package leo5.script

data class PathNonEmpty(val path: Path, val string: String)

fun nonEmpty(path: Path, string: String) = PathNonEmpty(path, string)