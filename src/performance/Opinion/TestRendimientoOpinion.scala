package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TestRendimientoOpinion extends Simulation {

	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.61 Safari/537.36")

	val headers_0 = Map(
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_2 = Map(
		"Accept" -> "image/webp,image/apng,image/*,*/*;q=0.8",
		"Proxy-Connection" -> "keep-alive")

	val headers_3 = Map(
		"Origin" -> "http://www.dp2.com",
		"Proxy-Connection" -> "keep-alive",
		"Upgrade-Insecure-Requests" -> "1")

	object Home {
		val home = exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(6)
	}

	object Login {
		val login = exec(http("Login")
			.get("/login")
			.headers(headers_0)
			.resources(http("request_2")
			.get("/login")
			.headers(headers_2)))
		.pause(7)
	}

	object Logged {
		val logged = exec(http("Logged")
			.post("/login")
			.headers(headers_3)
			.formParam("username", "owner1")
			.formParam("password", "0wn3r")
			.formParam("_csrf", "8bb2251b-f851-4368-abcb-e4ac06370f10"))
		.pause(7)
	}

	object Vets {
		val vets = exec(http("Vets")
			.get("/vets")
			.headers(headers_0))
		.pause(5)
	}

	object Create {
		val create = exec(http("Create")
			.get("/opinions/new/1")
			.headers(headers_0))
		.pause(9)
	}

	object Created {
		val created = exec(http("Created")
			.post("/opinions/new/1")
			.headers(headers_3)
			.formParam("comentary", "Soy una prueba de rendimiento")
			.formParam("puntuation", "1")
			.formParam("user", "")
			.formParam("vet", "")
			.formParam("date", "")
			.formParam("_csrf", "97ab0312-14eb-417e-b15f-e7b2883ed1dc"))
		.pause(9)
	}

	object List {
		val list = exec(http("List")
			.get("/opinions/list")
			.headers(headers_0))
		.pause(15)
	}

	object ListMine {
		val listmine = exec(http("ListMine")
			.get("/opinions/listMine")
			.headers(headers_0))
		.pause(11)
	}

	object Update {
		val update = exec(http("Update")
			.get("/opinions/edit/7")
			.headers(headers_0))
		.pause(11)
	}

	object Updated {
		val updated = exec(http("Updated")
			.post("/opinions/edit/7")
			.headers(headers_3)
			.formParam("comentary", "Soy una prueba de rendimiento, modificada")
			.formParam("puntuation", "4")
			.formParam("user", "owner1")
			.formParam("vet", "1")
			.formParam("date", "2020/05/25 18:33")
			.formParam("_csrf", "97ab0312-14eb-417e-b15f-e7b2883ed1dc"))
		.pause(14)
	}
	
	object Delete {
		val delete = exec(http("Delete")
			.get("/opinions/7/delete")
			.headers(headers_0))
		.pause(5)
	}

	val createOpScn = scenario("CreateOpinion").exec(Home.home, Login.login, Logged.logged, Vets.vets, Create.create, Created.created)
	
	val updateOpScn = scenario("UpdateOpinion").exec(Home.home, Login.login, Logged.logged, Vets.vets, List.list, ListMine.listmine, Update.update, Updated.updated)

	val deleteOpScn = scenario("DeleteOpinion").exec(Home.home, Login.login, Logged.logged, Vets.vets, List.list, ListMine.listmine, Delete.delete)

	setUp(
		createOpScn.inject(rampUsers(5000) during (100 seconds)),
		updateOpScn.inject(rampUsers(5000) during (100 seconds)),
		deleteOpScn.inject(rampUsers(5000) during (100 seconds))
	).protocols(httpProtocol)
	 .assertions(
		global.responseTime.max.lt(5000),
		global.responseTime.mean.lt(1000),
		global.successfulRequests.percent.gt(95)
	)
}