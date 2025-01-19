package org.edu.commom.domain.request;

import lombok.Data;

@Data
public class baseReq {
    private int pageNum = 1;
    private int pageSize = 10;
    private String keyword;
}
