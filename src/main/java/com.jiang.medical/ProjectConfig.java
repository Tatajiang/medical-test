package com.jiang.medical;

import com.homolo.framework.bean.ProjectBeanNameGenerator;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.hibernate.BaseHibernateDaoSupport;
import com.homolo.framework.events.EventTarget;
import com.homolo.framework.module.ModuleConfig;
import com.jiang.medical.platform.operation.domain.MedicalItem;
import com.jiang.medical.platform.system.domain.LoginLog;
import com.jiang.medical.platform.system.domain.Operation;
import com.jiang.medical.platform.system.domain.SysRoleOperation;
import com.jiang.medical.platform.system.domain.SysRoles;
import com.jiang.medical.platform.system.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ModuleConfig(name = ProjectConfig.NAME, domainPackages = {
	"com.jiang.medical"
})
@ComponentScan(basePackages = { "com.jiang.medical" }, nameGenerator = ProjectBeanNameGenerator.class)
@PropertySource(name = "medical.env", value = { "classpath:jiang/proj.properties" })
public class ProjectConfig {

	public static final String NAME = "medical";

	public static final String PREFIX = NAME + ".";

	@Bean(name = PREFIX + "eventTarget")
	public EventTarget generateEventTarget() {
		return new EventTarget();
	}
	
	@Bean(name = PREFIX + "UserDao")
	public DomainObjectDao<User> generateUserDao() {
		return new BaseHibernateDaoSupport<User>(User.class);
	}
	
	@Bean(name = PREFIX + "OperationDao")
	public DomainObjectDao<Operation> generateOperationDao() {
		return new BaseHibernateDaoSupport<Operation>(Operation.class);
	}
	
	@Bean(name = PREFIX + "SysRolesDao")
	public DomainObjectDao<SysRoles> generateSysRolesDao() {
		return new BaseHibernateDaoSupport<SysRoles>(SysRoles.class);
	}
	
	@Bean(name = PREFIX + "SysRoleOperationDao")
	public DomainObjectDao<SysRoleOperation> generateSysRoleOperationDao() {
		return new BaseHibernateDaoSupport<SysRoleOperation>(SysRoleOperation.class);
	}

	@Bean(name = PREFIX + "LoginLogDao")
	public DomainObjectDao<LoginLog> generateLoginLogDao() {
		return new BaseHibernateDaoSupport<LoginLog>(LoginLog.class);
	}


	/* *
	 * @Description: 体检项目
	 * @Param: []
	 * @return: com.homolo.framework.dao.DomainObjectDao<com.jiang.medical.platform.operation.domain.MedicalItem>
	 * @Author: zhantuo.jiang
	 * @date: 2019/11/20 20:16
	 */
	@Bean(name = PREFIX + "MedicalItemDao")
	public DomainObjectDao<MedicalItem> generateMedicalItemDao() {
		return new BaseHibernateDaoSupport<MedicalItem>(MedicalItem.class);
	}
	
}
