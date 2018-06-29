package cloud.cinder.web.tools.controller.command;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
@Data
public class TestPrivateKeysCommand {

    private String input;


    public List<String> toPrivateKeys() {
        if (input == null || input.length() == 0) {
            return new ArrayList<>();
        } else {
            return Stream.of(input.split("\\s+"))
                    .collect(Collectors.toList());
        }
    }
}
