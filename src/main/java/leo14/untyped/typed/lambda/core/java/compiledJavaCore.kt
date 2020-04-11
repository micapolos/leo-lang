package leo14.untyped.typed.lambda.core.java

val compiledJavaCore =
	JavaCore(
		bitIntFn = { "$this" },
		typeClassFn = { "$this.class" },
		textClassFn = { "leo14.MainKt.class.getClassLoader().loadClass($this)" },
		classFieldFn = { "$this.getField($it)" })

