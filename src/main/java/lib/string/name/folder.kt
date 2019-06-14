package lib.string.name

import lib.string.Name

data class Folder(val name: Name)

fun folder(name: Name) = Folder(name)
