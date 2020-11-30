package zhanghui.project.frontclient;

import com.zhanghui.front.framework.boot.FrontBootstrap;

import java.util.ArrayList;
import java.util.List;

public class FrontClientApplication {

    public static void main(String[] args) {

        //        SaltFish.start(new FrontApplication(),System.getProperty("user.home")+"/webapp-conf/saltFish.properties",args);
//        SaltFish.start(new HZApplication(),System.getProperty("user.home")+"/webapp-conf/saltFish-test.properties",args);
        List<String> list = new ArrayList<String>();
        list.add(System.getProperty("user.home") + "/webapp-conf/saltFish.properties");
        list.add(System.getProperty("user.home") + "/webapp-conf/mock.properties");
//        list.add(System.getProperty("user.home")+"/webapp-conf/medicalCloud.properties");
//        list.add(URLDecoder.decode(FrontApplication.class.getClassLoader().getResource("application.properties").getPath(), "utf-8"));
//        list.add(URLDecoder.decode(FrontApplication.class.getClassLoader().getResource("mock.properties").getPath(), "utf-8"));
        FrontBootstrap.start(new FrontClientApplication(), list, args);
    }

}
