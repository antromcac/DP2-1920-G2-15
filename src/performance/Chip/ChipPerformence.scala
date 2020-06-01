package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class ChipPerformence extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.png""", """.*.css""", """.*.ico""", """.*.js"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,"+
						"image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9,en;q=0.8")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)"+
						 "Chrome/81.0.4044.138 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_1 = Map(	
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_6 = Map(
		"Proxy-Connection" -> "Keep-Alive",
		"User-Agent" -> "Microsoft-WNS/10.0")

	val headers_11 = Map(
		"A-IM" -> "x-bm,gzip",
		"Proxy-Connection" -> "keep-alive")

    val uri1 = "http://clientservices.googleapis.com/chrome-variations/seed"

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(9)
	}

	object Login {
		var login = exec(
			http("Login")
				.get("/login")
				.headers(headers_0)
				.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(18)
		.exec(
			http("Logged")
				.post("/login")
				.headers(headers_3)
				.formParam("username", "vet1")
				.formParam("password", "v3t")
				.formParam("_csrf", "${stoken}")
		).pause(9)
	}

	object FindOwner {
		val findOwner = exec(http("FindOwner")
			.get("/owners/find")
			.headers(headers_0))
		.pause(16)
	}

	object ListOwners {
		val listOwners = exec(http("ListOwners")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(8)
	}

	object Owner {
		val owner = exec(http("Owner")
			.get("/owners/5")
			.headers(headers_0))
		.pause(12)
	}

	object AddChip {
		val addChip = exec(
			http("AddChip")
				.get("/owners/5/pets/6/chips/new")
				.headers(headers_0)
				.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			).pause(19)
		.exec(
			http("ChipAdded")
				.post("/owners/5/pets/6/chips/new")
				.headers(headers_3)
				.formParam("pet_id", "6")
				.formParam("serialNumber", "123")
				.formParam("model", "model123")
				.formParam("geolocatable", "true")
				.formParam("_csrf", "${stoken}")
			).pause(24)
	}

	object UpdateChip {
		val updateChip = exec(
			http("UpdateChip")
				.get("/owners/1/pets/1/chips/1/edit")
				.headers(headers_0)
				.check(css("input[name=_csrf]", "value").saveAs("stoken"))
			).pause(28)
		.exec(
			http("ChipUpdated")
				.post("/owners/5/pets/6/chips/4/edit")
				.headers(headers_1)
				.formParam("pet_id", "")
				.formParam("serialNumber", "12")
				.formParam("model", "model12")
				.formParam("geolocatable", "false")
				.formParam("_csrf", "${stoken}")
			).pause(2)
	}

	object ShowChip {
		val showChip = exec(http("ShowChip")
			.get("/owners/1/pets/1/chips/1")
			.headers(headers_0))
		.pause(8)
	}

	object ChipDeleted {
		val chipDeleted = exec(http("ChipDeleted")
			.get("/owners/2/pets/2/chips/2/delete")
			.headers(headers_0))
		.pause(13)
	}

	val createScn = scenario("Create").exec(Home.home,
											Login.login,
											FindOwner.findOwner,
											ListOwners.listOwners,
											Owner.owner,
											AddChip.addChip)

	val updateScn = scenario("Update").exec(Home.home,
											Login.login,
											FindOwner.findOwner,
											ListOwners.listOwners,
											Owner.owner,
											UpdateChip.updateChip)

	val showScn = scenario("Show").exec(Home.home,
											Login.login,
											FindOwner.findOwner,
											ListOwners.listOwners,
											Owner.owner,
											ShowChip.showChip)

											
	val deleteScn = scenario("Delete").exec(Home.home,
											Login.login,
											FindOwner.findOwner,
											ListOwners.listOwners,
											Owner.owner,
											ChipDeleted.chipDeleted)
																						

	setUp(
		createScn.inject(rampUsers(1000) during (10 seconds)),
		updateScn.inject(rampUsers(1000) during (10 seconds)),
		showScn.inject(rampUsers(1000) during (10 seconds)),
		deleteScn.inject(rampUsers(1000) during (10 seconds))
	).protocols(httpProtocol)
	.assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(5000),
		global.successfulRequests.percent.gt(95)
	)
}