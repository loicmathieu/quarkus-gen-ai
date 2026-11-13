package fr.loicmathieu.google.genai;

import com.google.genai.Chat;
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

    private Chat chat;

    // 3. Injection of the provided GenAI Client object
    @Inject
    Client client;

    // 4. Init the configuration with the system instruction and a chat session
    @PostConstruct
    void init() {
        var systemInstruction = Content.fromParts(Part.fromText(SYSTEM_INSTRUCTION));
        var config = GenerateContentConfig.builder().systemInstruction(systemInstruction).build();

        chat = client.chats.create("gemini-3.8-flash", config);
    }

    // 5. Generate a chat response on each call to /chat
    @GET
    public String chat(@QueryParam("prompt") String prompt) {
        return chat.sendMessage(prompt).text() + "\n";
    }
}
