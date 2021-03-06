package com.entity;
// Generated Aug 15, 2018 2:16:27 AM by Hibernate Tools 5.1.8.Final


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

/**
 * Admin generated by hbm2java
 */
@Entity
@Table(name="admin"
    ,catalog="webshop2"
)
@Proxy(lazy = false)
public class Admin  implements java.io.Serializable {


     private String adminId;
     private Role role;
     private String adminEmail;
     private String aminPass;

    public Admin() {
    }

    public Admin(String adminId, Role role, String adminEmail, String aminPass) {
       this.adminId = adminId;
       this.role = role;
       this.adminEmail = adminEmail;
       this.aminPass = aminPass;
    }
   
     @Id 

    
    @Column(name="admin_id", unique=true, nullable=false, length=45)
    public String getAdminId() {
        return this.adminId;
    }
    
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="role_id", nullable=false)
    public Role getRole() {
        return this.role;
    }
    
    public void setRole(Role role) {
        this.role = role;
    }

    
    @Column(name="admin_email", nullable=false, length=45)
    public String getAdminEmail() {
        return this.adminEmail;
    }
    
    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    
    @Column(name="amin_pass", nullable=false, length=45)
    public String getAminPass() {
        return this.aminPass;
    }
    
    public void setAminPass(String aminPass) {
        this.aminPass = aminPass;
    }




}


