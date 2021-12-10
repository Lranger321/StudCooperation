package main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class PassDTO {

    private String name;
    private Boolean isPassed;
}
