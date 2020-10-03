package glints.test.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

@Service
@Transactional
public class CommonServiceImpl implements CommonService {
    @Override
    public String stringCheck(JsonNode request, String key) {
        key = key.trim();
        String result = request.findValue(key) == null || request.findValue(key).asText().equalsIgnoreCase("null") ? "" : request.findValue(key).asText();
        return result;
    }

    @Override
    public String generateIneteger(Integer length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(1);
        for (int toPrepend = 0; toPrepend < length; toPrepend++) {
            sb.append(0);
        }
        String otp = String.format("%0" + length + "d", random.nextInt(Integer.parseInt(sb.toString())));
        return otp;
    }

    @Override
    public JsonNode getInputStream(String fileName) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        return new ObjectMapper().readTree(is);
    }
}
