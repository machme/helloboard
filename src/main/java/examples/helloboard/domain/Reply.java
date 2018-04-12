package examples.helloboard.domain;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class Reply {
    private Integer idx;
    private Integer userIdx;
    private String content;
    private Integer boardIdx;
    private String date;
}
