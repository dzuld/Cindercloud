package cloud.cinder.web.statistics;

import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.web.block.repository.BlockRepository;
import cloud.cinder.web.statistics.dto.MiningStatistics;
import cloud.cinder.web.transaction.repository.TransactionRepository;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StatisticsService {

    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public MiningStatistics miningStatistics() {
        Stream<Block> allBlocksSince = blockRepository.findAllBlocksSince(Date.from(LocalDateTime.now().minus(24, ChronoUnit.HOURS).atOffset(ZoneOffset.UTC).toInstant()));


        final Map<String, Long> addressWithBlockAmounts = allBlocksSince.map(Block::getMinedBy)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return new MiningStatistics(addressWithBlockAmounts,
                addressWithBlockAmounts.values().stream().mapToLong(x -> x).sum());
    }

    @Transactional(readOnly = true)
    public double getAverageDifficulty(final TemporalAmount since) {
        final OffsetDateTime dateSince = LocalDateTime.now().atOffset(ZoneOffset.UTC).minus(since);
        final Stream<Block> allSince = blockRepository.findAllBlocksSince(Date.from(dateSince.toInstant()));
        return allSince
                .map(Block::getDifficulty)
                .mapToDouble(BigInteger::doubleValue)
                .average().orElse(0);
    }

    @Transactional(readOnly = true)
    public double getAverageTransactions(final TemporalAmount since, final TemporalUnit per) {
        final OffsetDateTime dateSince = LocalDateTime.now().atOffset(ZoneOffset.UTC).minus(since);
        final Stream<Transaction> allSince = transactionRepository.findAllTransactionsSince(Date.from(dateSince.toInstant()));
        return allSince.count() / since.get(per);
    }

    @Transactional(readOnly = true)
    public double getAverageBlockTime(final TemporalAmount since, final TemporalUnit per) {
        final OffsetDateTime dateSince = LocalDateTime.now().atOffset(ZoneOffset.UTC).minus(since);
        Stream<Block> allSince = blockRepository.findAllBlocksSince(Date.from(dateSince.toInstant()));
        return since.get(per) / allSince.count();
    }
}
