package org.spring.dojooo.main.contents.dto;

import lombok.*;

@Getter
public class TechLogDeleteRequest {
    private Long techLogId;
    public TechLogDeleteRequest(Long techLogId) {
        this.techLogId = techLogId;
    }

    public static TechLogDeleteRequest of(Long techLogId) {
        return new TechLogDeleteRequest(techLogId);
    }
}

