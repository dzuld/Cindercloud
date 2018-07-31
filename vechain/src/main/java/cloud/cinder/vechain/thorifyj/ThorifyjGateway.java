package cloud.cinder.vechain.thorifyj;

import org.springframework.stereotype.Component;

@Component
public class ThorifyjGateway {
    private Thorifyj thorifyj;

    public ThorifyjGateway(final Thorifyj thorifyj) {
        this.thorifyj = thorifyj;
    }

    public Thorifyj thorify() {
        return thorifyj;
    }
}
