package com.dikiytechies.ger.config;

import com.dikiytechies.ger.GerConfig;

public class GlobalConfig {
    public static boolean isTemporal(boolean clientSide) {
        return GerConfig.getCommonConfigInstance(clientSide).isTemporal.get();
    }

    public static int getResolveDuration(boolean clientSide) {
        return GerConfig.getCommonConfigInstance(clientSide).resolveDuration.get();
    }

    public static int getResolveAmplifier(boolean clientSide) {
        return GerConfig.getCommonConfigInstance(clientSide).resolveAmplifier.get();
    }
}
