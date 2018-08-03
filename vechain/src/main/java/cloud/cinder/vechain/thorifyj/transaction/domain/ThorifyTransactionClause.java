package cloud.cinder.vechain.thorifyj.transaction.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ThorifyTransactionClause {
    private String to;
    private String data;
    private String value;
}
