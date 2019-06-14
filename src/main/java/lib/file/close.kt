package lib.file

import lib.File

data class Close(val file: File)

fun close(file: File) = Close(file)

