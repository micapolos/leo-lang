package leo5.gen.kotlin

import leo5.script.Path

data class Package(val path: Path)

fun package_(path: Path) = Package(path)
