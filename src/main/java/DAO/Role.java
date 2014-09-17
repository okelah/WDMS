package DAO;


public enum Role{
	Temp(1),
	Guest(2),
	SysAdmin(3),
	RegularEmployee(4),
	DeptManagement(5),
	CorpManagement(6);
			
			private final int roleId;
			private Role(int roleId){
				this.roleId = roleId;
			}
			
			public static Role setRole(int roleID){
				Role role=null;
				if(roleID==1){
					role= Role.Temp;
				}else if(roleID==2){
					role= Role.Guest;
				}else if(roleID==3){
					role= Role.SysAdmin;
				}else if(roleID==4){
					role= Role.RegularEmployee;
				}else if(roleID==5){
					role= Role.DeptManagement;
				}else if(roleID==6){
					role= Role.CorpManagement;
				}
				return role;
			}
			
			public int getRoleId(){
				return roleId;
			}
}

