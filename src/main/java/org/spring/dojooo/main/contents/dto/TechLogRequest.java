package org.spring.dojooo.main.contents.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

//공통 요청 DTO
@Getter
@NoArgsConstructor
@AllArgsConstructor

public class MemoRequest {

    private String nickname;
    private String category;
    private List<String> tags;

}
