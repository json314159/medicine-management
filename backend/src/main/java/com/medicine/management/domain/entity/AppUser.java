package com.medicine.management.domain.entity;
import jakarta.persistence.*;
@Entity @Table(name="app_users")
public class AppUser { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @Column(unique=true,nullable=false,length=50) private String username; @Column(nullable=false) private String password; @Column(nullable=false,length=20) private String role="OPERATOR"; @Column(nullable=false) private boolean enabled=true;
 public Long getId(){return id;} public String getUsername(){return username;} public void setUsername(String v){username=v;} public String getPassword(){return password;} public void setPassword(String v){password=v;} public String getRole(){return role;} public void setRole(String v){role=v;} public boolean isEnabled(){return enabled;} public void setEnabled(boolean v){enabled=v;} }
