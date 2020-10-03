package glints.test.service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public interface CommonService {
    String stringCheck(JsonNode request, String key);
    String generateIneteger(Integer length);
    JsonNode getInputStream(String fileName) throws IOException;
}
