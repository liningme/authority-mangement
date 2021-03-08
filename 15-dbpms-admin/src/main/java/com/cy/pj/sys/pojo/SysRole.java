package com.cy.pj.sys.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysRole implements Serializable {
    private static final long serialVersionUID = -8557710039441592130L;
    private Integer id;
    private String name;
    private String note;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm",timezone = "GMT+8")
    private Date createdTime;
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm",timezone = "GMT+8")
    private Date modifiedTime;
    private String createdUser;
    private String modifiedUser;
}
