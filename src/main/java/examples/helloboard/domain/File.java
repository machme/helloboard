package examples.helloboard.domain;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class File {
    private Integer idx;
    private String name;
    private String path;
    private Integer size;
    private String format;
}
