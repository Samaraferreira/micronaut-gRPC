package br.com.zup.edu.services

import br.com.zup.edu.CalculaFreteRequest
import br.com.zup.edu.CalculaFreteResponse
import br.com.zup.edu.ErrorDetails
import br.com.zup.edu.FreteServiceGrpc
import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.protobuf.StatusProto
import io.grpc.Status
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FreteServiceImpl : FreteServiceGrpc.FreteServiceImplBase() {

    private val logger = LoggerFactory.getLogger(FreteServiceImpl::class.java)

    override fun calculaFrete(request: CalculaFreteRequest?, responseObserver: StreamObserver<CalculaFreteResponse>?) {

        logger.info("Calculando frete para request: $request")

        val cep: String? = request?.cep

        if (cep == null || cep.isBlank()) {
            val error = Status.INVALID_ARGUMENT
                        .withDescription("O cep deve ser informado")
                        .asRuntimeException()

            responseObserver?.onError(error)
        }

        if (!cep!!.matches("[0-9]{5}-[0-9]{3}".toRegex())) {
            val error = Status.INVALID_ARGUMENT
                .withDescription("O cep informado é inválido")
                .augmentDescription("formato esperado deve ser 00000-000")
                .asRuntimeException()

            responseObserver?.onError(error)
        }

        if (cep.endsWith("333")) {
            val statusProto = com.google.rpc.Status.newBuilder()
                                    .setCode(Code.PERMISSION_DENIED.number)
                                    .setMessage("usuário não tem permissão para acessar esse recurso")
                                    .addDetails(Any.pack(ErrorDetails.newBuilder()
                                        .setCode(401)
                                        .setMessage("token expirado")
                                        .build()))
                                    .build()

            val e = StatusProto.toStatusRuntimeException(statusProto)
            responseObserver?.onError(e)
        }

        var valor = 0.0
        try {
            valor = Random.nextDouble(from = 0.0, until = 140.0)
            if (valor > 100) {
                throw IllegalStateException("Erro inesperado ao executar lógica de negócio")
            }
        } catch (e: Exception) {
            responseObserver?.onError(Status.INTERNAL
                                            .withDescription(e.message)
                                            .withCause(e) // anexado ao status, mas não enviado ao cliente
                                            .asRuntimeException()
            )
        }

        val response = CalculaFreteResponse.newBuilder()
            .setValor(valor)
            .setCep(cep)
            .build()

        logger.info("Frete calculado: $response")

        responseObserver!!.onNext(response)
        responseObserver.onCompleted()
    }
}