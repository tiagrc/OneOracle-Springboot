package dev.tiago.screenMatch.services;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        OpenAiService service = new OpenAiService("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");

        CompletionRequest requisicao = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduza para o português o texto: " + texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var resposta = service.createCompletion(requisicao);
        return resposta.getChoices().get(0).getText();
    }
}