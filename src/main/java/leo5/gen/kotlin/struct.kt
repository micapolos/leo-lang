package leo5.gen.kotlin

import leo5.script.Path

data class Struct(val path: Path)

fun struct(path: Path) = Struct(path)
