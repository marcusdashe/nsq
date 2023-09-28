/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cstemp.nsq.services;

import lombok.extern.slf4j.Slf4j;
import org.cstemp.nsq.payload.BaseResponse;
import org.cstemp.nsq.payload.ReportPayload;
import org.cstemp.nsq.repos.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author chibuezeharry & MarcusDashe
 */
@Service
@Slf4j
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    public ResponseEntity<?> getLogSheetReports(String traineeId, Integer logSheetIndex) {

        return ResponseEntity.ok(new BaseResponse(true, "Reports retrieved successfully", null));
    }

    public ResponseEntity<?> getUserReports(String userId) {

        return ResponseEntity.ok(new BaseResponse(true, "Reports retrieved successfully", null));
    }

    public ResponseEntity<?> addReport(ReportPayload.ReportRequest reportRequest, UserDetails userDetails) {

        return ResponseEntity.ok(new BaseResponse(true, "Report added successfully", null));

    }

    public ResponseEntity<?> deleteReport(ReportPayload.ReportRequest reportRequest, UserDetails userDetails) {

        return ResponseEntity.ok(new BaseResponse(true, "Report deleted successfully", null));
    }

}
