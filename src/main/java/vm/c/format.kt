package vm.c

import leo.java.io.inTempFile
import leo.java.lang.exec

val String.clangFormat: String
	get() =
		inTempFile(extension = "c") { file ->
			exec("clang-format", "${file.absolutePath}")
		}
