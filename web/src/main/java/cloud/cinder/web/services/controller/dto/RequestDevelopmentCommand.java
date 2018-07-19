package cloud.cinder.web.services.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class RequestDevelopmentCommand {

    private String name;
    @NotEmpty
    private String email;
    @NotEmpty
    private String message;
    @NotNull
    private String title;

    public String toContent() {
        return "email:\t" + getEmail() + "\ntitle:\t" + getTitle() + "\nmessage:\t" + getMessage();
    }
}
