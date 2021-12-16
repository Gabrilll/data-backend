package com.judicature.databackend.crawler;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OntologyConstructionLauncherTest {
    @Autowired
    OntologyConstructionLauncher ontologyConstructionLauncher;

    @Test
    public void testConstruct(){
        ontologyConstructionLauncher.construct("https://baike.baidu.com/item/2019%E6%96%B0%E5%9E%8B%E5%86%A0%E7%8A%B6%E7%97%85%E6%AF%92/24267858?fr=aladdin",10);
        ontologyConstructionLauncher.stopConstruction();
    }
}
