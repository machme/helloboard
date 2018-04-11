package examples.helloboard.domain;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private int roleId;
    private String description;
}
