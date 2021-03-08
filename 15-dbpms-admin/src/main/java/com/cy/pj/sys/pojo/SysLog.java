package com.cy.pj.sys.pojo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class SysLog implements Serializable {
   private static final long serialVersionUID = -1592163223057343412L;
   private Integer id;
   private String ip;
   private String username;
   private String operation;
   private String method;
   private String params;
   private Long time;
   private Date createdTime;
}
