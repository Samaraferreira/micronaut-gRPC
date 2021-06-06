package br.com.zup.edu.services

import br.com.zup.edu.FuncionarioRequest
import br.com.zup.edu.FuncionarioResponse
import br.com.zup.edu.FuncionarioServiceGrpc
import com.google.protobuf.Timestamp
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Singleton

@Singleton
class FuncionarioServiceImpl : FuncionarioServiceGrpc.FuncionarioServiceImplBase() {

    private val logger = LoggerFactory.getLogger(FreteServiceImpl::class.java)

    override fun cadastrar(request: FuncionarioRequest?, responseObserver: StreamObserver<FuncionarioResponse>?) {

        logger.info("Cadastrando funcionario para request: $request")

        var nome: String? = request!!.nome

        if (!request.hasField(FuncionarioRequest.getDescriptor().findFieldByName("nome"))) {
            nome = "[???]"
        }

        val instant = LocalDateTime.now().atZone(ZoneId.of("UTC")).toInstant()
        val data = Timestamp.newBuilder()
            .setSeconds(instant.epochSecond)
            .setNanos(instant.nano)
            .build()

        val response = FuncionarioResponse.newBuilder()
            .setNome(nome)
            .setDataCriacao(data)
            .build()

        responseObserver?.onNext(response)
        responseObserver?.onCompleted()
    }

}