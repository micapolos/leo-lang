package leo25.natives

import leo.java.io.file
import leo25.Environment
import java.nio.file.Path

val nativeEnvironment: Environment
	get() =
		Environment(loadStringFn = { path ->
			try {
				Path.of(path).file.readText()
			} catch (e: Exception) {
				null
			}
		})