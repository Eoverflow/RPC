package com.liws.rpc.ipc;

import com.liws.rpc.ServiceRegistry;
import com.liws.rpc.LocalServiceRegistryCenter;

/**
 * Created by liweisheng on 16/9/20.
 */
public class LocalServiceInvoke implements ServiceInvoke {
    private static LocalServiceRegistryCenter registryCenter = LocalServiceRegistryCenter.getInstance();

    @Override
    public Response invoke(Request request) {
        String serviceName = request.getServiceName();
        String methodSignatureName = request.getMethodSignatureName();
        String interfaceName = request.getInterfaceCanonicalName();
        String[] argsJSON = (String[])request.getArgsJSONAsArray();

        ServiceRegistry registry = registryCenter.getService(serviceName);

        String ret = null;

        try {
            ret = registry.invokeMethod(methodSignatureName,interfaceName,argsJSON);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.successReturn(ret,request.getUuid());
    }
}
