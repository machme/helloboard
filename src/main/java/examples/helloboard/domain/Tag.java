package examples.helloboard.domain;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Tag {
    private Integer idx;
    private String name;
    private Integer refCount;
}
