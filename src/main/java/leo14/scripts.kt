package leo14

import leo.binary.*
import java.lang.Runtime.getRuntime
import java.lang.System.getProperty

val Bit.scriptLine
	get() =
		"bit" lineTo script(if (isZero) "zero" else "one")

val Byte.scriptLine
	get() =
		"byte" lineTo script(
			bit7.scriptLine,
			bit6.scriptLine,
			bit5.scriptLine,
			bit4.scriptLine,
			bit3.scriptLine,
			bit2.scriptLine,
			bit1.scriptLine,
			bit0.scriptLine)

val leonardoScript =
	script(
		"leonardo" lineTo script(
			"version" lineTo script(
				literal("0.1")),
			"author" lineTo script(
				literal("Michał Pociecha-Łoś")),
			"operating" lineTo script(
				"system" lineTo script(
					"name" lineTo script(
						literal(getProperty("os.name"))),
					"version" lineTo script(
						literal(getProperty("os.version"))),
					"architecture" lineTo script(
						literal(getProperty("os.arch"))))),
			"runtime" lineTo script(
				"name" lineTo script(
					literal("Java")),
				"version" lineTo script(
					literal(getProperty("java.version"))),
				"vendor" lineTo script(
					literal(getProperty("java.vendor"))),
				"home" lineTo script(
					"folder" lineTo script(
						"name" lineTo script(
							literal(getProperty("java.home"))))),
				"memory" lineTo script(
					"total" lineTo script(
						"byte" lineTo script(
							"count" lineTo script(literal(getRuntime().totalMemory())))),
					"free" lineTo script(
						"byte" lineTo script(
							"count" lineTo script(
								literal(getRuntime().freeMemory()))))),
				"processor" lineTo script(
					"count" lineTo script(
						literal(getRuntime().availableProcessors())))),
			"user" lineTo script(
				"name" lineTo script(literal(getProperty("user.name"))),
				"home" lineTo script(
					"folder" lineTo script(
						"name" lineTo script(
							literal(getProperty("user.home"))))),
				"working" lineTo script(
					"folder" lineTo script(
						"name" lineTo script(
							literal(getProperty("user.dir"))))))))