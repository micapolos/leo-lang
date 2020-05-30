package leo16.native

import leo.base.println
import leo13.array
import leo13.linkOrNull
import leo13.map
import leo13.stack
import leo14.untyped.typed.loadClass
import leo16.accessOrNull
import leo16.contentOrNull
import leo16.does
import leo16.field
import leo16.getOrNull
import leo16.invoke
import leo16.names.*
import leo16.nativeField
import leo16.nativeValue
import leo16.printed
import leo16.sentenceOrNull
import leo16.stackOrNull
import leo16.theNativeOrNull
import leo16.value
import leo16.valueValue
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method

fun String.definition(class_: Class<*>) =
	value(_class(this())).does {
		_class(class_.nativeField).value
	}

val nativeObjectClassDefinition =
	value(_class(_object(_native()))).does {
		val class_ = this
			.getOrNull(_class)!!
			.getOrNull(_object)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value!!
			.javaClass
		_class(class_.nativeField).value
	}

val nullNativeDefinition =
	value(_native(_null())).does {
		null.nativeValue
	}

val nativeBooleanDefinition =
	value(_boolean(_native())).does {
		this
			.getOrNull(_boolean)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.run { value as Boolean }
			.field.value
	}

val nameClassDefinition =
	value(_class(_name(_text(_native())))).does {
		val name = this
			.getOrNull(_class)!!
			.getOrNull(_name)!!
			.getOrNull(_text)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as String
		_class(name.loadClass.nativeField).value
	}

val classFieldDefinition =
	value(
		_class(_anything()),
		_field(_name(_text(_native())))
	).does {
		val class_ = this
			.getOrNull(_class)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Class<*>
		val name = this
			.getOrNull(_field)!!
			.getOrNull(_name)!!
			.getOrNull(_text)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as String
		_field(class_.getField(name).nativeField).value
	}

val fieldGetDefinition =
	value(_get(_field(_native()))).does {
		val field = this
			.getOrNull(_get)!!
			.getOrNull(_field)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Field
		field.get(null).nativeValue
	}

val nativeGetFieldDefinition =
	value(
		_native(),
		_get(_field(_native()))
	).does {
		val object_ = this
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value
		val field = this
			.getOrNull(_get)!!
			.getOrNull(_field)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Field
		field.get(object_).nativeValue
	}

val classConstructorDefinition =
	value(
		_class(_native()),
		_constructor(_parameter(_list(_anything())))
	).does {
		val class_ = this
			.getOrNull(_class)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Class<*>
		val classes = this
			.getOrNull(_constructor)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map {
				this
					.accessOrNull(_class)!!
					.getOrNull(_native)!!
					.theNativeOrNull!!
					.value as Class<*>
			}
			.array
		_constructor(class_.getConstructor(*classes).nativeField).value
	}

val constructorInvokeDefinition =
	value(
		_constructor(_native()),
		_invoke(_parameter(_list(_anything())))
	).does {
		val constructor = this
			.getOrNull(_constructor)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Constructor<*>
		val args = this
			.getOrNull(_invoke)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		constructor.newInstance(*args).nativeValue
	}

val classMethodDefinition =
	value(
		_class(_native()),
		_method(
			_name(_text(_native())),
			_parameter(_list(_anything()))
		)
	).does {
		val class_ = this
			.getOrNull(_class)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Class<*>
		val name = this
			.getOrNull(_method)!!
			.getOrNull(_name)!!
			.getOrNull(_text)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as String
		val classes = this
			.getOrNull(_method)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map {
				this
					.accessOrNull(_class)!!
					.getOrNull(_native)!!
					.theNativeOrNull!!
					.value as Class<*>
			}
			.array
		_method(class_.getMethod(name, *classes).nativeField).value
	}

val methodInvokeDefinition =
	value(
		_method(_native()),
		_invoke(_parameter(_list(_anything())))
	).does {
		val method = this
			.getOrNull(_method)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(_invoke)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		method.invoke(null, *args).nativeValue
	}

val nativeInvokeMethodDefinition =
	value(
		_native(),
		_invoke(
			_method(_native()),
			_parameter(_list(_anything())))
	).does {
		val object_ = this
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value
		val method = this
			.getOrNull(_invoke)!!
			.getOrNull(_method)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Method
		val args = this
			.getOrNull(_invoke)!!
			.getOrNull(_parameter)!!
			.getOrNull(_list)!!
			.stackOrNull!!
			.map { theNativeOrNull!!.value }
			.array
		method.invoke(object_, *args).nativeValue
	}

val arrayStackDefinition =
	value(_list(_array(_native()))).does {
		val array = this
			.getOrNull(_list)!!
			.getOrNull(_array)!!
			.getOrNull(_native)!!
			.theNativeOrNull!!
			.value as Array<*>
		stack(*array).map { value(nativeField) }.valueValue
	}

val printingDefinition =
	value(_printing(_anything())).does {
		val content = this
			.getOrNull(_printing)!!
			.contentOrNull!!
		content.also { content.printed.println }
	}

val leoHeadDefinition =
	value(_head(_anything())).does {
		this
			.getOrNull(_head)!!
			.contentOrNull!!
			.fieldStack
			.linkOrNull
			?.value
			?.value
			?: this.getOrNull(_head)!!
	}

val leoTailDefinition =
	value(_tail(_anything())).does {
		this
			.getOrNull(_tail)!!
			.contentOrNull!!
			.fieldStack
			.linkOrNull
			?.stack
			?.value
			?: this.getOrNull(_tail)!!
	}

val leoOpDefinition =
	value(_op(_anything())).does {
		this
			.getOrNull(_op)!!
			.contentOrNull!!
			.fieldStack
			.linkOrNull
			?.value
			?.sentenceOrNull
			?.word
			?.invoke()
			?.value
			?: this.getOrNull(_op)!!
	}
