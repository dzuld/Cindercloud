package cloud.cinder.web.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class MiningStatistics {

    private Map<String, Long> data;
    private long total;

}
