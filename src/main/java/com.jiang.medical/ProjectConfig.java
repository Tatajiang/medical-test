package com.jiang.medical;

import com.homolo.framework.bean.ProjectBeanNameGenerator;
import com.homolo.framework.dao.DomainObjectDao;
import com.homolo.framework.dao.hibernate.BaseHibernateDaoSupport;
import com.homolo.framework.events.EventTarget;
import com.homolo.framework.module.ModuleConfig;
import com.jiang.medical.platform.operation.domain.InspectRecord;
import com.jiang.medical.platform.operation.domain.Item;
import com.jiang.medical.platform.operation.domain.MedicalItems;
import com.jiang.medical.platform.operation.domain.Relevance;
import com.jiang.medical.platform.operation.domain.ReservationRecord;
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
	 * @Description: 体检套餐
	 * @Param: []
	 * @return: com.homolo.framework.dao.DomainObjectDao<com.jiang.medical.platform.operation.domain.MedicalItems>
	 * @Author: zhantuo.jiang
	 * @date: 2019/11/20 20:16
	 */
	@Bean(name = PREFIX + "MedicalItemsDao")
	public DomainObjectDao<MedicalItems> generateMedicalItemsDao() {
		return new BaseHibernateDaoSupport<MedicalItems>(MedicalItems.class);
	}


	/* *
	 * @Description: 项目内容
	 * @Param: []
	 * @return: com.homolo.framework.dao.DomainObjectDao<com.jiang.medical.platform.operation.domain.Item>
	 * @Author: zhantuo.jiang
	 * @date: 2019/11/23 13:26
	 */
	@Bean(name = PREFIX + "ItemDao")
	public DomainObjectDao<Item> generateItemDao() {
		return new BaseHibernateDaoSupport<Item>(Item.class);
	}

	/* *
	 * @Description: 关联表
	 * @Param: []
	 * @return: com.homolo.framework.dao.DomainObjectDao<com.jiang.medical.platform.operation.domain.Relevance>
	 * @Author: zhantuo.jiang
	 * @date: 2019/12/3 14:14
	 */
	@Bean(name = PREFIX + "RelevanceDao")
	public DomainObjectDao<Relevance> generateRelevanceDao() {
		return new BaseHibernateDaoSupport<Relevance>(Relevance.class);
	}

	/* *
	 * @Description: 预约记录
	 * @Param: []
	 * @return: com.homolo.framework.dao.DomainObjectDao<com.jiang.medical.platform.operation.domain.ReservationRecord>
	 * @Author: zhantuo.jiang
	 * @date: 2019/12/3 14:17
	 */
	@Bean(name = PREFIX + "ReservationRecordDao")
	public DomainObjectDao<ReservationRecord> generateReservationRecordDao() {
		return new BaseHibernateDaoSupport<ReservationRecord>(ReservationRecord.class);
	}

	/* *
	 * @Description: 体检记录
	 * @Param: []
	 * @return: com.homolo.framework.dao.DomainObjectDao<com.jiang.medical.platform.operation.domain.InspectRecord>
	 * @Author: zhantuo.jiang
	 * @date: 2019/12/18 10:24
	 */
	@Bean(name = PREFIX + "InspectRecordDao")
	public DomainObjectDao<InspectRecord> generateInspectRecordDao() {
		return new BaseHibernateDaoSupport<InspectRecord>(InspectRecord.class);
	}

}
