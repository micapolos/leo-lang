package leo13.lambda.java

import leo.java.lang.exec
import java.io.File.createTempFile

fun String.run(): String {
	val tmpFile = createTempFile("just-to-get-temp-dir", ".tmp")
	tmpFile.deleteOnExit()
	val dir = tmpFile.parentFile

	val className = "Main"

	val javaFile = dir.resolve("$className.java")
	javaFile.deleteOnExit()
	javaFile.writeText(this)

	val classFile = dir.resolve("$className.class")
	classFile.deleteOnExit()

	exec("javac", "$javaFile")

	return exec("java", "-cp", "$dir", className)
}

val JavaExpr.eval
	get() =
		printCode.mainCode.run()
