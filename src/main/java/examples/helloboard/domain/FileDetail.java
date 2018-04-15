package examples.helloboard.domain;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class FileDetail {
    private Integer idx;
    private String name;
    private String path;
    private Long size;
    private String format;
    private Integer boardIdx;
}
