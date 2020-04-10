package leo14.untyped.typed.lambda.core.java

import leo.java.lang.typeClassOrNull
import leo14.untyped.typed.loadClass

val runtimeJavaCore =
	JavaCore(
		typeClassFn = { typeClassOrNull },
		textClassFn = { (this as String).loadClass },
		classFieldFn = { (this as Class<*>).getField(it as String) })

