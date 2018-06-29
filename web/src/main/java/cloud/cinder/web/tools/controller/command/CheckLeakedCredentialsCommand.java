package cloud.cinder.web.tools.controller.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckLeakedCredentialsCommand {

    private String input;

    public List<String> toCredentials() {
        if (input == null || input.length() == 0) {
            return new ArrayList<>();
        } else {
            return Stream.of(input.split("\\s+"))
                    .collect(Collectors.toList());
        }
    }

}
