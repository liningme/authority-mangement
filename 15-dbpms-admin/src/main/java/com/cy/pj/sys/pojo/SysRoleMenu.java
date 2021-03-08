package com.cy.pj.sys.pojo;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 借助此对象封装查询到的角色以及角色对应的菜单id。
 * 查询方案如下：首先要确定数据来源(sys_role,sys_role_menus)
 * 1)方案1：业务层发起多次单表查询(一张表一张的查询)
 * 2)方案2：数据层进行表关联查询(left join)
 * 3)方案3：数据层进行嵌套查询(嵌套select)
 */
@Data
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = -2671028987524519218L;
    private Integer id;
    private String name;
    private String note;
    private List<Integer> menuIds;
}
