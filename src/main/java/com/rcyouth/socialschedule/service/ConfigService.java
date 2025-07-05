package com.rcyouth.socialschedule.service;

import com.rcyouth.socialschedule.config.Config;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    private static final String CONFIG_FILE_PATH = "config_rcy.json";

    public Config getGlobalConfig() {
        return new Config(CONFIG_FILE_PATH);
    }

    public Config updateGlobalConfig(Config newConfig) {
        newConfig.serialize();
        return newConfig;
    }
}
