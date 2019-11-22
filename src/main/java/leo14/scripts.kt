package leo14

import java.lang.Runtime.getRuntime
import java.lang.System.getProperty

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
							"count" lineTo script(literal(getRuntime().totalMemory().toInt())))),
					"free" lineTo script(
						"byte" lineTo script(
							"count" lineTo script(
								literal(getRuntime().freeMemory().toInt()))))),
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