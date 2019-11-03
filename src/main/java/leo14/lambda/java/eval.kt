package leo14.lambda.java

import leo.base.ifNotNull
import leo.java.lang.exec
import leo13.push
import leo13.stack
import leo13.toList
import leo14.Script
import leo14.compile
import java.io.File
import java.io.File.createTempFile

fun String.mainCodeEval(): String {
	val tmpFile = createTempFile("just-to-get-temp-dir", ".tmp")
	tmpFile.deleteOnExit()
	val dir = tmpFile.parentFile

	val className = "Main"

	val javaFile = dir.resolve("$className.java")
	javaFile.deleteOnExit()
	javaFile.writeText(this)

	val classFile = dir.resolve("$className.class")
	classFile.deleteOnExit()

	javaCompile(javaFile)

	return javaRun(className, dir)
}

fun javaCompile(file: File) =
	exec("javac", "$file")

fun javaRun(className: String, classPathFile: File? = null): String =
	stack<String>()
		.push("java")
		.ifNotNull(classPathFile) { push("-cp").push("$it") }
		.push(className)
		.toList()
		.toTypedArray()
		.let { exec(*it) }

val Value.eval
	get() =
		printCode.mainCode.mainCodeEval()

val Script.eval
	get() =
		compiler.compile<Value>(this).eval
