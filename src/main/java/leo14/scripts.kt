package leo14

import java.lang.Runtime.getRuntime

val leonardoScript =
	script(
		"leonardo" lineTo script(
			"version" lineTo script(
				literal("0.1")),
			"author" lineTo script(
				literal("Michał Pociecha-Łoś")),
			"operating" lineTo script(
				"system" lineTo script(
					"architecture" lineTo script(
						literal(System.getProperty("os.arch"))),
					"name" lineTo script(
						literal(System.getProperty("os.name"))),
					"version" lineTo script(
						literal(System.getProperty("os.version"))))),
			"runtime" lineTo script(
				"name" lineTo script(
					literal("Java")),
				"version" lineTo script(
					literal(System.getProperty("java.version"))),
				"vendor" lineTo script(
					literal(System.getProperty("java.vendor"))),
				"home" lineTo script(
					"folder" lineTo script(
						"name" lineTo script(
							literal(System.getProperty("java.home"))))),
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
				"name" lineTo script(literal(System.getProperty("user.name"))),
				"home" lineTo script(
					"folder" lineTo script(
						"name" lineTo script(
							literal(System.getProperty("user.home"))))),
				"working" lineTo script(
					"folder" lineTo script(
						"name" lineTo script(
							literal(System.getProperty("user.dir"))))))))