package com.finalproject.dontbeweak.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Api {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
//    @Column
//    private String ENTRPS;

    @Column
    private String PRDUCT;

//    @Column
//    private String STTEMNT_NO;
//
//    @Column
//    private String REGIST_DT;
//
//    @Column
//    private String DISTB_PD;
//
//    @Column
//    private String SUNGSANG;

    @Column
    private String SRV_USE;

//    @Column
//    private String PRSRV_PD;
//
//    @Column
//    private String INTAKE_HINT1;
//
//    @Column
//    private String MAIN_FNCTN;
//
//    @Column
//    private String BASE_STANDARD;

}
