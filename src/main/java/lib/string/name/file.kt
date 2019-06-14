package lib.string.name

import lib.string.Name

data class File(val name: Name)

fun file(name: Name) = File(name)
