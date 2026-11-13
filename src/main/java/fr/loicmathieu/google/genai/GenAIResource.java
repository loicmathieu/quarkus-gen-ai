package fr.loicmathieu.google.genai;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Part;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

// 1. Standard REST endpoint available on /chat
@Path("/chat")
public class GenAIResource {
    // 2. System message telling the LLM it is a chatbot
    private static final String SYSTEM_INSTRUCTION = """
        You are a chatbot named Loïc.
        Don't pretend you know everything but be helpful with a touch of humour.""";

    private GenerateContentConfig config;

    // 3. Injection of the provided GenAI Client object
    @Inject
    Client client;

    // 4. Init the configuration with the system instruction
    @PostConstruct
    void init() {
        var systemInstruction = Content.fromParts(Part.fromText(SYSTEM_INSTRUCTION));
        config = GenerateContentConfig.builder().systemInstruction(systemInstruction).build();
    }

    // 5. Generate a chat response on each call to /chat
    @GET
    public String chat(@QueryParam("prompt") String prompt) {
        return client.models.generateContent("gemini-3.8-flash", prompt, config).text() + "\n";
    }
}
