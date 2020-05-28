package dp2

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class TestPerformanceCreateAndDelete extends Simulation {
	
	val httpProtocol = http
		.baseUrl("http://www.dp2.com")
		.inferHtmlResources(BlackList(""".*.css""", """.*.js""", """.*.ico""", """.*.png"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("es-ES,es;q=0.9")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36")

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


	object Home{
		val home= exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(9)
		
	}


	object HomeDelete{
		val home= exec(http("Home")
			.get("/")
			.headers(headers_0))
		.pause(36)
	
	}

	object Login{
		val login= exec(
			http("Login")
				.get("/login")
				.headers(headers_0)
				.resources(http("request_2")
				.get("/login")
				.headers(headers_2))
				.check(css("input[name=_csrf]", "value").saveAs("stoken"))
		).pause(13)
		.exec(
			http("Logged")
				.post("/login")
				.headers(headers_3)
				.formParam("username", "vet1")
				.formParam("password", "v3t")
				.formParam("_csrf", "${stoken}")
		).pause(10)	
	}

	object FindOwners{
		val findOwners=exec(http("FindOwners")
			.get("/owners/find")
			.headers(headers_0))
		.pause(10)
	}
	object ListOwners{
		val listOwners=exec(http("ListOwners")
			.get("/owners?lastName=")
			.headers(headers_0))
		.pause(11)
		
	}
	object ShowOwner1{
		val showOwner1=exec(http("ShowOwner1")
			.get("/owners/1")
			.headers(headers_0))
		.pause(10)
	}
		
		object CreateDiseaseFormPetOwner1{
		val createDiseaseFormPetOwner1=exec(http("CreateDiseaseFormPetOwner1")
			.get("/diseases/new/1?diseaseId=")
			.headers(headers_0)
			.check(css("input[name=_csrf]", "value").saveAs("stoken")))
		.pause(28)
			.exec(http("DiseaseCreated")
				.post("/diseases/new/1?diseaseId=")
				.headers(headers_3)
				.formParam("pet_id", "1")
				.formParam("symptoms", "Nueva enfermedad")
				.formParam("severity", "LOW")
				.formParam("cure", "Paracetamol")
				.formParam("_csrf", "${stoken}"))
	.pause(26)
	}

	
		object ListDiseases{
		val listDiseases=exec(http("ListDiseases")
			.get("/diseases/diseasesList")
			.headers(headers_0))
		.pause(46)
		
	}

		object DeleteNewDisease{
		
		
		
		val deleteNewDisease=repeat(3000,"id"){
	
  
		exec(http("DeleteNewDisease")
			.get("/diseases/delete/${id}")
			.headers(headers_0))
		}
		
	}


	val createDiseaseScn = scenario("TestPerformanceCreateDisease").exec(Home.home,
															Login.login,
															FindOwners.findOwners,
															ListOwners.listOwners,
															ShowOwner1.showOwner1,
															CreateDiseaseFormPetOwner1.createDiseaseFormPetOwner1)

	val deleteDiseaseScn = scenario("TestPerformanceDeleteDisease").exec(HomeDelete.home,
															Login.login,
															ListDiseases.listDiseases,
															DeleteNewDisease.deleteNewDisease)
		

	setUp(
		createDiseaseScn.inject(rampUsers(3000) during (100 seconds)),
		deleteDiseaseScn.inject(rampUsers(1) during (100 seconds))
	).protocols(httpProtocol)
     .assertions(
        forAll.failedRequests.percent.lte(5),    
        global.responseTime.mean.lt(1200),
        global.successfulRequests.percent.gt(97)
     )
}