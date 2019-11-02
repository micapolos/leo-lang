package leo13.lambda.java

import java.io.File

fun String.run(): String {
	val tmpFile = File.createTempFile("Main", ".tmp")
	tmpFile.deleteOnExit()
	//println(tmpFile)

	val dir = File.createTempFile("tmp", ".tmp").parentFile
	//println(dir)

	val javaFile = dir.resolve("Main.java")
	javaFile.deleteOnExit()
	//println(javaFile)

	javaFile.writeText(this)

	val classFile = dir.resolve("Main.class")
	classFile.deleteOnExit()
	//println(classFile)

	Runtime.getRuntime().exec("javac $javaFile").waitFor()

	val execProcess = Runtime.getRuntime().exec("java -cp $dir Main")
	return execProcess.inputStream.reader().readText()
}

val JavaExpr.eval
	get() =
		printCode.mainCode.run()
