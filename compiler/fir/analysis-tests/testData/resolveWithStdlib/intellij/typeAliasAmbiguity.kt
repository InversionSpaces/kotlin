// RUN_PIPELINE_TILL: BACKEND
typealias ServiceId = String
fun ServiceId(serviceId: String): ServiceId = serviceId
// FE 1.0 resolves this to function
val GaugeSpecTmsIntegrationServiceId = ServiceId("Gauge")

/* GENERATED_FIR_TAGS: functionDeclaration, propertyDeclaration, stringLiteral, typeAliasDeclaration */
