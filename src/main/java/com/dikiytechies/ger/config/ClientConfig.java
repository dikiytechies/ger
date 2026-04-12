package com.dikiytechies.ger.config;

import com.dikiytechies.ger.GerConfig;

public class ClientConfig {
    // ts doesn't work, having no idea how to transfer server sounds to client side
    public static boolean isClassicShoutEnabled() {
        return GerConfig.CLIENT.isClassicShoutEnabled.get();
    }
}
