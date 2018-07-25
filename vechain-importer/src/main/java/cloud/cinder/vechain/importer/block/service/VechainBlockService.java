package cloud.cinder.vechain.importer.block.service;

import cloud.cinder.vechain.thorifyj.ThorifyjGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class VechainBlockService {

    @Autowired
    private ThorifyjGateway thorifyjGateway;

    @PostConstruct
    public void init() {
        System.out.println("lol");
    }
}
