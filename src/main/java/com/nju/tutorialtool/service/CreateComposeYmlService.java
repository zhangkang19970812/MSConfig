package com.nju.tutorialtool.service;

import com.nju.tutorialtool.model.ServiceInfo;
import com.nju.tutorialtool.template.compose.ComposeYmlFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class CreateComposeYmlService {

    @Autowired
    private ServiceDirMapService serviceDirMapService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private UserService userService;

    /**
     * 创建docker-compose.yml文件
     * @throws Exception
     */
    public void createComposeYml() throws Exception {
        List<ServiceInfo> list = serviceDirMapService.getAllServices();
        List<com.nju.tutorialtool.model.Service> serviceList = new ArrayList<>();
        for (ServiceInfo serviceInfo : list) {
            com.nju.tutorialtool.model.Service service = null;
            if ("service".equals(serviceInfo.getType())) {
                service = new com.nju.tutorialtool.model.Service(serviceInfo.getServiceName(), "", false);
            }
            else if ("mysql".equals(serviceInfo.getType())) {
                service = new com.nju.tutorialtool.model.Service(serviceInfo.getServiceName(), "", true);
            }
            else if ("eureka".equals(serviceInfo.getType())){
                service = new com.nju.tutorialtool.model.Service(serviceInfo.getServiceName(), "8761", false);
            }
            else {
                service = new com.nju.tutorialtool.model.Service(serviceInfo.getServiceName(), "8040", false);
            }

            serviceList.add(service);
        }
        ComposeYmlFile composeYmlFile = new ComposeYmlFile(userService.getUserFolder(), serviceList);
        composeYmlFile.generate();
    }

    public String getProjectPath(ServiceInfo serviceInfo) {
        return userService.getUserFolder() + File.separator + serviceInfo.getFolderName();
    }

}
