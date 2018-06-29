package cloud.cinder.web.tools.service;

import cloud.cinder.web.credentials.repository.LeakedCredentialRepository;
import cloud.cinder.web.tools.service.dto.LeakedCredentialCheckResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeakedCredentialService {

    @Autowired
    private LeakedCredentialRepository leakedCredentialRepository;

    @Transactional(readOnly = true)
    public List<LeakedCredentialCheckResult> check(final List<String> strings) {
        return strings.stream()
                .filter(entry -> entry.length() == 64 || entry.length() == 66 || entry.length() == 42 || entry.length() == 40)
                .map(entry -> new LeakedCredentialCheckResult(entry, !leakedCredentialRepository.search(entry).isEmpty()))
                .collect(Collectors.toList());
    }
}
