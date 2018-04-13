package examples.helloboard.domain;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    private Integer idx;
    private String title;
    private String content;
    private Integer fileIdx;
    private Integer userIdx;
    private String date;
    private Integer topicIdx;
    private Integer view;
    private Integer great;
}
