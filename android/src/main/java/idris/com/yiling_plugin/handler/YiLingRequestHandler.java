package idris.com.yiling_plugin.handler;

import io.flutter.plugin.common.PluginRegistry;

public class YiLingRequestHandler {

    private static PluginRegistry.Registrar registrar = null;

    public static void setRegistrar(PluginRegistry.Registrar reg) {
        YiLingRequestHandler.registrar = reg;
    }

    public PluginRegistry.Registrar getRegistrar() {
        return registrar;
    }
}
