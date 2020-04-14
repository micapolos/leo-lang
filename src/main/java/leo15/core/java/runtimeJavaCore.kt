package leo15.core.java

import leo.java.lang.typeClassOrNull
import leo14.untyped.typed.loadClass

val runtimeJavaCore =
	JavaCore(
		bitIntFn = { this },
		typeClassFn = { typeClassOrNull },
		textClassFn = { (this as String).loadClass },
		classFieldFn = { (this as Class<*>).getField(it as String) })

