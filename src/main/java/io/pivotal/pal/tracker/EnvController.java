package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EnvController {

    private String _port;
    private String _memoryLimit;
    private String _cfInstanceIndex;
    private String _cfInstanceAddr;

    public EnvController(
            @Value("${port:NOT SET}") String port,
            @Value("${memory.limit:NOT SET}") String memoryLimit,
            @Value("${cf.instance.index:NOT SET}") String cfInstanceIndex,
            @Value("${cf.instance.addr:NOT SET}") String cfInstanceAddr
    ) {
        _port = port;
        _memoryLimit = memoryLimit;
        _cfInstanceIndex = cfInstanceIndex;
        _cfInstanceAddr = cfInstanceAddr;
    }

    @GetMapping("/env")
    public Map<String, String> getEnv() {

        Map<String, String> env = new HashMap<>();

        env.put("PORT", _port);
        env.put("MEMORY_LIMIT", _memoryLimit);
        env.put("CF_INSTANCE_INDEX", _cfInstanceIndex);
        env.put("CF_INSTANCE_ADDR", _cfInstanceAddr);

        return env;
    }
}
