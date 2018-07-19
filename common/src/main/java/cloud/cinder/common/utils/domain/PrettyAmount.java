package cloud.cinder.common.utils.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;

@Getter
@AllArgsConstructor
@Slf4j
public class PrettyAmount {

    final DecimalFormat decimalFormatter = new DecimalFormat("#0.0000");

    private String value;
    private String unit;

    public boolean isRounded() {
        return value != null && !value.equals(format());
    }

    private String format() {
        try {
            return decimalFormatter.format(Double.valueOf(value));
        } catch (final Exception ex) {
            log.error("Error during formatting of prettyamount", ex);
            return "";
        }
    }

    public String toString() {
        final StringBuilder returnValue = new StringBuilder();
        if (isRounded()) {
            returnValue.append("~");
        }
        return returnValue.append(format()).append(" ").append(unit).toString();
    }
}
