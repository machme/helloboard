package examples.helloboard.domain;

import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor

public class BoardTag {
    private Integer idx;
    private Integer boardIdx;
    private Integer tagIdx;
}
